package adopciones.preguntas.preguntasExceptions;

public class RespuestaInvalidaException extends RuntimeException {
  public RespuestaInvalidaException(String mensajeDeError) {
    super(mensajeDeError);
  }
}