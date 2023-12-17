import adopciones.preguntas.Pregunta;
import adopciones.preguntas.PreguntaRespondida;
import caracteristicas.CaracteristicaMascota;
import mascotas.Mascota;
import mascotas.MascotaBuilder;
import mascotas.Sexo;
import mascotas.TipoMascota;
import mediosDeContacto.MailerGmail;
import mediosDeContacto.MedioDeContacto;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import publicaciones.PublicacionAdopcion;
import publicaciones.PublicacionAdoptante;
import repositorios.RepositorioPublicaciones;
import usuarios.Adoptante;
import usuarios.Contacto;
import usuarios.Duenio;
import usuarios.TipoDocumento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Crear AbstractPersistance.. porque no va a andar > buscar en el foro
public class RepositorioPublicacionesTest extends AbstractPersistenceTest implements WithGlobalEntityManager {


  @Test
  public void persistirUnaPublicacionDeAdopcion() {
    PublicacionAdopcion publicacionAdopcion = this.publicacionAdopcion(
        this.unaListaDeContactos(new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com")),
        this.unaListaDeMediosDeContactos());

    RepositorioPublicaciones repositorioPublicaciones = RepositorioPublicaciones.getInstance();

    repositorioPublicaciones.agregarPublicacionDeAdopcion(publicacionAdopcion);
    List<PublicacionAdopcion> publicaciones = repositorioPublicaciones.getPublicacionesDeAdopcion();

    Assertions.assertEquals(1, publicaciones.size());
  }

  @Test
  public void persistirUnaPublicacionDeAdoptante() {
    List<String> opciones = new ArrayList<>();
    opciones.add("Si");
    opciones.add("No");
    PublicacionAdoptante publicacionAdoptante = this.publicacionAdoptante("Tamaño chico?",
        "Tiene patio?", opciones, "Si");
    RepositorioPublicaciones repositorioPublicaciones = RepositorioPublicaciones.getInstance();

    repositorioPublicaciones.agregarPublicacionDeAdoptante(publicacionAdoptante);
    List<PublicacionAdoptante> publicaciones = repositorioPublicaciones.getPublicacionesDeAdoptantes();

    Assertions.assertEquals(1, publicaciones.size());
  }

  // ------------------------------------------------- Instanciadores -------------------------------------------------

  public PublicacionAdoptante publicacionAdoptante(String preguntaMascota, String preguntaAdoptante, List<String> opciones, String respuesta) {
    Contacto contacto = new Contacto("Ricardo", "Rodriguez", "1156314975", "rickRodriguez98@gmail.com");
    Adoptante adoptante = new Adoptante("Angel Martinez", LocalDate.of(1989,8, 15), TipoDocumento.DNI,
        32457938, unaListaDeContactos(contacto), unaListaDeMediosDeContactos(new MailerGmail()));
    Pregunta preguntaConOpciones = new Pregunta(preguntaMascota, preguntaAdoptante, opciones);
    PreguntaRespondida preguntaRespondida = new PreguntaRespondida(preguntaConOpciones, respuesta);
    List<PreguntaRespondida> preguntasRespondidas = new ArrayList<>();
    preguntasRespondidas.add(preguntaRespondida);

    return new PublicacionAdoptante(adoptante, preguntasRespondidas);
  }

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

  private Duenio duenio(List<Contacto> contactos, List<MedioDeContacto> mediosDeContacto) {
    return new Duenio("Candela Benitez","1/02/1994" , TipoDocumento.DNI,
        387168305,"candeBenitez94", "387168305Cann&", contactos, mediosDeContacto);
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
