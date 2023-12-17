package publicaciones;

import adopciones.preguntas.PreguntaRespondida;
import mascotas.Mascota;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "publicacionesAdopciones")
public class PublicacionAdopcion {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "mascota_id")
  private Mascota mascota;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "publicacion_adopcion_id")
  private List<PreguntaRespondida> respuestas;

  public PublicacionAdopcion(Mascota unaMascota, List<PreguntaRespondida> unasRtas) {
    this.mascota = unaMascota;
    this.respuestas = unasRtas;
  }

  public Boolean coincideCon(List<PreguntaRespondida> respuestasAdoptante) {
    // Comparamos solo las preguntas que esten en las 2 listas (por si se agregaron otras en otro momento)
    List<PreguntaRespondida> preguntasAComparar = this.respuestas.stream()
        .filter(respuesta -> respuestasAdoptante.stream()
            .map(PreguntaRespondida::getPreguntaAsString)
            .collect(Collectors.toList())
            .contains(respuesta.getPreguntaAsString()))
        .collect(Collectors.toList());

    // Si en todas las preguntas respondieron lo mismo, coincide
    return preguntasAComparar.stream()
        .allMatch(preguntaAdopcion -> {
          PreguntaRespondida preguntaAdoptanteAComparar = respuestasAdoptante.stream()
              .filter(respuestaAdoptante -> respuestaAdoptante.esLaMismaPregunta(preguntaAdopcion))
              .findFirst().get();
          return  preguntaAdoptanteAComparar.coincideCon(preguntaAdopcion);
        });
  }

  public Mascota getMascota() {
    return mascota;
  }

  public String obtenerDescripcion(){
    return mascota.getNombre() + " - " + mascota.getTipo() + " - " + mascota.getDescripcion();
  }

}
