package usuarios;

import adopciones.preguntas.Pregunta;
import adopciones.preguntas.PreguntaRespondida;
import mascotas.Mascota;
import mascotasEncontradas.PublicacionMascotaEncontrada;
import mediosDeContacto.MedioDeContacto;
import publicaciones.PublicacionAdopcion;
import repositorios.RepositorioMascotas;
import repositorios.RepositorioMascotasEncontradas;
import usuarios.usuarioExceptions.DuenioDebeTenerUnContactoException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "duenios")
public class Duenio {

  @Id
  @GeneratedValue
  private Long id;

  private String nombre;
  private Date fechaNacimiento;

  @Enumerated(value = EnumType.STRING)
  private TipoDocumento tipoDocumento;

  private int numeroDocumento;
  private String username;
  private String password;

  @OneToMany(mappedBy = "duenio", fetch = FetchType.EAGER)
  private List<Mascota> mascotas;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // OneToMany? pensar si se reutilizan
  @JoinColumn(name = "duenio_id")
  private List<Contacto> contactos;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "duenio_id")
  private List<MedioDeContacto> mediosDeContactoPreferidos;


  public Duenio(String nombre, String fechaNacimiento, TipoDocumento tipoDocumento, int numeroDocumento, String username, String password, List<Contacto> contactos,
                List<MedioDeContacto> mediosDeContactoPreferidos) {
    this.nombre = nombre;
    this.fechaNacimiento = new Date(fechaNacimiento);
    this.tipoDocumento = tipoDocumento;
    this.numeroDocumento = numeroDocumento;
    this.username = username;
    this.password = password;
    ValidadorContrasenias.getValidadorContrasenias().validarPassword(password);
    this.mascotas = new ArrayList<>();
    if (contactos == null || contactos.isEmpty()) {
      throw new DuenioDebeTenerUnContactoException("El dueño debe tener al menos un contacto.");
    }
    this.contactos = contactos;
    this.mediosDeContactoPreferidos = mediosDeContactoPreferidos;
  }

  public Duenio() {}

  public Long getId() {
    return id;
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

  public List<MedioDeContacto> getMediosDeContactoPreferidos() {
    return mediosDeContactoPreferidos;
  }

  public void agregarMascota(Mascota mascota) {
    this.mascotas.add(mascota);
    RepositorioMascotas.getInstance().agregarMascota(mascota);
    //Cuando agrega una mascota lo agrega tambien a la lista de las mascotas del sistema
  }

  public void agregarContacto(Contacto contacto) {
    this.contactos.add(contacto);
  }

  public List<Mascota> getMascotas() {
    return mascotas;
  }

  public List<Contacto> getContactos() {
    return contactos;
  }

  public List<PublicacionMascotaEncontrada> buscarUnaMascota() {
    return RepositorioMascotasEncontradas.getRepositorioMascotas().getPublicaciones();
  }

  public void avisarQueSePerdioUnaMascota(Mascota unaMascota) {
    unaMascota.cambiarEstadoAPerdida();
  }

  public String obtenerMailsComoString(){
    return getContactos().stream().map(Contacto::getEmail).collect(Collectors.joining(", "));
  }

  // TODO: Hacer en objeto separado porque no se usa dueño (CreadorDePublicaciones)
  // podria ser clase con metodo static. no singleton.
  // en un futuro podria estar en ui
  // Que solo genere la publicacion. Agregar a asociacion se hace de afuera en el test por ejemplo.
  public void darMascotaEnAdopcion(Mascota unaMascota, Asociacion asociacion, List<String> listaRespuestas){
    List<Pregunta> preguntasAlDuenio = asociacion.getPreguntasAHacer();
    PreguntaRespondida respuesta;
    List<PreguntaRespondida> respuestasAPreguntas = new ArrayList<PreguntaRespondida>();

    for(int i = 0; i < preguntasAlDuenio.size(); i++){
      for(int j = 0; j < listaRespuestas.size(); j++){
        respuesta = preguntasAlDuenio.get(i).responder(listaRespuestas.get(j));
        respuestasAPreguntas.add(respuesta);
      }
    }
    asociacion.agregarPublicacionAdopcion(new PublicacionAdopcion(unaMascota, respuestasAPreguntas));
  }

}
