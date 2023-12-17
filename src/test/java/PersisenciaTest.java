import adopciones.preguntas.Pregunta;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import repositorios.RepositorioPreguntas;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersisenciaTest extends AbstractPersistenceTest  implements WithGlobalEntityManager {

  @After
  public void rollBack() {
    rollbackTransaction();
  }

  @Test
  public void persistirUnaPregunta() {
    List<String> opciones = new ArrayList<>();
    opciones.add("Prueba");
    Pregunta preguntaPrueba = new Pregunta("¿Prueba?","¿Prueba?",opciones);
    RepositorioPreguntas.getRepositorioPreguntas().agregarPreguntaAlRepo(preguntaPrueba);
    List <Pregunta> resultado = RepositorioPreguntas.getRepositorioPreguntas().getPreguntasFijas();
    assertEquals(1,resultado.size());
  }




}
