package repositorios;

import caracteristicas.Caracteristica;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;
import java.util.Objects;

public class RepositorioCaracteristicas implements WithGlobalEntityManager {
  private static RepositorioCaracteristicas instance = new RepositorioCaracteristicas();
//  private List<Caracteristica> caracteristicas;

  private RepositorioCaracteristicas() {
//    this.caracteristicas = new ArrayList<>();
  }

  public static RepositorioCaracteristicas getRepositorio() {
    return instance;
  }

  public void agregarCaracteristica(Caracteristica caracteristica) {
//    this.caracteristicas.add(caracteristica);
    entityManager().persist(caracteristica);
  }

  public void borrarCaracteristicaPorId(Long id) {
    Caracteristica caracteristica = entityManager().find(Caracteristica.class, id);
    if (Objects.nonNull(caracteristica)) {
      entityManager().remove(caracteristica);
    }
  }

  public void editarCaracteristicaPorId(Long id, String nuevoNombre) {
    Caracteristica caracteristica = entityManager().find(Caracteristica.class, id);
    if (Objects.nonNull(caracteristica)) {
      caracteristica.setNombre(nuevoNombre);
    }
  }

  public Caracteristica getCaracteristicaPorId(Long id) {
    return entityManager().find(Caracteristica.class, id);
  }

  public List<Caracteristica> getCaracteristicas() {
//    return caracteristicas;
    return entityManager().createQuery("FROM Caracteristica").getResultList();
  }

}
