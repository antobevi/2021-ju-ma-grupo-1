package mediosDeContacto.medioDeContactoException;

public class NoSePuedeEnviarMensajeException extends RuntimeException{

  public NoSePuedeEnviarMensajeException(String mensajeDeError) {
    super(mensajeDeError);
  }

}
