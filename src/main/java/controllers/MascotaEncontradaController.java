package controllers;

import aws.UploadImageService;
import mapa.Ubicacion;
import mascotas.Mascota;
import mascotasEncontradas.MascotaEncontrada;
import mascotasEncontradas.MascotaEncontradaConChapita;
import mascotasEncontradas.PublicacionMascotaEncontrada;
import repositorios.RepositorioMascotas;
import repositorios.RepositorioMascotasEncontradas;
import rescatistas.Rescatista;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuarios.Contacto;
import usuarios.TipoDocumento;


import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MascotaEncontradaController {

  public MascotaEncontrada mascotaEncontrada(Request request) {
    // Datos del rescatista
    String nombreRescatista = request.queryParams("nombre-rescatista");
    String apellidoRescatista = request.queryParams("apellido-rescatista");
    TipoDocumento tipoDocumento = TipoDocumento.valueOf(request.queryParams("tipo-documento"));
    int numeroDocumento = Integer.parseInt(request.queryParams("numero-documento"));

    // Datos de contacto
    String nombreContacto = request.queryParams("nombre-contacto");
    String apellidoContacto = request.queryParams("apellido-contacto");
    String telefonoContacto = request.queryParams("telefono");
    String emailContacto = request.queryParams("email");

    Contacto contacto = new Contacto(nombreContacto, apellidoContacto, telefonoContacto, emailContacto);
    List<Contacto> listaContactos = new ArrayList<>();
    listaContactos.add(contacto);

    Rescatista rescatista = new Rescatista(nombreRescatista, apellidoRescatista, tipoDocumento, numeroDocumento, listaContactos);

    // Datos de la mascota encontrada
    String descripcion = request.queryParams("descripcion");
    String imagen = request.queryParams("imagen-mascota");


    Ubicacion ubicacion = new Ubicacion();
    ubicacion.setDireccion(request.queryParams("direccion"));
    ubicacion.setLatitud(Double.parseDouble(request.queryParams("latitud")));
    ubicacion.setLongitud(Double.parseDouble(request.queryParams("longitud")));

    List<String> fotos = new ArrayList<>();
    //fotos.add(imagen);


    return new MascotaEncontrada(fotos, descripcion,  ubicacion, LocalDate.now(), rescatista);
  }

  public ModelAndView encontradaMascotaSinChapita(Request request, Response response) {
    return ModelAndViewBuilder.create(null, "mascota-encontrada.hbs", request);
  }

  public ModelAndView encontradaMascotaConChapita(Request request, Response response) {

    Long id = Long.valueOf(request.params("id"));
    boolean estaRegistradoEnElSistema = RepositorioMascotas.getInstance().existeLaMascotaConID(id);

    if(estaRegistradoEnElSistema) {
      Map<String, Object> model = new HashMap<>();
      model.put("id", id);
      return ModelAndViewBuilder.create(model, "mascota-encontrada.hbs", request);
    } else {
      //response.redirect("/encontradas/new");
      return ModelAndViewBuilder.create(null, "encontrada_not_found.hbs", request);
    }

  }

  public ModelAndView crearPublicacion(Request request, Response response) {

    MascotaEncontrada mascotaEncontrada = mascotaEncontrada(request);
    try {
      mascotaEncontrada.setFotos(this.getFotosFromRequest(request));
    } catch (Exception e) {
      Map<String, Object> model = new HashMap<>();
      model.put("mensajeError", "Error al subir la foto. Intente de nuevo.");
      return ModelAndViewBuilder.create(model, "mascota-encontrada.hbs", request);
    }
    // Creo la publicacion de mascota encontrada
    PublicacionMascotaEncontrada publicacion = new PublicacionMascotaEncontrada(mascotaEncontrada, false);

    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarPublicacion(publicacion);

    Map<String, Object> model = new HashMap<>();
    model.put("mascotaEncontrada", mascotaEncontrada);
    return ModelAndViewBuilder.create(model, "mascota-encontrada.hbs", request);
  }

  public ModelAndView crearMascotaConChapita(Request request, Response response) {

    MascotaEncontrada mascotaEncontrada = mascotaEncontrada(request);
    try {
      mascotaEncontrada.setFotos(this.getFotosFromRequest(request));
    } catch (Exception e) {
      Map<String, Object> model = new HashMap<>();
      model.put("mensajeError", "Error al subir la foto. Intente de nuevo.");
      return ModelAndViewBuilder.create(model, "mascota-encontrada.hbs", request);
    }
    Mascota mascota = RepositorioMascotas.getInstance().getMascotaById(Long.valueOf(request.params("id")));


    // Creo mascota encontrada con chapita (tiene duenio)

    MascotaEncontradaConChapita mascotaEncontradaConChapita = new MascotaEncontradaConChapita(mascotaEncontrada, mascota);
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(mascotaEncontradaConChapita);
    //mascotaEncontradaConChapita.avisarDuenio();

    Map<String, Object> model = new HashMap<>();
    model.put("mascotaEncontrada", mascotaEncontrada);
    return ModelAndViewBuilder.create(model, "mascota-encontrada.hbs", request);
  }

  private List<String> getFotosFromRequest(Request req) throws Exception {
    List<String> rutasFotos = new ArrayList<>();

    File uploadDir = new File("fotos-mascotas-encontradas");
    boolean dir = uploadDir.mkdir();

    System.out.println("Se creo el directorio fotos-mascotas-encontradas: " + dir);

    List<Part> partsFotos = req.raw().getParts().stream()
        .filter(part -> part.getName().equals("foto"))
        .collect(Collectors.toList());

    for (Part part : partsFotos) {
      // createTempFile asegura que no se repita el nombre del archivo. Prefijo user-id es solo por organizacion.
      Path tempFile = Files.createTempFile(uploadDir.toPath(), "encontrada_", "");

      if(part != null){
        try (InputStream input = part.getInputStream()) {
          Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
      }

      // Hecho asi para pasar spotbugs.
      if (tempFile != null){
        Path fileName = tempFile.getFileName();
        if(fileName != null){
          UploadImageService uploadImageService = new UploadImageService();
          uploadImageService.uploadFileEncontrada(fileName.toString());
          rutasFotos.add(fileName.toString());
        }

      }
    }

    return rutasFotos;
  }
}
