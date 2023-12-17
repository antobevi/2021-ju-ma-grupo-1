import adopciones.preguntas.Pregunta;
import adopciones.preguntas.PreguntaRespondida;
import caracteristicas.CaracteristicaMascota;
import mapa.Ubicacion;
import mascotas.Mascota;
import mascotas.MascotaBuilder;
import mascotas.Sexo;
import mascotas.TipoMascota;
import mediosDeContacto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import publicaciones.PublicacionAdopcion;
import publicaciones.PublicacionAdoptante;
import repositorios.RepositorioAsociaciones;
import usuarios.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class AdoptanteTest {
  private Contacto contacto1 = new Contacto("Ricardo", "Rodriguez", "1156314975", "rickRodriguez98@gmail.com");
  private Contacto contacto2 = new Contacto("Carolina", "Almada", "1134956310", "caro_almada@gmail.com");
  private Contacto contacto3 = new Contacto("Daniel", "Perez", "1145678923", "danielperez45@gmail.com");
  private MailerGmail mailerMock;
  private MensajeriaTwilio twilioMock;

  @BeforeEach
  public void init() {

    mailerMock = Mockito.mock(MailerGmail.class);
    twilioMock = Mockito.mock(MensajeriaTwilio.class);

    ServiceLocator.getServiceLocator().registrar(MailerGmail.class, mailerMock);
    ServiceLocator.getServiceLocator().registrar(MensajeriaTwilio.class, twilioMock);
  }

  @AfterEach
  public void borrarServicios() {
    ServiceLocator.getServiceLocator().eliminarServicios();
    RepositorioAsociaciones.getRepositorio().eliminarAsociacionesGuardadas();
  }


  @Test
  public void AdoptanteAlGenerarPublicacionSeAgregaALaAsociacionDePreferencia() {

    List<String> opciones = new ArrayList<>();
    opciones.add("Si");
    opciones.add("No");

    Asociacion asociacion = this.asociacion();
    PublicacionAdoptante publicacionAdoptante = this.publicacionAdoptante("Necesita patio", "Tiene patio", opciones, "Si");

    asociacion.agregarPublicacionAdoptante(publicacionAdoptante);

    Assertions.assertTrue(asociacion.getPublicacionesAdoptantes().contains(publicacionAdoptante));
  }

  @Test
  public void AdoptanteAlDarDeBajaLaPublicacionSeEliminaDeLaAsociacionElegida() {

    List<String> opciones1 = new ArrayList<>();
    opciones1.add("Si");
    opciones1.add("No");
    List<String> opciones2 = new ArrayList<>();
    opciones2.add("Perro");
    opciones2.add("Gato");

    Asociacion asociacion1 = this.asociacion();
    Asociacion asociacion2 = this.asociacion();
    PublicacionAdoptante publicacionAdoptante1 = this.publicacionAdoptante("Necesita patio", "Tiene patio", opciones1, "No");
    PublicacionAdoptante publicacionAdoptante2 = this.publicacionAdoptante("Tipo de mascota", "Tipo de mascota", opciones2, "Perro");

    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion1, asociacion2);
    asociacion1.agregarPublicacionAdoptante(publicacionAdoptante1);
    asociacion2.agregarPublicacionAdoptante(publicacionAdoptante2);

    publicacionAdoptante1.darDeBaja();

    Assertions.assertFalse(asociacion1.getPublicacionesAdoptantes().contains(publicacionAdoptante1));
  }

  @Test
  public void AsociacionACargoDeLaPublicacionEsLaElegidaPorElAdoptante() {

    List<String> opciones = new ArrayList<>();
    opciones.add("Perro");
    opciones.add("Gato");

    Asociacion asociacion1 = this.asociacion();
    Asociacion asociacion2 = this.asociacion();
    PublicacionAdoptante publicacionAdoptante = this.publicacionAdoptante("Tipo de mascota", "Tipo de mascota", opciones, "Gato");

    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion1, asociacion2);
    asociacion1.agregarPublicacionAdoptante(publicacionAdoptante);

    Assertions.assertEquals(publicacionAdoptante.getAsociacionACargo(), asociacion1);
  }

  @Test
  public void AdoptanteAdoptaMascotaCuyoDuenioTieneUnContacto() {
    PublicacionAdopcion publicacion = publicacionAdopcion(unaListaDeContactos(contacto2), unaListaDeMediosDeContactos(new MailerGmail()));

    Asociacion asociacion = this.asociacion();
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    asociacion.agregarPublicacionAdopcion(publicacion);

    Adoptante adoptante = adoptante();
    adoptante.adoptar(publicacion);

    Mockito.verify(mailerMock).comunicarleA(Mockito.eq(contacto2), anyString(), anyString());
    Assertions.assertTrue(asociacion.getPublicacionesAdopcion().contains(publicacion));
  }

  @Test
  public void AdoptanteAdoptaMascotaCuyoDuenioTieneDosContactos() {
    PublicacionAdopcion publicacion = publicacionAdopcion(unaListaDeContactos(contacto2, contacto3), unaListaDeMediosDeContactos(new MailerGmail(), new MensajeriaTwilio()));

    Asociacion asociacion = this.asociacion();
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    asociacion.agregarPublicacionAdopcion(publicacion);

    Adoptante adoptante = adoptante();
    adoptante.adoptar(publicacion);

    Mockito.verify(mailerMock, times(2)).comunicarleA(any(), anyString(), anyString());
    Mockito.verify(twilioMock, times(2)).comunicarleA(any(), anyString(), anyString());
    Assertions.assertTrue(asociacion.getPublicacionesAdopcion().contains(publicacion));
  }

  // Instanciadores

  public Asociacion asociacion() {
    Ubicacion ubicacion = new Ubicacion(1000, 2000);

    return new Asociacion(ubicacion);
  }

  public PublicacionAdoptante publicacionAdoptante(String preguntaMascota, String preguntaAdoptante, List<String> opciones, String respuesta) {
    Adoptante adoptante = new Adoptante("Angel Martinez", LocalDate.of(1989,8, 15), TipoDocumento.DNI,
        32457938, unaListaDeContactos(contacto1), unaListaDeMediosDeContactos(new MailerGmail()));
    Pregunta preguntaConOpciones = new Pregunta(preguntaMascota, preguntaAdoptante, opciones);
    PreguntaRespondida preguntaRespondida = new PreguntaRespondida(preguntaConOpciones, respuesta);
    List<PreguntaRespondida> preguntasRespondidas = new ArrayList<>();
    preguntasRespondidas.add(preguntaRespondida);

    return new PublicacionAdoptante(adoptante, preguntasRespondidas);
  }

  private Adoptante adoptante() {
    return new Adoptante("Angel Martinez", LocalDate.of(1989,8, 15), TipoDocumento.DNI,
        32457938, unaListaDeContactos(contacto1), unaListaDeMediosDeContactos(new MailerGmail()));
  }

  private Duenio duenio(List<Contacto> contactos, List<MedioDeContacto> mediosDeContacto) {
    return new Duenio("Candela Benitez","1/02/1994" , TipoDocumento.DNI,
        387168305,"candeBenitez94", "387168305Cann&", contactos, mediosDeContacto);
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

  private PublicacionAdopcion publicacionAdopcion(List<Contacto> contactos, List<MedioDeContacto> mediosDeContacto) {
    CaracteristicaMascota caracteristica = new CaracteristicaMascota("Color", "Negro");
    Mascota mascota = mascotaPerroHembra("Blacky", unaListaDeCaracteristicas(caracteristica), duenio(contactos,mediosDeContacto));

    List<String> opciones1 = new ArrayList<>();
    opciones1.add("Perro");
    opciones1.add("Gato");

    Pregunta pregunta1 = new Pregunta("Tipo de Mascota", "Tipo de Mascota", opciones1);
    PreguntaRespondida preguntaRespondida1 = new PreguntaRespondida(pregunta1, "Perro");

    List<String> opciones2 = new ArrayList<>();
    opciones2.add("Si");
    opciones2.add("No");

    Pregunta pregunta2 = new Pregunta("Esta castrada", "Esta castrada", opciones2);
    PreguntaRespondida preguntaRespondida2 = new PreguntaRespondida(pregunta2, "Si");

    List<PreguntaRespondida> listaPreguntasRespondidas = new ArrayList<>();
    listaPreguntasRespondidas.add(preguntaRespondida1);
    listaPreguntasRespondidas.add(preguntaRespondida2);

    return new PublicacionAdopcion(mascota, listaPreguntasRespondidas);

  }

  private Mascota mascotaPerroHembra(String nombre, List<CaracteristicaMascota> caracteristicas, Duenio duenio) {
    return new MascotaBuilder()
        .setTipo(TipoMascota.PERRO)
        .setNombre(nombre)
        .setApodo(nombre)
        .setEdad(10)
        .setSexo(Sexo.HEMBRA)
        .setDescripcion("")
        .setFotos(fotosDeMascota())
        .setCaracteristicas(caracteristicas)
        .setDuenio(duenio)
        .build();
  }

  private List<String> fotosDeMascota() {
    List<String> fotos = new ArrayList<>();
    fotos.add("Foto1");
    fotos.add("Foto2");
    fotos.add("Foto3");
    return fotos;
  }

  private List<CaracteristicaMascota> unaListaDeCaracteristicas(CaracteristicaMascota... caracteristicas) {
    List<CaracteristicaMascota> lista = new ArrayList<>();
    Collections.addAll(lista, caracteristicas);
    return lista;
  }

}