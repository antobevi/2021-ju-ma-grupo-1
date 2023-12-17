package repositorios;

import mascotasEncontradas.MascotaEncontrada;
import mascotasEncontradas.MascotaEncontradaConChapita;
import mascotasEncontradas.PublicacionMascotaEncontrada;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import usuarios.Asociacion;

import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepositorioMascotasEncontradas implements WithGlobalEntityManager  {
  private static RepositorioMascotasEncontradas instance= new RepositorioMascotasEncontradas();
  //private List<MascotaEncontradaConChapita> mascotasEncontradasConChapita;
  //private List<PublicacionMascotaEncontrada> publicaciones; // Publicaciones pendientes de aprobacion

  private RepositorioMascotasEncontradas() {
    //this.mascotasEncontradasConChapita = new ArrayList<>();
    //this.publicaciones = new ArrayList<>();
  }

  public static RepositorioMascotasEncontradas getRepositorioMascotas(){
    return instance;
  }

  public void agregarMascotaEncontrada(MascotaEncontradaConChapita mascotaEncontrada) {
    //Collections.addAll(this.mascotasEncontradasConChapita, mascotasEncontradas);

    entityManager().persist(mascotaEncontrada);

  }

  public void agregarPublicacion(PublicacionMascotaEncontrada publicacion) {
    //Collections.addAll(this.publicaciones, publicaciones);

    entityManager().persist(publicacion);

  }

  public List<MascotaEncontrada> mascotasEncontradasUltimos10Dias() {
    return this.getMascotasEncontradas().stream()
            .filter(mascota -> this.encontradoEnLosUltimos10Dias(mascota)).collect(Collectors.toList());
  }

  public boolean encontradoEnLosUltimos10Dias(MascotaEncontrada mascota) {
    return this.cantidadDeDiasDesdeQueFueEncontrado(mascota) >= 0
            && this.cantidadDeDiasDesdeQueFueEncontrado(mascota) <= 10;
  }

  public long cantidadDeDiasDesdeQueFueEncontrado(MascotaEncontrada mascota) {
    // Con ChronoUnit se obtienen las unidades que se necesita de la fecha. En este caso los días.
    return ChronoUnit.DAYS.between(mascota.getFecha(), LocalDate.now());
  }

  // Te da todas las mascotas, tanto las que tienen chapita como las que no
  public List<MascotaEncontrada> getMascotasEncontradas() {
    List<MascotaEncontrada> mascotas = this.getMascotasEncontradasConChapita().stream()
        .map(mascota -> mascota.getMascotaEncontrada()).collect(Collectors.toList());
    List<MascotaEncontrada> publicacionesMascotas = this.getPublicaciones().stream()
        .map(publicacion -> publicacion.getMascotaEncontrada()).collect(Collectors.toList());

    return Stream.concat(mascotas.stream(), publicacionesMascotas.stream()).collect(Collectors.toList());

  }

  public List<PublicacionMascotaEncontrada> getPublicaciones(){
    return entityManager().createQuery("FROM PublicacionMascotaEncontrada").getResultList();
  }

  public List<MascotaEncontradaConChapita> getMascotasEncontradasConChapita() {
    return entityManager().createQuery("FROM MascotaEncontradaConChapita").getResultList();
  }

  public List<PublicacionMascotaEncontrada> getPublicacionesDeAsociacion(Asociacion asociacion){
    return this.getPublicaciones().stream()
        .filter(publicacion -> RepositorioAsociaciones.getRepositorio().esLaAsociacionMasCercana(publicacion
            .getMascotaEncontrada().getUbicacion(), asociacion))
        .collect(Collectors.toList());
  }

  // Para tests
  public void eliminarMascotasEncontradas(){
    /*
    this.publicaciones = new ArrayList<>();
    this.mascotasEncontradasConChapita = new ArrayList<>(); */
  }

}
