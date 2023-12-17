package hogares.apis.apiExceptions;

public class HttpRequestMalHecha extends RuntimeException {
  public HttpRequestMalHecha(String mensajeDeError) {
    super(mensajeDeError);
  }
}

