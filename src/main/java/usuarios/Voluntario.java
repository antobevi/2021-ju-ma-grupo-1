package usuarios;

import mascotas.Mascota;
import mascotasEncontradas.MascotaEncontrada;
import mascotasEncontradas.PublicacionMascotaEncontrada;
import repositorios.RepositorioMascotasEncontradas;
import usuarios.usuarioExceptions.NoPerteneceAAsociacionException;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "voluntarios")
public class Voluntario {

  @Id
  @GeneratedValue
  private Long id;

  private String nombre;
  private Date fechaNacimiento;
  private int numeroDocumento;
  private String username;
  private String password;

  @Enumerated(value = EnumType.STRING)
  private TipoDocumento tipoDocumento;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Asociacion asociacion;

  public Voluntario(String nombre, String fechaNacimiento, TipoDocumento tipoDocumento,
                    int numeroDocumento, String username, String password, Asociacion asociacion) {
    this.nombre = nombre;
    this.fechaNacimiento = new Date(fechaNacimiento);
    this.tipoDocumento = tipoDocumento;
    this.numeroDocumento = numeroDocumento;
    this.username = username;
    this.password = password;
    ValidadorContrasenias.getValidadorContrasenias().validarPassword(password);
    this.asociacion = asociacion;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {

    return password;

  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento.toString();
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = new Date(fechaNacimiento);
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(TipoDocumento tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public int getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(int numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public Long getId() {
    return id;
  }

  public Voluntario(){}

  public void avisarleAlDuenio() {

  }

  public Optional<Mascota> mascotaDelDuenioPerdida(Duenio duenio, MascotaEncontrada mascotaARegresar) {
    return duenio.getMascotas().stream()
        .filter(mascota -> mascota.getDescripcion().equals(mascotaARegresar.getDescripcionEstado()))
        .findFirst();
    //TODO: AHORA QUE NO HAY CHAPITA, ENCONTRAR OTRA FORMA DE COMPARAR UNA MASCOTA CON MASCOTAENCONTRADA (que no sea Descripcion)
  }

  public List<MascotaEncontrada> getMascotasEncontradas() {
    return RepositorioMascotasEncontradas.getRepositorioMascotas().getMascotasEncontradas();
  }

  // trabajo extra (contemplar cambiarlo)
  public void aprobarSolicitud(PublicacionMascotaEncontrada mascota) {

    if (publicacionPerteneceAAsociacion(mascota)) {
      mascota.aprobarPublicacion();
    } else {
      throw new NoPerteneceAAsociacionException("la publicacion de mascota no pertenece a la asociacion del voluntario");
    }


  }

  public void rechazarSolicitud(PublicacionMascotaEncontrada mascota) {

    if (publicacionPerteneceAAsociacion(mascota)) {
      mascota.rechazarPublicacion();
    } else {
      throw new NoPerteneceAAsociacionException("la publicacion de mascota no pertenece a la asociacion del voluntario");
    }

  }

  private Boolean publicacionPerteneceAAsociacion(PublicacionMascotaEncontrada publicacion) {
    return RepositorioMascotasEncontradas.getRepositorioMascotas().getPublicacionesDeAsociacion(this.asociacion).contains(publicacion);
  }

}
