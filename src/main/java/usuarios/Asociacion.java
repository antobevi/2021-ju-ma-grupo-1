package usuarios;

import adopciones.preguntas.Pregunta;
import mapa.Ubicacion;
import publicaciones.PublicacionAdopcion;
import publicaciones.PublicacionAdoptante;
import repositorios.RepositorioPreguntas;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Asociacion {

  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  Ubicacion ubicacion;

  @OneToMany(mappedBy = "asociacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  List<Voluntario> voluntarios;

  //No hago el cascade porque no se van a modificar los datos: las preguntas van a ser siempre las mismas
  //Si se agregan/quitan preguntas, iria el cascade?
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "asociacion_id")
  List<PublicacionAdoptante> publicacionesAdoptantes;

  @OneToMany(fetch = FetchType.LAZY) //El fetch por default ya es lazy!
  @JoinColumn(name = "asociacion_id")
  List<PublicacionAdopcion> publicacionesAdopcion;

  @ManyToMany
  List<Pregunta> preguntasPropias;

  public Asociacion(Ubicacion ubicacion) {
    this.ubicacion = ubicacion;
    this.preguntasPropias = new ArrayList<>();
    this.voluntarios = new ArrayList<>();
    this.publicacionesAdopcion = new ArrayList<>();
    this.publicacionesAdoptantes = new ArrayList<>();
  }

  public Asociacion(){}

  public void agregarVoluntario(Voluntario voluntario) {
    this.voluntarios.add(voluntario);
  }

  public List<Voluntario> getVoluntarios() {
    return voluntarios;
  }

  public void agregarPreguntasPropias(Pregunta... preguntas) {
    preguntasPropias.addAll(Arrays.asList(preguntas));
  }

  public void borrarPreguntasPropias(Pregunta... preguntas) {
    Arrays.stream(preguntas).forEach(pregunta -> preguntasPropias.remove(pregunta));
  }

  public List<Pregunta> getPreguntasPropias() {
    return preguntasPropias;
  }

  public List<Pregunta> getPreguntasAHacer() {
    List<Pregunta> preguntasAHacer = this.preguntasPropias;
    preguntasAHacer.addAll(RepositorioPreguntas.getRepositorioPreguntas().getPreguntasFijas());
    return preguntasAHacer;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public List<PublicacionAdopcion> getPublicacionesAdopcion() {
    return publicacionesAdopcion;
  }

  public List<PublicacionAdoptante> getPublicacionesAdoptantes() {
    return publicacionesAdoptantes;
  }

  public void quitarPublicacionAdoptante(PublicacionAdoptante publicacion) {
    publicacionesAdoptantes.remove(publicacion);
  }

  public void agregarPublicacionAdoptante(PublicacionAdoptante publicacionAdoptante) {
    this.publicacionesAdoptantes.add(publicacionAdoptante);
  }

  public void agregarPublicacionAdopcion(PublicacionAdopcion publicacionAdopcion) {
    this.publicacionesAdopcion.add(publicacionAdopcion);
  }

  public void recomendarMascotasParaAdoptar() {
    this.getPublicacionesAdoptantes().forEach(publicacionAdoptante -> {
      List<PublicacionAdopcion> mascotasRecomendadas = this.obtenerPublicacionesParaAdoptante(publicacionAdoptante);
      publicacionAdoptante.getAdoptante().recibirRecomendaciones(mascotasRecomendadas);
    });
  }

  private List<PublicacionAdopcion> obtenerPublicacionesParaAdoptante(PublicacionAdoptante publicacionAdoptante) {
    return this.getPublicacionesAdopcion().stream()
        .filter(publicacionAdopcion -> publicacionAdopcion.coincideCon(publicacionAdoptante.getRespuestas()))
        .collect(Collectors.toList());
  }
}
