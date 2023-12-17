package usuarios.usuarioExceptions;

public class ArchivoNotFoundException extends RuntimeException {

  public ArchivoNotFoundException(String mensajeDeError) {
      System.out.println(mensajeDeError);
    }



}
