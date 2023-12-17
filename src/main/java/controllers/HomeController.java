package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController {

  public ModelAndView index(Request request, Response response) {

    if(request.session().attribute("usuario_logueado") != null) {
      return ModelAndViewBuilder.create(null, "home-user-logged-in.hbs", request);
    } else {
      return ModelAndViewBuilder.create(null, "home-log-in.hbs", request);
    }

  }

}
