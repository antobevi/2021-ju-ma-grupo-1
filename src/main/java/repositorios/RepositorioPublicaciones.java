package repositorios;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import publicaciones.PublicacionAdopcion;
import publicaciones.PublicacionAdoptante;

import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioPublicaciones implements WithGlobalEntityManager {

  private static RepositorioPublicaciones instance = new RepositorioPublicaciones();

  public static RepositorioPublicaciones getInstance() {
    return instance;
  }

  public void agregarPublicacionDeAdoptante(PublicacionAdoptante publicacionAdoptante) {
    EntityTransaction transaction = entityManager().getTransaction();
    transaction.begin();
    entityManager().persist(publicacionAdoptante);
    transaction.commit();
  }

  public void agregarPublicacionDeAdopcion(PublicacionAdopcion publicacionAdopcion) {
    EntityTransaction transaction = entityManager().getTransaction();
    transaction.begin();
    entityManager().persist(publicacionAdopcion);
    transaction.commit();
  }

  public List<PublicacionAdopcion> getPublicacionesDeAdopcion() {
    return entityManager().createQuery("from PublicacionAdopcion").getResultList();
  }

  public List<PublicacionAdoptante> getPublicacionesDeAdoptantes() {
    return entityManager().createQuery("from PublicacionAdoptante").getResultList();
  }

}
