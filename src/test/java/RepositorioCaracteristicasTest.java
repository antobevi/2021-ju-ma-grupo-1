import caracteristicas.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repositorios.RepositorioCaracteristicas;
import usuarios.Admin;
import usuarios.TipoDocumento;

import javax.persistence.EntityTransaction;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RepositorioCaracteristicasTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @BeforeEach
  public void before(){
    entityManager().getTransaction().begin();
  }

  @AfterEach
  public void after(){
    entityManager().getTransaction().rollback();
  }

  @Test
  public void persistirCaracteristicasYRecuperarlas() throws NoSuchAlgorithmException {
    RepositorioCaracteristicas repo = RepositorioCaracteristicas.getRepositorio();
    Caracteristica c = new Caracteristica("colorPrincipal");
    Caracteristica c2 = new Caracteristica("tamanio");
    repo.agregarCaracteristica(c);
    repo.agregarCaracteristica(c2);

    assertTrue(repo.getCaracteristicas().stream()
        .anyMatch(dbc -> dbc.getNombre() == "colorPrincipal")
    );
    assertTrue(repo.getCaracteristicas().stream()
        .anyMatch(dbc -> dbc.getNombre() == "tamanio")
    );
  }

  //Instanciadores
  private Admin unAdmin() throws NoSuchAlgorithmException {
    return new Admin("Ozzy", LocalDate.now(), TipoDocumento.DNI, 15625514, "PrinceOfDarkness", "Yagni3210+");
  }

}
