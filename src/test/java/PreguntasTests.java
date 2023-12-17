import adopciones.preguntas.Pregunta;
import adopciones.preguntas.PreguntaRespondida;
import adopciones.preguntas.preguntasExceptions.RespuestaInvalidaException;
import mapa.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositorios.RepositorioPreguntas;
import usuarios.Asociacion;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PreguntasTests {

  @BeforeEach
  public void init(){
    RepositorioPreguntas.getRepositorioPreguntas().borrarTodasLasPreguntasFijas();

  }

  @Test
  public void lasPreguntasQueHacenLasAsociacionesSonLasPropiasMasLasFijas(){
    Pregunta p1 = unaPregunta("Ruido en el barrio?", "Ruido en el barrio?", "Mucho", "Poco");
    Pregunta p2 = unaPregunta("Tamanio mascota?", "Tamanio mascota?", "Grande", "Mediano", "Chico");
    Asociacion asociacion = new Asociacion(new Ubicacion(35,13));
    asociacion.agregarPreguntasPropias(p1, p2);


    Pregunta pFija1 = unaPregunta("Necesita patio?", "Tiene patio?", "Si", "No");
    Pregunta pFija2 = unaPregunta("Cantidad mascotas?", "Cantidad mascotas?", "Ninguna", "Entre 1 y 5", "Mas de 5");
    RepositorioPreguntas repo = RepositorioPreguntas.getRepositorioPreguntas();
    repo.agregarPreguntaAlRepo(pFija1);
    repo.agregarPreguntaAlRepo(pFija2);

    // repo.agregarPreguntasFijas(pFija1);
    //repo.agregarPreguntasFijas(pFija2);

    assertEquals(4, asociacion.getPreguntasAHacer().size());
    assertTrue(asociacion.getPreguntasAHacer().contains(p1));
    assertTrue(asociacion.getPreguntasAHacer().contains(p2));
    assertTrue(asociacion.getPreguntasAHacer().contains(pFija1));
    assertTrue(asociacion.getPreguntasAHacer().contains(pFija2));
  }

  @Test
  public void responderUnaPreguntaCreaUnaPreguntaRespondida() {
    Pregunta pregunta = unaPregunta("Necesita patio?", "Tiene patio?", "Si", "No");
    PreguntaRespondida preguntaRespondida = pregunta.responder("Si");

    assertEquals("Si", preguntaRespondida.getRespuesta());
    assertEquals(pregunta, preguntaRespondida.getPregunta());
  }

  @Test
  public void responderAlgoInvalidoNoDejaCrearUnaPreguntaRespondida(){
    Pregunta pregunta = unaPregunta("Necesita patio?", "Tiene patio?", "Si", "No");
    assertThrows(RespuestaInvalidaException.class, () -> pregunta.responder("Ni si ni no"));
  }

  @Test
  public void sePuedenAgregarPreguntasFijasAlRepositorioDePreguntas() {
    Pregunta p1 = unaPregunta("Necesita patio?", "Tiene patio?", "Si", "No");
    Pregunta p2 = unaPregunta("Tamanio mascota?", "Tamanio mascota?", "Grande", "Mediano", "Chico");

    RepositorioPreguntas repo = RepositorioPreguntas.getRepositorioPreguntas();
    repo.agregarPreguntaAlRepo(p1);
    repo.agregarPreguntaAlRepo(p2);
    //repo.agregarPreguntasFijas(p1,p2);

    assertTrue(repo.getPreguntasFijas().contains(p1));
    assertTrue(repo.getPreguntasFijas().contains(p2));
  }

  @Test
  public void lasAsociacionesPuedenAgregarSusPropiasPreguntas(){
    Pregunta p1 = unaPregunta("Necesita patio?", "Tiene patio?", "Si", "No");
    Pregunta p2 = unaPregunta("Tamanio mascota?", "Tamanio mascota?", "Grande", "Mediano", "Chico");

    Asociacion asociacion = new Asociacion(new Ubicacion(35,13));
    asociacion.agregarPreguntasPropias(p1, p2);

    assertTrue(asociacion.getPreguntasPropias().contains(p1));
    assertTrue(asociacion.getPreguntasPropias().contains(p2));
  }



  /// Instanciadores
  private Pregunta unaPregunta(String preguntaMascota, String preguntaAdoptante, String ... opciones){
    return new Pregunta(preguntaMascota, preguntaAdoptante, unasOpciones(opciones));
  }
  private List<String> unasOpciones(String... opciones) {
    return Arrays.asList(opciones);
  }
}
