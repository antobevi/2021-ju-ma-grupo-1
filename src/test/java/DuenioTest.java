import adopciones.preguntas.Pregunta;
import caracteristicas.Caracteristica;
import caracteristicas.CaracteristicaMascota;
import caracteristicas.Colores;
import mapa.Ubicacion;
import mascotas.Mascota;
import mascotas.MascotaBuilder;
import mascotas.Sexo;
import mascotas.TipoMascota;
import org.junit.jupiter.api.Test;
import publicaciones.PublicacionAdopcion;
import repositorios.RepositorioMascotas;
import usuarios.Asociacion;
import usuarios.Contacto;
import usuarios.Duenio;
import usuarios.TipoDocumento;
import usuarios.usuarioExceptions.DuenioDebeTenerUnContactoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DuenioTest {

  @Test
  public void elDuenioTieneUnaMascotaDespuesDeAgregarsela() {
    Duenio duenio = unDuenio();
    CaracteristicaMascota color = new CaracteristicaMascota("Color", Colores.MARRON.toString());
    CaracteristicaMascota castrado = new CaracteristicaMascota("Castrado", Boolean.FALSE.toString());
    List<CaracteristicaMascota> caracteristicas = unaListaDeCaracteristicas(color, castrado);
    Mascota mascota = new MascotaBuilder().perroConNombreYDuenio("Yago", duenio).setCaracteristicas(caracteristicas).build();
    //
    duenio.agregarMascota(mascota);
    //
    assertEquals(1, duenio.getMascotas().size());
    assertEquals(mascota, duenio.getMascotas().get(0));
  }

  @Test
  public void elDuenioTieneDosMascotasDespuesDeAgregarselas() {
    Duenio duenio = unDuenio();
    CaracteristicaMascota color1 = new CaracteristicaMascota("Color", Colores.MARRON.toString());
    CaracteristicaMascota castrado = new CaracteristicaMascota("Castrado", Boolean.FALSE.toString());
    List<CaracteristicaMascota> caracteristicas1 = unaListaDeCaracteristicas(color1, castrado);
    Mascota mascota1 = new MascotaBuilder().perroConNombreYDuenio("Yago", duenio).setCaracteristicas(caracteristicas1).build();
    CaracteristicaMascota color2 = new CaracteristicaMascota("Color", Colores.NEGRO.toString());
    List<CaracteristicaMascota> caracteristicas2 = unaListaDeCaracteristicas(color2, castrado);
    Mascota mascota2 = new MascotaBuilder().perroConNombreYDuenio("Rocky", duenio).setCaracteristicas(caracteristicas2).build();
    //
    duenio.agregarMascota(mascota1);
    duenio.agregarMascota(mascota2);
    //
    assertEquals(2, duenio.getMascotas().size());
    assertEquals(mascota1, duenio.getMascotas().get(0));
    assertEquals(mascota2, duenio.getMascotas().get(1));
  }

  @Test
  public void elDuenioReportoUnaMascotaPerdidaYFiguraComoPerdida() {
    Duenio duenio = unDuenio();
    CaracteristicaMascota color1 = new CaracteristicaMascota("Color", Colores.MARRON.toString());
    CaracteristicaMascota castrado = new CaracteristicaMascota("Castrado", Boolean.FALSE.toString());
    List<CaracteristicaMascota> caracteristicas1 = unaListaDeCaracteristicas(color1, castrado);
    CaracteristicaMascota color2 = new CaracteristicaMascota("Color", Colores.NEGRO.toString());
    List<CaracteristicaMascota> caracteristicas2 = unaListaDeCaracteristicas(color2, castrado);
    CaracteristicaMascota color3 = new CaracteristicaMascota("Color", Colores.BLANCO.toString());
    List<CaracteristicaMascota> caracteristicas3 = unaListaDeCaracteristicas(color3);

    Mascota mascota1 = new MascotaBuilder().perroConNombreYDuenio("Yago", duenio).setCaracteristicas(caracteristicas1).build();
    Mascota mascota2 = new MascotaBuilder().perroConNombreYDuenio("Rocky", duenio).setCaracteristicas(caracteristicas2).build();
    Mascota mascota3 = new MascotaBuilder()
        .setTipo(TipoMascota.GATO)
        .setNombre("Michi")
        .setApodo("Michi")
        .setEdad(2)
        .setSexo(Sexo.HEMBRA)
        .setDescripcion("Gatita")
        .setFotos(new ArrayList<>())
        .setCaracteristicas(caracteristicas3)
        .setDuenio(duenio)
        .build();

    duenio.agregarMascota(mascota1);
    duenio.agregarMascota(mascota2);
    duenio.agregarMascota(mascota3);

    duenio.avisarQueSePerdioUnaMascota(mascota3); //Se perdio el gato
    RepositorioMascotas.getInstance().actualizarMascota(mascota3);

    assertEquals(RepositorioMascotas.getInstance().getMascotasPerdidas().size(), 1);
  }

  @Test
  public void unDuenioPuedePonerParaAdopcionUnaMascota() {
    Duenio duenio = unDuenio();
    CaracteristicaMascota color1 = new CaracteristicaMascota("Color", Colores.MARRON.toString());
    CaracteristicaMascota castrado = new CaracteristicaMascota("Castrado", Boolean.FALSE.toString());
    List<CaracteristicaMascota> caracteristicas1 = unaListaDeCaracteristicas(color1, castrado);
    Mascota mascota1 = new MascotaBuilder().perroConNombreYDuenio("Yago", duenio).setCaracteristicas(caracteristicas1).build();
    duenio.agregarMascota(mascota1);
    Asociacion asociacion = new Asociacion(new Ubicacion(-31.38476649512739, -64.22706150177592));
    List<String> respuestas = new ArrayList<>();
    Pregunta pregunta = unaPregunta("Necesita patio?", "Tiene patio?", "Si", "No");
    respuestas.add("Si");
    duenio.darMascotaEnAdopcion(mascota1, asociacion, respuestas);
    List<PublicacionAdopcion> publicaciones = asociacion.getPublicacionesAdopcion();
    assertEquals(1, publicaciones.size());
  }

  @Test
  public void elDuenioTieneDosContactosDespuesDeAgregarselos() {
    Duenio duenio = unDuenio();
    Contacto contacto = new Contacto("Pedro", "Smith", "1112345678", "pedrosmith@gmail.com");
    //
    duenio.agregarContacto(contacto);
    //
    assertEquals(2, duenio.getContactos().size());
    assertEquals("Pedro", duenio.getContactos().get(1).getNombre());
    assertEquals("Smith", duenio.getContactos().get(1).getApellido());
    assertEquals("1112345678", duenio.getContactos().get(1).getTelefono());
    assertEquals("pedrosmith@gmail.com", duenio.getContactos().get(1).getEmail());
  }

  @Test
  public void elDuenioDebeTenerAlMenosUnContacto() {
    assertThrows(DuenioDebeTenerUnContactoException.class,
        () -> {
          Duenio duenio = new Duenio("Pedro", "2021/05/03", TipoDocumento.DNI, 11111111, "username", "Mypass123$", Collections.emptyList(),new ArrayList<>());
        });
  }

  private Duenio unDuenio() {
    Contacto contacto = new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com");
    List<Contacto> contactos = new ArrayList<>();
    contactos.add(contacto);
    return new Duenio("Pedro", "3/5/2021", TipoDocumento.DNI, 11111111, "username", "Mypass123$", contactos, new ArrayList<>());

  }

  private List<CaracteristicaMascota> unaListaDeCaracteristicas(CaracteristicaMascota... caracteristicas) {
    List<CaracteristicaMascota> lista = new ArrayList<>();
    Collections.addAll(lista, caracteristicas);
    return lista;
  }

  private Pregunta unaPregunta(String preguntaMascota, String preguntaAdoptante, String... opciones) {
    return new Pregunta(preguntaMascota, preguntaAdoptante, unasOpciones(opciones));
  }

  private List<String> unasOpciones(String... opciones) {
    return Arrays.asList(opciones);
  }
}