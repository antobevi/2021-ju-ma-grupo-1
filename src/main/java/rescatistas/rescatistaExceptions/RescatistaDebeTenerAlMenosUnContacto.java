package rescatistas.rescatistaExceptions;

public class RescatistaDebeTenerAlMenosUnContacto extends RuntimeException {
  public RescatistaDebeTenerAlMenosUnContacto (String message) {
    super(message);
  }
}
