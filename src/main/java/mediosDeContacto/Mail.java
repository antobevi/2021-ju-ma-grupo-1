package mediosDeContacto;


public class Mail {
  private String destinatario;
  private String asunto;
  private String cuerpoDelMensaje;

  public Mail(String destinatario, String asunto, String cuerpoDelMensaje) {
    this.destinatario = destinatario;
    this.asunto = asunto;
    this.cuerpoDelMensaje = cuerpoDelMensaje;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public String getAsunto() {
    return asunto;
  }

  public String getCuerpoDelMensaje() {
    return cuerpoDelMensaje;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }
}
