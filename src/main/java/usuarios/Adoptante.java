package usuarios;

import mascotas.Mascota;
import mediosDeContacto.MedioDeContacto;
import mediosDeContacto.TareaEnviarMensajes;
import publicaciones.PublicacionAdopcion;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "adoptantes")
public class Adoptante {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "fechaNacimiento", columnDefinition = "DATE")
  private LocalDate fechaNacimiento;

  @Column(name = "tipoDocumento")
  @Enumerated(value = EnumType.STRING)
  private TipoDocumento tipoDocumento;

  @Column(name = "numeroDocumento")
  private int numeroDocumento;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "adoptante_id")
  private List<Contacto> contactos;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "adoptante_id")
  private List<MedioDeContacto> mediosDeContactoPreferidos;

  @Transient
  private List<PublicacionAdopcion> publicacionesRecomendadas;

  public Adoptante(String nombre, LocalDate fechaNacimiento, TipoDocumento tipoDocumento, int numeroDocumento, List<Contacto> contactos, List<MedioDeContacto> mediosDeContactoPreferidos) {
    this.nombre = nombre;
    this.fechaNacimiento = fechaNacimiento;
    this.tipoDocumento = tipoDocumento;
    this.numeroDocumento = numeroDocumento;
    this.contactos = contactos;
    this.mediosDeContactoPreferidos = mediosDeContactoPreferidos;
  }

  public Long getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public void adoptar(PublicacionAdopcion publicacion) {
    Mascota mascota = publicacion.getMascota();
    Duenio duenio = publicacion.getMascota().getDuenio();
    List<MedioDeContacto> mediosDeContacto = duenio.getMediosDeContactoPreferidos();
    List<Contacto> contactos = duenio.getContactos();
    String asunto = "Quieren adoptar tu mascota " + mascota.getNombre();
    String mensaje = this.getNombre() + " quiere adoptar tu mascota, comunicate con alguno de estos contactos para concretar un encuentro: " + this.obtenerMailsComoString();

    TareaEnviarMensajes tareaEnviarMensajes = new TareaEnviarMensajes();
    tareaEnviarMensajes.enviarMensajesPorVariosMediosA(contactos, mediosDeContacto, asunto, mensaje);

  }

  public void recibirRecomendaciones(List<PublicacionAdopcion> publicacionesRecomendadas) {
    this.publicacionesRecomendadas = publicacionesRecomendadas;
    if (!this.publicacionesRecomendadas.isEmpty()) {
      this.enviarMailConRecomendaciones(publicacionesRecomendadas);
    }
  }

  public List<PublicacionAdopcion> getPublicacionesRecomendadas() {
    return publicacionesRecomendadas;
  }

  private void enviarMailConRecomendaciones(List<PublicacionAdopcion> publicacionesRecomendadas) {
    String mensaje = this.generarMensajeDeAdopcion(publicacionesRecomendadas);

    TareaEnviarMensajes tareaEnviarMensajes = new TareaEnviarMensajes();
    //TODO: enviar a todos los medios. ojo pueden ser mensajes distintos segun el medio
    // En medio de contacto, tener metodos que sepan crear la notificacion (ej: notificarAdopcion(datos))
    tareaEnviarMensajes.enviarMailATodosLosContactos(this.getContactos(),"Recomendacion de Adopcion!", mensaje);
  }

  private String generarMensajeDeAdopcion(List<PublicacionAdopcion> publicacionesRecomendadas) {
    String mensaje = "Hola! Tenemos estas mascotas para recomendarte: \n";
    mensaje += publicacionesRecomendadas.stream()
        .map(publicacion -> "-> " + publicacion.obtenerDescripcion() + " Contacto: " + publicacion.getMascota().getDuenio().obtenerMailsComoString() + "\n")
        .collect(Collectors.joining());
    return mensaje;
  }

  public void setContactos(List<Contacto> contactos) {
    this.contactos = contactos;
  }

  public List<Contacto> getContactos() {
    return contactos;
  }

  public String obtenerMailsComoString() {
    return getContactos().stream().map(Contacto::getEmail).collect(Collectors.joining(", "));
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public int getNumeroDocumento() {
    return numeroDocumento;
  }

  public List<MedioDeContacto> getMediosDeContactoPreferidos() {
    return mediosDeContactoPreferidos;
  }

}