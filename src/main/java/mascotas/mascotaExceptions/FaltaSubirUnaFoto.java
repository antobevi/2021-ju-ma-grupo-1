package mascotas.mascotaExceptions;

public class FaltaSubirUnaFoto extends RuntimeException {
  public FaltaSubirUnaFoto(String mensajeDeError) {
    super(mensajeDeError);
  }
}
