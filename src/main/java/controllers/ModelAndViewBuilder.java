package controllers;

import spark.ModelAndView;
import spark.Request;
import usuarios.Admin;
import usuarios.Duenio;

import java.util.HashMap;
import java.util.Map;

public class ModelAndViewBuilder {

  public static ModelAndView create(Map<String, Object> model, String viewName, Request request) {
    if(model == null){
      model = new HashMap<>();
    }
    Admin admin = request.session().attribute("admin_logueado");
    Duenio usuario = request.session().attribute("usuario_logueado");

    model.put("usuario", usuario);
    model.put("admin", admin);
    model.put("logueado", usuario != null || admin != null);

    return new ModelAndView(model, viewName);
  }

}
