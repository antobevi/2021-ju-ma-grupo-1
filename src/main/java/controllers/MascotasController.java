package controllers;

import aws.UploadImageService;
import caracteristicas.CaracteristicaMascota;
import mascotas.Mascota;
import mascotas.MascotaBuilder;
import mascotas.Sexo;
import mascotas.TipoMascota;
import repositorios.RepositorioCaracteristicas;
import repositorios.RepositorioMascotas;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuarios.Duenio;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

public class MascotasController {

  public ModelAndView indexQR(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();
    model.put("id", req.params(":id"));
    return ModelAndViewBuilder.create(model, "mascota-qr.hbs", req);
  }

  public ModelAndView indexCrear(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();
    model.put("caracteristicas", RepositorioCaracteristicas.getRepositorio().getCaracteristicas());
    model.put("registrada", false);
    return ModelAndViewBuilder.create(model, "mascota.hbs", req);
  }

  public ModelAndView crear(Request req, Response res) {
    Duenio duenio = req.session().attribute("usuario_logueado");

    // Para obtener las fotos y los campos ya que el form se envia encodeado como multipart
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

    Mascota mascota = new MascotaBuilder()
        .setDuenio(duenio)
        .setTipo(TipoMascota.valueOf(req.queryParams("tipoMascota")))
        .setNombre(req.queryParams("nombre"))
        .setApodo(req.queryParams("apodo"))
        .setEdad(Integer.parseInt(req.queryParams("edad")))
        .setSexo(Sexo.valueOf(req.queryParams("sexo")))
        .setDescripcion(req.queryParams("descripcion"))
        .setCaracteristicas(this.getCaracteristicasFromRequest(req))
        .setFotos(Collections.emptyList())
        .build();

    Map<String, Object> model = new HashMap<>();
    model.put("caracteristicas", RepositorioCaracteristicas.getRepositorio().getCaracteristicas());

    try {
      mascota.setFotos(this.getFotosFromRequest(req, duenio.getId()));
    } catch (Exception e) {
      model.put("mensajeError", "Error al subir la foto. Intente de nuevo.");
      return ModelAndViewBuilder.create(model, "mascota.hbs", req);
    }

    duenio.agregarMascota(mascota);

    model.put("registrada", true);
    model.put("id", mascota.getId());

    return ModelAndViewBuilder.create(model, "mascota.hbs", req);
  }

  public ModelAndView index(Request req, Response res) {
    Duenio duenio = req.session().attribute("usuario_logueado");
    List<Mascota> mascotas = RepositorioMascotas.getInstance().getMascotasPorDuenio(duenio);
    Map<String, Object> model = new HashMap<>();
    model.put("mascotas", mascotas);
    return ModelAndViewBuilder.create(model, "mascotas.hbs", req);
  }


  private List<CaracteristicaMascota> getCaracteristicasFromRequest(Request req) {
    List<CaracteristicaMascota> caracteristicasMascota = new ArrayList<>();
    RepositorioCaracteristicas.getRepositorio().getCaracteristicas().forEach(caracteristica -> {
      String valorCaracteristica = req.queryParams("caracteristicas[" + caracteristica.getNombre() + "]");
      if (valorCaracteristica != null && !valorCaracteristica.isEmpty()) {
        caracteristicasMascota.add(new CaracteristicaMascota(caracteristica.getNombre(), valorCaracteristica));
      }
    });
    return caracteristicasMascota;
  }

  private List<String> getFotosFromRequest(Request req, Long duenioId) throws Exception {
    List<String> rutasFotos = new ArrayList<>();

    File uploadDir = new File("fotos-mascotas");
    boolean dir = uploadDir.mkdir();

    System.out.println("Se creo el directorio fotos-mascotas: " + dir);

    List<Part> partsFotos = req.raw().getParts().stream()
        .filter(part -> part.getName().equals("foto"))
        .collect(Collectors.toList());

    for (Part part : partsFotos) {
      // createTempFile asegura que no se repita el nombre del archivo. Prefijo user-id es solo por organizacion.
      Path tempFile = Files.createTempFile(uploadDir.toPath(), "user-" + duenioId + "_", "");

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
          uploadImageService.uploadFile(fileName.toString());
          rutasFotos.add(fileName.toString());
        }

      }
    }

    return rutasFotos;
  }

}
