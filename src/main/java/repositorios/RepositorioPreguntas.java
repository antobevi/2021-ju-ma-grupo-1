package repositorios;

import adopciones.preguntas.Pregunta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class RepositorioPreguntas implements WithGlobalEntityManager{
  private static RepositorioPreguntas instance = new RepositorioPreguntas();
  private List<Pregunta> preguntasFijas;

  private RepositorioPreguntas() {
    this.preguntasFijas = new ArrayList<>();
  }

  public static RepositorioPreguntas getRepositorioPreguntas(){
    return instance;
  }

  public void agregarPreguntaAlRepo(Pregunta unaPregunta) {
    this.preguntasFijas.add(unaPregunta);
    EntityTransaction transaction = entityManager().getTransaction();
    transaction.begin();
    entityManager().persist(unaPregunta);
    transaction.commit();
  }
  public List<Pregunta> getPreguntasFijas() {
    return entityManager().createQuery("from Pregunta").getResultList();
  }

  public void agregarPreguntasFijas(Pregunta ... preguntas){
    preguntasFijas.addAll(Arrays.asList(preguntas));
  }

  public void borrarPreguntaFija(Pregunta ... preguntas) {
    Arrays.stream(preguntas).forEach(pregunta -> preguntasFijas.remove(pregunta));
  }


  public void borrarTodasLasPreguntasFijas(){
    this.preguntasFijas = new ArrayList<>();
  }




  /*




  public List<Pregunta> getPreguntasFijas() {
    return preguntasFijas;
  }




  */

}
