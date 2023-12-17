package controllers;

import caracteristicas.Caracteristica;
import repositorios.RepositorioCaracteristicas;
import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuarios.Admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CaracteristicasController {
  private Map<String, Object> construirModelo(String mensajeVerde, String mensajeRojo, List<Caracteristica> caracteristicas) {
    Map<String, Object> model = new HashMap<>();
    model.put("caracteristicas", caracteristicas);
    model.put("mensajeVerde", mensajeVerde);
    model.put("mensajeRojo", mensajeRojo);

    return model;
  }

  private Map<String, Object> construirModelo(String mensajeVerde, String mensajeRojo) {
    List<Caracteristica> caracteristicas = RepositorioCaracteristicas.getRepositorio().getCaracteristicas();

    return construirModelo(mensajeVerde, mensajeRojo, caracteristicas);
  }

  public ModelAndView index(Request req, Response res) {
    Map<String, Object> model = construirModelo(null, null);
    return ModelAndViewBuilder.create(model, "caracteristicas.hbs", req);
  }

  public ModelAndView crear(Request req, Response res) {
    String nombre = req.queryParams("inputNombre");
    Map<String, Object> model;

    //Validar y construir modelo
    if (nombre.isEmpty()) {
      model = construirModelo(null, "La caracteristica debe tener al menos una letra.");
    } else {
      Caracteristica nuevaCaracteristica = new Caracteristica(nombre);
      RepositorioCaracteristicas.getRepositorio().agregarCaracteristica(nuevaCaracteristica);

      String mensajeVerde = "Se ha creado la caracteristica \"" + nombre + "\" con exito.";
      model = construirModelo(mensajeVerde, null);
    }
    return ModelAndViewBuilder.create(model, "caracteristicas.hbs", req);
  }

  public ModelAndView borrar(Request req, Response res) {
    Long id = Long.parseLong(req.params(":id"));
    RepositorioCaracteristicas.getRepositorio().borrarCaracteristicaPorId(id);
    Map<String, Object> model = construirModelo("Se ha borrado la caracteristica correctamente.", null);
    return ModelAndViewBuilder.create(model, "caracteristicas.hbs", req);
  }

  public ModelAndView indexEditar(Request req, Response res) {
    Long id = Long.parseLong(req.params(":id"));
    Caracteristica caracteristica = RepositorioCaracteristicas.getRepositorio().getCaracteristicaPorId(id);

    Map<String, Object> model;
    if (Objects.isNull(caracteristica)) {
      model = construirModelo(null, "No se ha encontrado ninguna caracteristica.");
      return ModelAndViewBuilder.create(model, "caracteristicas.hbs", req);
    }

    model = new HashMap<>();
    model.put("nombre", caracteristica.getNombre());
    return ModelAndViewBuilder.create(model, "caracteristicas_edit.hbs", req);
  }

  public ModelAndView editar(Request req, Response res) {
    Long id = Long.parseLong(req.params(":id"));
    String nombre = req.queryParams("inputNombre");

    Map<String, Object> model;
    if (nombre.isEmpty()) {
      model = construirModelo(null, "La caracteristica debe tener al menos una letra.", null);
      return ModelAndViewBuilder.create(model, "caracteristicas_edit.hbs", req);
    }

    RepositorioCaracteristicas.getRepositorio().editarCaracteristicaPorId(id, nombre);
    model = construirModelo("Se ha editado la caracteristica correctamente.", null, null);
    return ModelAndViewBuilder.create(model, "caracteristicas_edit.hbs", req);
  }
}
