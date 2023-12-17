import controllers.*;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import repositorios.RepositorioUsuarios;
import spark.Spark;
import spark.debug.DebugScreen;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.CapitalizeEnumHelper;
import spark.utils.HandlebarsTemplateEngineBuilder;
import usuarios.Admin;
import usuarios.Duenio;

import javax.persistence.EntityTransaction;
import java.util.Objects;

public class Router {

  public static void configure() {
    HandlebarsTemplateEngine engineTemplate = HandlebarsTemplateEngineBuilder
        .create()
        .withDefaultHelpers()
        .withHelper("capitalizeEnum", CapitalizeEnumHelper.capitalizeEnum)
        .build();

    HomeController homeController = new HomeController();
    CaracteristicasController caracteristicasController = new CaracteristicasController();
    LoginController loginController = new LoginController();
    RegistroController registroController = new RegistroController();
    MascotasController mascotasController = new MascotasController();
    MascotaEncontradaController mascotaEncontradaController = new MascotaEncontradaController();

    DebugScreen.enableDebugScreen();

    Spark.staticFiles.location("public");
    Spark.staticFiles.externalLocation("fotos-mascotas");


    //Matchear con o sin trailing slash
    Spark.before((req, res) -> {
      String path = req.pathInfo();
      if (!path.equals("/") && path.endsWith("/"))
        res.redirect(path.substring(0, path.length() - 1));
    });

    //Autenticar paginas de admins
    Spark.before((req, res) -> {
      String path = req.pathInfo();
      if (path.matches(".*caracteristicas.*")) {
        Admin admin = req.session().attribute("admin_logueado");
        if (Objects.isNull(admin)) {
          res.redirect("/login", 401);
        }
      }

      // Autenticacion para mascotas
      if(path.matches(".*mascotas.*")){
        Duenio duenio = req.session().attribute("usuario_logueado");
        if(Objects.isNull(duenio)){
          res.redirect("/login");
        }
      }
    });

    // TODO: revisar el tema de las transactions. Agregar que sea POST, PUT O DELETE
    Spark.before((req, res) -> {
        String method = req.requestMethod();
        if(method == "POST" || method == "PUT" || method == "DELETE" || method == "PATCH")
          PerThreadEntityManagers.getEntityManager().getTransaction().begin();
    });

    Spark.after((req, res) -> {
      EntityTransaction t = PerThreadEntityManagers.getEntityManager().getTransaction();
      if (t.isActive()) t.commit();


//       Para arreglar el problema con la cache y los hilos
      PerThreadEntityManagers.getEntityManager().clear();
    });

    //Controllers
    Spark.get("/home", homeController::index, engineTemplate);

    Spark.get("/login",loginController::index,engineTemplate);
    Spark.get("/login/admin", loginController::indexAdmin, engineTemplate);
    Spark.post("/login",loginController::login,engineTemplate);
    Spark.post("/login/admin", loginController::loginAdmin, engineTemplate);
    Spark.post("/sesssion/remove", loginController::removeSession, engineTemplate);

    Spark.get("/registro-duenio",registroController::index,engineTemplate);
    Spark.post("/confirmar-creacion",registroController::confirmar,engineTemplate);
    Spark.get("/caracteristicas", caracteristicasController::index, engineTemplate);
    Spark.post("/caracteristicas", caracteristicasController::crear, engineTemplate);
    Spark.post("/caracteristicas/:id/remove", caracteristicasController::borrar, engineTemplate);
    Spark.get("/caracteristicas/:id/edit", caracteristicasController::indexEditar, engineTemplate);
    Spark.post("/caracteristicas/:id/edit", caracteristicasController::editar, engineTemplate);

    // Mascotas
    Spark.get("/mascotas", mascotasController::index, engineTemplate);
    Spark.get("/mascotas/new", mascotasController::indexCrear, engineTemplate);
    Spark.post("/mascotas", mascotasController::crear, engineTemplate);
    Spark.get("/mascotas/:id/qr", mascotasController::indexQR, engineTemplate);


    // Mascotas Encontradas
    Spark.get("/encontradas/new", mascotaEncontradaController::encontradaMascotaSinChapita, engineTemplate);
    Spark.get("/encontradas/:id/new", mascotaEncontradaController::encontradaMascotaConChapita, engineTemplate);
    Spark.post("/encontradas/new", mascotaEncontradaController::crearPublicacion, engineTemplate);
    Spark.post("/encontradas/:id/new", mascotaEncontradaController::crearMascotaConChapita, engineTemplate);


    Spark.get("/", (req, res) -> {
      res.redirect("/home");
      return null;
    });
  }

}
