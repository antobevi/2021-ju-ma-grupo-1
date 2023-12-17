package controllers;

import repositorios.RepositorioUsuarios;
import spark.Request;
import spark.Response;
import usuarios.Admin;
import usuarios.Duenio;
import usuarios.TipoDocumento;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DebugController {
  public String loginComoAdmin(Request req, Response res) throws NoSuchAlgorithmException {
    //Crear usuario admin
    Admin admin = new Admin("a", LocalDate.now(), TipoDocumento.DNI, 2, "admin", "R_oot1337*");
    RepositorioUsuarios.getInstance().agregarAdmin(admin);

    //Registrar nueva sesion con el admin recien creado
    req.session(true);
    req.session().attribute("admin_logueado", admin);

    return "<h3>Admin creado y logueado</h3> <h4>Usuario: " + admin.getUsername() + "</h4><h4>Password: " + admin.getPassword() + "</h4>";
  }

  public String printCurrentSession(Request req, Response res) {
    String username = req.session().attribute("username");
    String password = req.session().attribute("password");
    return "<h3>Sesion actual:</h3><h4>Usuario: " + username + "</h4><h4>Password: " + password + "</h4>";
  }

  public String printUsuarios(Request req, Response res) {
    List<Admin> admins = RepositorioUsuarios.getInstance().getAdmins();
    List<Duenio> duenios = RepositorioUsuarios.getInstance().getDuenios();
    List<String> adminsToString = admins.stream()
        .map((e) -> "{ " + e.getUsername() + ", " + e.getPassword() + "}")
        .collect(Collectors.toList());
    List<String> dueniosToString = duenios.stream()
        .map((e) -> "{ " + e.getUsername() + ", " + e.getPassword() + "}")
        .collect(Collectors.toList());
    return "<h3>Admins:</h3><h4>" + adminsToString + "</h4><h3>Duenios:</h3><h4>" + dueniosToString + "</h4>";
  }
}
