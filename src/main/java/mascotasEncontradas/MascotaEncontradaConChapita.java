package mascotasEncontradas;

import mascotas.Mascota;
import mediosDeContacto.*;
import usuarios.Contacto;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "mascotasencontradasconchapita")
public class MascotaEncontradaConChapita {
  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private MascotaEncontrada mascotaEncontrada; // Para el constructor

  /* Si una mascota registrada es encontrada varias veces. Deberia ser
  * @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)*/
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "id_mascota", referencedColumnName = "id")
  private Mascota mascota;

  public MascotaEncontradaConChapita(MascotaEncontrada mascotaEncontrada, Mascota mascota) {
    this.mascotaEncontrada = mascotaEncontrada;
    this.mascota = mascota;
  }

  public void informar() {
    this.avisarDuenio();
  }

  public void avisarDuenio() {
    List<MedioDeContacto> mediosDeContacto = mascota.getDuenio().getMediosDeContactoPreferidos();
    List<Contacto> contactos = mascota.getDuenio().getContactos();
    String asunto = "Encontramos la mascota perdida de " + mascota.getDuenio().getNombre();
    String mensaje = "Encontramos a " + mascota.getNombre() + ", coordina con el rescatista para buscarla! " +
        "Contacto del rescatista: " + getMascotaEncontrada().getRescatista().obtenerMailsComoString();

    TareaEnviarMensajes enviarMensajes = new TareaEnviarMensajes();
    enviarMensajes.enviarMensajesPorVariosMediosA(contactos, mediosDeContacto, asunto, mensaje);


  }

  public Mascota getMascota() {
    return mascota;
  }

  public MascotaEncontrada getMascotaEncontrada() {
    return mascotaEncontrada;
  }

  public MascotaEncontradaConChapita(){}

}
