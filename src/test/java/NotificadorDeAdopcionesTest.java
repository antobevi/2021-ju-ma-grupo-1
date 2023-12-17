import adopciones.notificador.NotificadorDeAdopciones;
import adopciones.preguntas.Pregunta;
import adopciones.preguntas.PreguntaRespondida;
import mapa.Ubicacion;
import mascotas.Mascota;
import mascotas.MascotaBuilder;
import mediosDeContacto.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import publicaciones.PublicacionAdopcion;
import publicaciones.PublicacionAdoptante;
import repositorios.RepositorioAsociaciones;
import repositorios.RepositorioMascotasEncontradas;
import usuarios.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificadorDeAdopcionesTest {

  private NotificadorDeAdopciones notificadorDeAdopciones;
  private MailerGmail mailerMock;

  @BeforeEach
  public void setUp() {
    notificadorDeAdopciones = new NotificadorDeAdopciones();
    RepositorioAsociaciones.getRepositorio().eliminarAsociacionesGuardadas();
    mailerMock = Mockito.mock(MailerGmail.class);
    ServiceLocator.getServiceLocator().registrar(MailerGmail.class, mailerMock);
  }

  @AfterEach
  public void limpiarServicios(){
    ServiceLocator.getServiceLocator().eliminarServicios();
  }

  @Test
  public void adoptanteQueTienePatioDeberiaRecibirUnaRecomendacion() {
    Pregunta pregunta = preguntaDeSiNecesitaPatio();
    PublicacionAdopcion publicacionAdopcion = new PublicacionAdopcion(mascotaPerro("Firulais"), Arrays.asList(generarPreguntaRespondida(pregunta, "Si")));

    Adoptante adoptante = adoptante(unaListaDeContactos());
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, Arrays.asList(generarPreguntaRespondida(pregunta, "Si")));

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    Mockito.verify(mailerMock, Mockito.only()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueNoTienePatioNoDeberiaRecibirUnaRecomendacion() {
    Pregunta pregunta = preguntaDeSiNecesitaPatio();
    PublicacionAdopcion publicacionAdopcion = new PublicacionAdopcion(mascotaPerro("Firulais"), Arrays.asList(generarPreguntaRespondida(pregunta, "Si")));

    Adoptante adoptante = adoptante(unaListaDeContactos());
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, Arrays.asList(generarPreguntaRespondida(pregunta, "No")));

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertFalse(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    Mockito.verify(mailerMock, Mockito.never()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueTienePatioYNoTieneOtrasMascotasDeberiaRecibirUnaRecomendacion() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    Pregunta preguntaOtrasMascotas = preguntaDeSiPuedeEstarConOtrasMascotas();
    List<PreguntaRespondida> respuestas = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "Si"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "No")
    );

    PublicacionAdopcion publicacionAdopcion = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestas);

    Adoptante adoptante = adoptante(unaListaDeContactos());
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, respuestas);

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    Mockito.verify(mailerMock, Mockito.only()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueTienePatioYTieneOtrasMascotasNoDeberiaRecibirUnaRecomendacion() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    Pregunta preguntaOtrasMascotas = preguntaDeSiPuedeEstarConOtrasMascotas();
    List<PreguntaRespondida> respuestasAdopcion = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "Si"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "No")
    );

    PublicacionAdopcion publicacionAdopcion = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestasAdopcion);


    Adoptante adoptante = adoptante(unaListaDeContactos());
    List<PreguntaRespondida> respuestasAdoptante = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "Si"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "Si")
    );
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, respuestasAdoptante);

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertFalse(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    Mockito.verify(mailerMock, Mockito.never()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueTienePatioYNoTieneOtrasMascotasDeberiaRecibirDosRecomendaciones() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    Pregunta preguntaOtrasMascotas = preguntaDeSiPuedeEstarConOtrasMascotas();
    List<PreguntaRespondida> respuestasAdopcion = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "Si"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "No")
    );

    PublicacionAdopcion publicacionAdopcion1 = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestasAdopcion);
    PublicacionAdopcion publicacionAdopcion2 = new PublicacionAdopcion(mascotaPerro("Blanca"), respuestasAdopcion);

    Adoptante adoptante = adoptante(unaListaDeContactos());
    List<PreguntaRespondida> respuestasAdoptante = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "Si"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "No")
    );
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, respuestasAdoptante);

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion1);
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion2);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante.getPublicacionesRecomendadas().containsAll(Arrays.asList(publicacionAdopcion2, publicacionAdopcion1)));
    Mockito.verify(mailerMock, Mockito.only()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueNoTienePatioYNoTieneOtrasMascotasDeberiaRecibirUnaRecomendacion() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    Pregunta preguntaOtrasMascotas = preguntaDeSiPuedeEstarConOtrasMascotas();
    List<PreguntaRespondida> respuestasAdopcion1 = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "Si"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "No")
    );
    List<PreguntaRespondida> respuestasAdopcion2 = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "No"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "No")
    );

    PublicacionAdopcion publicacionAdopcion1 = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestasAdopcion1);
    PublicacionAdopcion publicacionAdopcion2 = new PublicacionAdopcion(mascotaPerro("Blanca"), respuestasAdopcion2);

    Adoptante adoptante = adoptante(unaListaDeContactos());
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, respuestasAdopcion2);

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion1);
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion2);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion2));
    assertFalse(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion1));
    Mockito.verify(mailerMock, Mockito.only()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueTienePatioDeberiaRecibirUnaRecomendacionAunqueSeHayaAgregadoOtraPregunta() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    Adoptante adoptante = adoptante(unaListaDeContactos());
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, Arrays.asList(generarPreguntaRespondida(preguntaPatio, "Si")));

    Pregunta preguntaOtrasMascotas = preguntaDeSiPuedeEstarConOtrasMascotas();
    List<PreguntaRespondida> respuestasAdopcion = Arrays.asList(
        generarPreguntaRespondida(preguntaPatio, "Si"),
        generarPreguntaRespondida(preguntaOtrasMascotas, "No")
    );
    PublicacionAdopcion publicacionAdopcion1 = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestasAdopcion);

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion1);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion1));
    Mockito.verify(mailerMock, Mockito.only()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptantesQueTienenPatioDeberianRecibirUnaRecomendacion() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    List<PreguntaRespondida> respuestas = Arrays.asList(generarPreguntaRespondida(preguntaPatio, "Si"));
    PublicacionAdopcion publicacionAdopcion = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestas);

    Adoptante adoptante1 = adoptante(unaListaDeContactos());
    Adoptante adoptante2 = adoptante(unaListaDeContactos());
    PublicacionAdoptante publicacionAdoptante1 = new PublicacionAdoptante(adoptante1, respuestas);
    PublicacionAdoptante publicacionAdoptante2 = new PublicacionAdoptante(adoptante2, respuestas);

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante1);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante2);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante1.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    assertTrue(adoptante2.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    Mockito.verify(mailerMock, Mockito.times(2)).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueTienePatioDeberiaRecibirUnaRecomendacionYAdoptanteSinPatioNoDeberiaRecibir() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    List<PreguntaRespondida> respuestas = Arrays.asList(generarPreguntaRespondida(preguntaPatio, "Si"));
    PublicacionAdopcion publicacionAdopcion = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestas);

    Adoptante adoptante1 = adoptante(unaListaDeContactos());
    Adoptante adoptante2 = adoptante(unaListaDeContactos());
    PublicacionAdoptante publicacionAdoptante1 = new PublicacionAdoptante(adoptante1, respuestas);
    PublicacionAdoptante publicacionAdoptante2 = new PublicacionAdoptante(adoptante2, Arrays.asList(generarPreguntaRespondida(preguntaPatio, "No")));

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante1);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante2);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante1.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    assertFalse(adoptante2.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    Mockito.verify(mailerMock, Mockito.only()).enviarMail(Mockito.any(Mail.class));
  }

  @Test
  public void adoptanteQueTienePatioDeberiaRecibirUnaRecomendacionPorDosMailsSiTieneDosContactos() {
    Pregunta preguntaPatio = preguntaDeSiNecesitaPatio();
    List<PreguntaRespondida> respuestas = Arrays.asList(generarPreguntaRespondida(preguntaPatio, "Si"));
    PublicacionAdopcion publicacionAdopcion = new PublicacionAdopcion(mascotaPerro("Firulais"), respuestas);

    Contacto contacto1 = new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com");
    Contacto contacto2 = new Contacto("Juan2", "Perez2", "1112345679", "juanperez2@gmail.com");
    Adoptante adoptante = adoptante(unaListaDeContactos(contacto1, contacto2));
    PublicacionAdoptante publicacionAdoptante = new PublicacionAdoptante(adoptante, respuestas);

    Asociacion asociacion = new Asociacion(new Ubicacion(4, 1));
    asociacion.agregarPublicacionAdopcion(publicacionAdopcion);
    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    notificadorDeAdopciones.notificar();
    //
    assertTrue(adoptante.getPublicacionesRecomendadas().contains(publicacionAdopcion));
    Mockito.verify(mailerMock, Mockito.times(2)).enviarMail(Mockito.any(Mail.class));
  }


  // Creaciones
  private Mascota mascotaPerro(String nombre) {
    return new MascotaBuilder()
        .perroConNombre(nombre)
        .setCaracteristicas(Collections.emptyList())
        .setFotos(Collections.emptyList())
        .setDuenio(unDuenio())
        .build();
  }

  private Duenio unDuenio() {
    Contacto contacto = new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com");
    List<Contacto> contactos = new ArrayList<>();
    contactos.add(contacto);
    return new Duenio("Pedro", "2021/05/03", TipoDocumento.DNI, 11111111, "username", "Mypass123$", contactos, new ArrayList<>());
  }

  private PreguntaRespondida generarPreguntaRespondida(Pregunta pregunta, String respuesta){
    return pregunta.responder(respuesta);
  }

  private Pregunta preguntaDeSiNecesitaPatio(){
    return new Pregunta("Necesita patio?", "Tiene patio?", Arrays.asList("Si", "No"));
  }

  private Pregunta preguntaDeSiPuedeEstarConOtrasMascotas(){
    return new Pregunta("Puede estar con otras mascotas?", "Tiene otras mascotas?", Arrays.asList("Si", "No"));
  }

  private Adoptante adoptante(List<Contacto> contactos){
    Contacto contacto = new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com");
    Adoptante adoptante = new Adoptante("Angel Martinez", LocalDate.of(1989,8, 15), TipoDocumento.DNI,
        32457938, contactos, unaListaDeMediosDeContactos(new MailerGmail()));
    //adoptante.setContactos(contactos());
    return adoptante;
  }

  private List<Contacto> unaListaDeContactos(Contacto... contactos) {
    List<Contacto> lista = new ArrayList<>();
    Collections.addAll(lista, contactos);
    return lista;
  }

  private List<Contacto> unaListaDeContactos(){
    return Arrays.asList(new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com"));
  }

  private List<MedioDeContacto> unaListaDeMediosDeContactos(MedioDeContacto ... medios) {
    List<MedioDeContacto> lista = new ArrayList<>();
    Collections.addAll(lista, medios);
    return lista;
  }
}
