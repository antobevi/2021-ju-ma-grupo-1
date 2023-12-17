package mascotasEncontradas;

import mediosDeContacto.TareaEnviarMensajes;
import repositorios.RepositorioMascotasEncontradas;
import usuarios.Duenio;

import javax.persistence.*;

@Entity
@Table(name = "publicacionesmascotasencontradas")
public class PublicacionMascotaEncontrada {
  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private MascotaEncontrada mascotaEncontrada; // Para el constructor

  @Column(name = "estaAprobado")
  private Boolean estaAprobado;

  public PublicacionMascotaEncontrada(MascotaEncontrada mascotaEncontrada, boolean estaAprobado) {
    this.mascotaEncontrada = mascotaEncontrada;
    this.estaAprobado = estaAprobado;
  }

  public void aprobarPublicacion() {
    estaAprobado = true;
  }

  public void rechazarPublicacion() {
    estaAprobado = false;
  }

  public Boolean estaAprobado() {
    return estaAprobado;
  }

  // TODO: Cambiar nombre?
  public void informar() {
    RepositorioMascotasEncontradas.getRepositorioMascotas().agregarPublicacion(this);
  }

  public void reclamarMascota(Duenio duenio){
    this.notificarRescatistaMascotaFueEncontrada(duenio);
  }

  private void notificarRescatistaMascotaFueEncontrada(Duenio duenio) {
    TareaEnviarMensajes tareaEnviarMensajes = new TareaEnviarMensajes();
    tareaEnviarMensajes.enviarMailATodosLosContactos(mascotaEncontrada.getRescatista().getContactos(),
        "Mascota reclamada!","Coordina con el dueño de la mascota para entregarle su mascota! " +
            "Mail del dueño: " + duenio.obtenerMailsComoString());
  }

  public MascotaEncontrada getMascotaEncontrada() {
    return mascotaEncontrada;
  }

}
