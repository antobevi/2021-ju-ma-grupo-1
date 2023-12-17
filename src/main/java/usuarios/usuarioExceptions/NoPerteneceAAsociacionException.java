package usuarios.usuarioExceptions;

public class NoPerteneceAAsociacionException extends  RuntimeException{

  public NoPerteneceAAsociacionException(String mensajeDeError) {
      super(mensajeDeError);
    }


}
