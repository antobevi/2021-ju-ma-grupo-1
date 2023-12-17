import mediosDeContacto.MailerGmail;
import mediosDeContacto.MedioDeContacto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repositorios.RepositorioUsuarios;
import usuarios.Adoptante;
import usuarios.Contacto;
import usuarios.TipoDocumento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioUsuariosTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void before(){
    entityManager().getTransaction().begin();
  }

  @AfterEach
  public void after(){
    entityManager().getTransaction().rollback();
  }

  @Test
  public void persistirUnAdoptante() {
    Adoptante adoptante = this.adoptante();
    RepositorioUsuarios repositorioUsuarios = RepositorioUsuarios.getInstance();

    repositorioUsuarios.agregarAdoptante(adoptante);
    List<Adoptante> adoptantes = repositorioUsuarios.getAdoptantes();

    Assertions.assertEquals(1, adoptantes.size());
    Assertions.assertEquals(adoptante.getId(), adoptantes.stream().findFirst().get().getId());
  }

  // ------------------------------------------------- Instanciadores -------------------------------------------------

  private Adoptante adoptante() {
    Contacto contacto = new Contacto("Ricardo", "Rodriguez", "1156314975", "rickRodriguez98@gmail.com");

    return new Adoptante("Angel Martinez", LocalDate.of(1989,8, 15), TipoDocumento.DNI,
        32457938, unaListaDeContactos(contacto), unaListaDeMediosDeContactos(new MailerGmail()));
  }

  private List<Contacto> unaListaDeContactos(Contacto... contactos) {
    List<Contacto> lista = new ArrayList<>();
    Collections.addAll(lista, contactos);

    return lista;
  }

  private List<MedioDeContacto> unaListaDeMediosDeContactos(MedioDeContacto ... medios) {
    List<MedioDeContacto> lista = new ArrayList<>();
    Collections.addAll(lista, medios);

    return lista;
  }

}
