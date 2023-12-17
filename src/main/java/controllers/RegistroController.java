package controllers;

//import javafx.collections.ModifiableObservableListBase;
import mediosDeContacto.MedioDeContacto;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuarios.Contacto;
import usuarios.Duenio;
import usuarios.TipoDocumento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RegistroController implements WithGlobalEntityManager, TransactionalOps, EntityManagerOps {


  public ModelAndView index(Request request, Response response) {
    return new ModelAndView(new HashMap<>(),"registro-duenio.hbs");
  }

  public ModelAndView confirmar(Request req, Response res) {

    String nombreDuenio = req.queryParams("Nombre");
    String nacimientoDuenio = req.queryParams("FechaNacimiento");
    nacimientoDuenio = nacimientoDuenio.replace("-","/");
    TipoDocumento tipo = TipoDocumento.valueOf(req.queryParams("TIPODOCUMENTO"));
    req.session().attribute("TipoDocumento", tipo);
    String numero = req.queryParams("Numero");
    String username = req.queryParams("username");
    String password = req.queryParams("password");

    String nombreContacto = req.queryParams("NombreContacto");
    String apellidoContacto = req.queryParams("ApellidoContacto");
    String telefonoContacto = req.queryParams("TelefonoContacto");
    String emailContacto = req.queryParams("EmailContacto");

    List<Contacto> contactos = new ArrayList<>();
    List<MedioDeContacto> medios = new ArrayList<>();

    Contacto contactoDuenio = new Contacto(nombreContacto, apellidoContacto, telefonoContacto, emailContacto);

    contactos.add(contactoDuenio);


    int nroDoc = Integer.parseInt(numero);
    //LocalDate date = LocalDate.parse(nacimientoDuenio, DateTimeFormatter.ISO_DATE);
    //Date date = new Date(nacimientoDuenio);
    //String nacimiento = date.toString();
    Duenio duenio = new Duenio(nombreDuenio, nacimientoDuenio, tipo, nroDoc, username, password, contactos, medios);

    if (RepositorioUsuarios.getInstance().usernameDisponible(username)) {
      RepositorioUsuarios.getInstance().agregarDuenio(duenio);
      res.redirect("/login");
    } else {
      Map<String, Object> model = new HashMap<>();
      // Para mantener los campos completados
      model.put("nombre", nombreDuenio);
      model.put("fechaNacimiento", nacimientoDuenio);
      model.put("numeroDocumento", numero);
      model.put("username", username);
      model.put("password", password);
      model.put("nombreContacto", nombreContacto);
      model.put("apellidoContacto", apellidoContacto);
      model.put("telefonoContacto", telefonoContacto);
      model.put("emailContacto", emailContacto);
      modelarTipoDocumento(model, tipo);
      model.put("mensajeError", "El nombre de usuario ya se encuentra registrado, por favor ingrese otro.");
      return new ModelAndView(model, "registro-duenio.hbs");
    }
    return null;
  }

  private Map<String, Object> modelarTipoDocumento(Map<String, Object> model, TipoDocumento tipo){
    if(TipoDocumento.DNI.equals(tipo)){
      model.put("dni", true);
    } else if(TipoDocumento.LE.equals(tipo)){
      model.put("le", true);
    } else if(TipoDocumento.CI.equals(tipo)){
      model.put("ci", true);
    } else {
      model.put("lc", true);
    }
    return model;
  }

}
