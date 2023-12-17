package publicaciones;

import adopciones.preguntas.PreguntaRespondida;
import repositorios.RepositorioAsociaciones;
import usuarios.Adoptante;
import usuarios.Asociacion;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "publicacionesAdoptantes")
public class PublicacionAdoptante {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "adoptante_id")
  private Adoptante adoptante;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "publicacion_adopante_id")
  private List<PreguntaRespondida> respuestas;

  //TODO: Ver si hay que hacer como Creador en PublicacionAdopcion o cambiar como se crea PublicacionAdopcion
  public PublicacionAdoptante(Adoptante adoptante, List<PreguntaRespondida> respuestas) {
    this.adoptante = adoptante;
    this.respuestas = respuestas;
  }

  public void darDeBaja() {
    this.getAsociacionACargo().quitarPublicacionAdoptante(this);
  }

  public Asociacion getAsociacionACargo() {
    return RepositorioAsociaciones.getRepositorio().getAsociaciones().stream().
        filter(asoc -> asoc.getPublicacionesAdoptantes().contains(this)).findFirst().get();
  }

  public Adoptante getAdoptante() {
    return adoptante;
  }

  public List<PreguntaRespondida> getRespuestas() {
    return respuestas;
  }

}
