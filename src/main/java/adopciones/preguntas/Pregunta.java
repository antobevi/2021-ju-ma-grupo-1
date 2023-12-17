package adopciones.preguntas;

import adopciones.preguntas.preguntasExceptions.RespuestaInvalidaException;

import javax.persistence.*;
import java.util.List;


@Entity
public class Pregunta {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  private String preguntaMascota;
  private String preguntaAdoptante;
  @ElementCollection // parametrizar para singular
  private List<String> opciones;

  public Pregunta(String preguntaMascota, String preguntaAdoptante, List<String> opciones) {
    this.preguntaMascota = preguntaMascota;
    this.preguntaAdoptante = preguntaAdoptante;
    this.opciones = opciones;
  }

  public PreguntaRespondida responder(String respuesta){
    if(!opciones.contains(respuesta)) throw new RespuestaInvalidaException("La respuesta no esta contenida en las opciones válidas.");
    return new PreguntaRespondida(this, respuesta);
  }

  public String getPreguntaMascota() {
    return preguntaMascota;
  }

  public String getPreguntaAdoptante() {
    return preguntaAdoptante;
  }

  public List<String> getOpciones() {
    return opciones;
  }
}
