package mediosDeContacto.medioDeContactoException;

public class NoSePuedeEnviarMailException extends RuntimeException{

    public NoSePuedeEnviarMailException(String mensajeDeError) {
      super(mensajeDeError);
    }

}
