package repositorios;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import usuarios.Admin;
import usuarios.Adoptante;
import usuarios.Duenio;
import usuarios.Voluntario;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RepositorioUsuarios implements WithGlobalEntityManager {

  private static RepositorioUsuarios instance = new RepositorioUsuarios();

  public static RepositorioUsuarios getInstance() {
    if (instance == null) {
      instance = new RepositorioUsuarios();
    }
    return instance;
  }

  public Duenio checkUser(String username){
    List<Duenio> duenios = entityManager().createQuery("FROM Duenio where username = :username")
        .setParameter("username", username)
        .getResultList();
    if (duenios.size() > 0) {
      return duenios.get(0);
    } else {
      return null;
    }
  }

  public Admin checkAdmin(String username){
    List<Admin> admins = entityManager().createQuery("FROM Admin where username = :username")
        .setParameter("username", username)
        .getResultList();
    if (admins.size() > 0) {
      return admins.get(0);
    } else {
      return null;
    }
  }

  public void agregarAdoptante(Adoptante adoptante) {
    entityManager().persist(adoptante);
  }

  public void agregarDuenio(Duenio duenio) {
    entityManager().persist(duenio);
//    duenios.add(duenio);
  }

  public void agregarAdmin(Admin admin) {
    entityManager().persist(admin);
    //admins.add(admin);
  }

  public void agregarVoluntario(Voluntario voluntario) {
    entityManager().persist(voluntario);
  }

  /* TODO: Se podría agregar una interfaz usuario para simplificar el agregar usuarios al repo?
   public void agregarUsuario(Usuario usuario) {
    EntityTransaction transaction = entityManager().getTransaction();
    transaction.begin();
    entityManager().persist(usuario);
    transaction.commit();
  }
  */

  public List<Adoptante> getAdoptantes() {
    return entityManager().createQuery("from Adoptante").getResultList();
  }

  public List<Duenio> getDuenios() {
    return entityManager().createQuery("from Duenio").getResultList();
  }

  public List<Admin> getAdmins() {
    return entityManager().createQuery("from Admin").getResultList();
  }

  public List<Voluntario> getVoluntarios() {
    return entityManager().createQuery("from Voluntario").getResultList();
  }

  // Chequear si algun admin o duenio tiene el mismo username
  public boolean usernameDisponible(String username) {
    List<Duenio> duenios = entityManager().createQuery("from Duenio where username = :username")
        .setParameter("username", username)
        .getResultList();
    List<Admin> admins = entityManager().createQuery("from Admin where username = :username")
        .setParameter("username", username)
        .getResultList();
    return duenios.size() == 0 && admins.size() == 0;
  }

}