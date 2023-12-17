import caracteristicas.CaracteristicaMascota;
import mapa.Ubicacion;
import mascotas.Mascota;
import mascotas.MascotaBuilder;
import mascotas.Sexo;
import mascotas.TipoMascota;
import mascotasEncontradas.MascotaEncontrada;
import mascotasEncontradas.MascotaEncontradaConChapita;
import mascotasEncontradas.PublicacionMascotaEncontrada;
import mediosDeContacto.MailerGmail;
import mediosDeContacto.MedioDeContacto;
import mediosDeContacto.MensajeriaTwilio;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repositorios.RepositorioMascotasEncontradas;
import rescatistas.Rescatista;
import usuarios.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RepoMascotasEncontradasTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
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


  @Test
  public void persistirMascotaEncontradaConChapitaYRecuperar() {
    RepositorioMascotasEncontradas repo = RepositorioMascotasEncontradas.getRepositorioMascotas();

    repo.agregarMascotaEncontrada(firulais);
    repo.agregarMascotaEncontrada(blanca);

    assertTrue(repo.getMascotasEncontradasConChapita().contains(firulais));
    assertTrue(repo.getMascotasEncontradasConChapita().contains(blanca));
  }

  @Test
  public void persistirPubliMascotaEncontradaYRecuperar() {
    RepositorioMascotasEncontradas repo = RepositorioMascotasEncontradas.getRepositorioMascotas();

    repo.agregarPublicacion(fachita);

    assertTrue(repo.getPublicaciones().contains(fachita));

  }

  //Instanciadores


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
