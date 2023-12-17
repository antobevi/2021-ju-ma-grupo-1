package controllers;

import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuarios.Admin;
import usuarios.Duenio;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

  RepositorioUsuarios repo = new RepositorioUsuarios();

  public ModelAndView index(Request request, Response response) {

    return new ModelAndView(null,"login.hbs");
  }

  public ModelAndView indexAdmin(Request request, Response response) {

    return new ModelAndView(null,"login-admin.hbs");
  }

  public ModelAndView login(Request request, Response response) {
    String usuario = request.queryParams("Username");
    String password = request.queryParams("Password");

    Duenio usuarioEncontrado = repo.checkUser(usuario);

    Map<String, Object> model = new HashMap<>();
    model.put("username", usuario);

    if (usuarioEncontrado != null) {

      if(usuarioEncontrado.getPassword().equals(password)) {
        request.session(true);
        request.session().attribute("usuario_logueado", usuarioEncontrado);
        request.session().removeAttribute("admin_logueado");
        request.session().maxInactiveInterval(3600);

        response.redirect("/home");
      } else {
        model.put("mensajeError", "Usuario o contraseña incorrectos");

        return new ModelAndView(model, "login.hbs");
      }

    } else{
      // No se encontro el usuario por el username o contraseña => mostrar algun mensaje de error
      model.put("mensajeError", "Usuario o contraseña incorrectos");

      return new ModelAndView(model, "login.hbs");
    }

    return null;
  }

  public ModelAndView loginAdmin(Request request, Response response) {
    String usuario = request.queryParams("Username");
    String password = request.queryParams("Password");

    // Para el admin le mando la password para que no falle algo en Router
    Admin adminEncontrado = repo.checkAdmin(usuario);

    Map<String, Object> model = new HashMap<>();
    model.put("username", usuario);

    if(adminEncontrado != null) {

      if(adminEncontrado.getPassword().equals(password)) {
        request.session(true);
        request.session().attribute("admin_logueado", adminEncontrado);
        request.session().removeAttribute("usuario_logueado");
        request.session().maxInactiveInterval(3600);

        response.redirect("/caracteristicas");
      } else {
        model.put("mensajeError", "Usuario o contraseña incorrectos");

        return new ModelAndView(model, "login-admin.hbs");
      }

    } else {
      model.put("mensajeError", "Usuario o contraseña incorrectos"); // No existe un admin con ese usuario

      return new ModelAndView(model, "login-admin.hbs");
    }

    return null;
  }

  public ModelAndView removeSession(Request request, Response response) {

    request.session().removeAttribute("usuario_logueado");
    request.session().removeAttribute("admin_logueado");
    response.redirect("/home");

    return null;
  }

}
