import caracteristicas.Caracteristica;
import caracteristicas.CaracteristicaMascota;
import mapa.Ubicacion;
import mascotas.*;
import mascotas.mascotaExceptions.FaltaSubirUnaFoto;
import mascotasEncontradas.MascotaEncontrada;
import mascotasEncontradas.MascotaEncontradaConChapita;
import mascotasEncontradas.PublicacionMascotaEncontrada;
import mediosDeContacto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repositorios.RepositorioMascotasEncontradas;
import rescatistas.Rescatista;
import rescatistas.rescatistaExceptions.RescatistaDebeTenerAlMenosUnContacto;
import usuarios.*;
import usuarios.usuarioExceptions.NoPerteneceAAsociacionException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class MascotasEncontradasTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  private Contacto contacto1 = new Contacto("Marcela", "Barreiro", "20659414", "marcelabar@gmai.com");
  private Contacto contacto2 = new Contacto("Daniel", "Perez", "1145678923", "danielperez45@gmail.com");

  private Duenio duenioFirulais = new Duenio("Jazmin", "01/06/1998", TipoDocumento.DNI,
      41234567, "Jaz", "41234567Jaz+", unaListaDeContactos(contacto1), unaListaDeMediosDeContactos(new MailerGmail()));
  private Duenio duenioBlanca = new Duenio("Maria", "12/09/1998", TipoDocumento.DNI,
      40156876, "Maru", "12091998Mar+", unaListaDeContactos(contacto1, contacto2), unaListaDeMediosDeContactos(new MailerGmail(), new MensajeriaTwilio()));

  private CaracteristicaMascota caracteristica1 = new CaracteristicaMascota("Color", "Negro");
  private CaracteristicaMascota caracteristica2 = new CaracteristicaMascota("Color", "Blanca");
  private List<CaracteristicaMascota> caracteristicasPelajeNegro = unaListaDeCaracteristicas(caracteristica1);
  private List<CaracteristicaMascota> caracteristicasPelajeBlanco = unaListaDeCaracteristicas(caracteristica2);
  private Mascota firu = mascotaPerroMacho("Firulais", caracteristicasPelajeNegro, duenioFirulais);
  private Mascota blanquita = mascotaPerroHembra("Blanca", caracteristicasPelajeBlanco, duenioBlanca);
  private MascotaEncontradaConChapita firulais = mascotaEncontradaConChapitaSetUbicacionFotosRescatista(LocalDate.now(), firu);
  private MascotaEncontradaConChapita blanca = mascotaEncontradaConChapitaSetUbicacionFotosRescatista(LocalDate.of(2020, 3, 29), blanquita);
  private PublicacionMascotaEncontrada fachita = mascotaEncontradaSinChapitaSetUbicacionFotosRescatista(LocalDate.of(2021, 5, 3));

  private Asociacion asociacion = new Asociacion(new Ubicacion(4500, 2000));
  private Asociacion asociacion2 = new Asociacion(new Ubicacion(12,12));
  private Voluntario voluntario = new Voluntario("pepe","1965/12/12",TipoDocumento.DNI,235468754,"pepe123","Yagni3210+", asociacion);
  private Voluntario voluntario2 = new Voluntario("Juan","1969/08/25",TipoDocumento.DNI,165975215,"juan12","Yagni251+",asociacion2);

  private MailerGmail mailerMock;
  private MensajeriaTwilio twilioMock;

  public MascotasEncontradasTest() throws NoSuchAlgorithmException {
    //
  }

  @BeforeEach
  public void init() {
    mailerMock = Mockito.mock(MailerGmail.class);
    twilioMock = Mockito.mock(MensajeriaTwilio.class);

    ServiceLocator.getServiceLocator().registrar(MailerGmail.class, mailerMock);
    ServiceLocator.getServiceLocator().registrar(MensajeriaTwilio.class, twilioMock);
  }

  @AfterEach
  public void limpiarServicios() {
    RepositorioMascotasEncontradas.getRepositorioMascotas().eliminarMascotasEncontradas();
    ServiceLocator.getServiceLocator().eliminarServicios();
  }

  @Test
  public void lasMascotasEncontradasEnLosUltimos10DiasEsSolo1() throws NoSuchAlgorithmException {

    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(blanca);
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(firulais);
    assertEquals(1, RepositorioMascotasEncontradas.getRepositorioMascotas().mascotasEncontradasUltimos10Dias().size());
  }

  @Test
  public void periodoDeDiasEntreElDiaDeHoyYElDiaEnQueFueEncontradaLaMascota() throws NoSuchAlgorithmException {
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(blanca);
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(firulais);

    assertEquals(RepositorioMascotasEncontradas.getRepositorioMascotas().cantidadDeDiasDesdeQueFueEncontrado(firulais.getMascotaEncontrada()), 0);
    assertEquals(RepositorioMascotasEncontradas.getRepositorioMascotas().cantidadDeDiasDesdeQueFueEncontrado(blanca.getMascotaEncontrada()),
        ChronoUnit.DAYS.between(blanca.getMascotaEncontrada().getFecha(), LocalDate.now()));
  }

  @Test
  public void rescatarMascotaAgregandoseAMascotasEncontradas() {
    int cantidadDemascotasAntesDeEncontrarUna = RepositorioMascotasEncontradas.getRepositorioMascotas().getMascotasEncontradas().size();
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(firulais);

    assertTrue(RepositorioMascotasEncontradas.getRepositorioMascotas().getMascotasEncontradas().contains(firulais.getMascotaEncontrada()));
    assertEquals(RepositorioMascotasEncontradas.getRepositorioMascotas().getMascotasEncontradas().size(), cantidadDemascotasAntesDeEncontrarUna + 1);
  }

  @Test
  public void ubicacionDeMascotasEncontradasEsLaMismaDespuesDeAgregarlasALaLista() {
    Ubicacion ubicacionBase = new Ubicacion(firulais.getMascotaEncontrada().getUbicacion().getLatitud(), firulais.getMascotaEncontrada().getUbicacion().getLongitud());
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(firulais);
    //
    MascotaEncontrada mascota = RepositorioMascotasEncontradas.getRepositorioMascotas().getMascotasEncontradas().get(0);
    //
    assertEquals(ubicacionBase.getLatitud(), mascota.getUbicacion().getLatitud());
    assertEquals(ubicacionBase.getLongitud(), mascota.getUbicacion().getLongitud());
  }

  @Test
  public void rescatistaDebeTenerAlMenosUnContacto() {
    assertThrows(RescatistaDebeTenerAlMenosUnContacto.class,
        () -> {
          Rescatista rescatista = new Rescatista("Juan", "Perez", TipoDocumento.DNI, 30598784, Collections.emptyList());
        });
  }

  @Test
  public void mascotaEncontradaDebeTenerAlMenosUnaFoto() {
    Rescatista rescatista = rescatista();

    assertThrows(FaltaSubirUnaFoto.class,
        () -> {
          Ubicacion ubicacion = new Ubicacion(4500, 2000);
          new MascotaEncontradaConChapita(new MascotaEncontrada(null, "-", ubicacion,
              LocalDate.now(), rescatista), firu);
        });
  }

  @Test
  public void aprobarPublicacionMascotaEncontrada() throws NoSuchAlgorithmException {
    fachita.informar();
    voluntario.aprobarSolicitud(fachita);
    assertTrue(fachita.estaAprobado());
  }

  @Test
  public void eliminarPublicacionMascotaEncontrada() throws NoSuchAlgorithmException {
    fachita.informar();
    voluntario.rechazarSolicitud(fachita);
    assertFalse(fachita.estaAprobado());
  }

  @Test
  public void NoPerteneceAAsociacion() throws NoSuchAlgorithmException {
    assertThrows(NoPerteneceAAsociacionException.class,() -> voluntario2.aprobarSolicitud(fachita));
  }

  @Test
  public void enviarMailAUnContactoPorMascotaEncontradaConDuenio() {
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(firulais);

    firulais.avisarDuenio();

    Mockito.verify(mailerMock, Mockito.only()).comunicarleA(any(), anyString(), anyString());
  }

  @Test
  public void enviarSMSYMailAContactosPorMascotaEncontradaConDuenio(){
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarMascotaEncontrada(blanca);
    blanca.avisarDuenio();

    Mockito.verify(mailerMock, Mockito.times(2)).comunicarleA(any(), anyString(), anyString());
    Mockito.verify(twilioMock, Mockito.times(2)).comunicarleA(any(), anyString(), anyString());
    assertEquals(1, RepositorioMascotasEncontradas.getRepositorioMascotas().getMascotasEncontradas().size());
  }

  @Test
  public void duenioReclamaUnaMascotaVistaEnPublicacionesYSeEnviaMailAlRescatista(){
    PublicacionMascotaEncontrada publicacion = mascotaEncontradaSinChapitaSetUbicacionFotosRescatista(LocalDate.now());
    publicacion.getMascotaEncontrada().setRescatista(rescatista());
    Duenio duenio = duenioBlanca;

    //
    publicacion.reclamarMascota(duenio);
    //
    Mockito.verify(mailerMock, Mockito.only()).enviarMail(any(Mail.class));
  }

  @Test
  public void duenioReclamaUnaMascotaVistaEnPublicacionesYSeEnviaMailATodosLosContactosDelRescatista(){
    PublicacionMascotaEncontrada publicacion = mascotaEncontradaSinChapitaSetUbicacionFotosRescatista(LocalDate.now());
    Rescatista rescatista = rescatista();
    rescatista.getContactos().add(new Contacto("Julia", "Perez", "1123456789", "juliaPerez2@gmail.com"));
    publicacion.getMascotaEncontrada().setRescatista(rescatista);
    Duenio duenio = duenioBlanca;

    //
    publicacion.reclamarMascota(duenio);
    //
    Mockito.verify(mailerMock, Mockito.times(2)).enviarMail(any(Mail.class));
  }



  //Instanciadores
  private Voluntario voluntario() throws NoSuchAlgorithmException {
    return new Voluntario("Jose", "04/05/1998", TipoDocumento.DNI,
        41138432, "Pepe", "123456Pepe+", asociacion);
  }

  private List<CaracteristicaMascota> unaListaDeCaracteristicas(CaracteristicaMascota... caracteristicas) {
    List<CaracteristicaMascota> lista = new ArrayList<>();
    Collections.addAll(lista, caracteristicas);
    return lista;
  }

  private MascotaEncontradaConChapita mascotaEncontradaConChapitaSetUbicacionFotosRescatista(LocalDate fechaEncontrada, Mascota mascota) {
    Ubicacion ubicacion = new Ubicacion(4500, 2000);
    Rescatista rescatista = rescatista();
    MascotaEncontrada mascotaEncontrada = new MascotaEncontrada(fotosDeMascota(), "-", ubicacion, fechaEncontrada, rescatista);

    return new MascotaEncontradaConChapita(mascotaEncontrada, mascota);
  }

  private PublicacionMascotaEncontrada mascotaEncontradaSinChapitaSetUbicacionFotosRescatista(LocalDate fechaEncontrada) {
    Ubicacion ubicacion = new Ubicacion(4500, 2000);
    Rescatista rescatista = rescatista();
    MascotaEncontrada mascotaEncontrada = new MascotaEncontrada(fotosDeMascota(), "-", ubicacion, fechaEncontrada, rescatista);

    return new PublicacionMascotaEncontrada(mascotaEncontrada, false);
  }

  private List<String> fotosDeMascota() {
    List<String> fotos = new ArrayList<>();
    fotos.add("Foto1");
    fotos.add("Foto2");
    return fotos;
  }

  private Mascota mascotaPerroMacho(String nombre, List<CaracteristicaMascota> caracteristicas, Duenio duenio) {
    return new MascotaBuilder()
        .perroConNombre(nombre)
        .setCaracteristicas(caracteristicas)
        .setFotos(fotosDeMascota())
        .setDuenio(duenio)
        .build();
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

  private Rescatista rescatista() {
    Contacto contacto = new Contacto("Julia", "Perez", "1123456789", "juliaPerez@gmai.com");
    List<Contacto> contactos = new ArrayList<>();
    contactos.add(contacto);
    return new Rescatista("Juan", "Perez", TipoDocumento.DNI, 30598784, contactos);
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
