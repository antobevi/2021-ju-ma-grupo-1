package adopciones.preguntas;

import javax.persistence.*;

@Entity
public class PreguntaRespondida {

@Id@GeneratedValue(strategy = GenerationType.AUTO)
  private long id_pregunta_respondida;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Pregunta pregunta;

  private String respuesta;



  public PreguntaRespondida(Pregunta pregunta, String respuesta) {
    this.pregunta = pregunta;
    this.respuesta = respuesta;
  }

  public Pregunta getPregunta() {
    return pregunta;
  }

  public String getRespuesta() {
    return respuesta;
  }

  public String getPreguntaAsString(){
    return this.pregunta.getPreguntaMascota();
  }


  public Boolean esLaMismaPregunta(PreguntaRespondida preguntaRespondida){
    return this.pregunta.equals(preguntaRespondida.getPregunta());
  }

  public Boolean coincideCon(PreguntaRespondida preguntaRespondida){
    return this.getRespuesta().equals(preguntaRespondida.getRespuesta());
  }
}
