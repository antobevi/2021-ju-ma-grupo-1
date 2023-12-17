package mediosDeContacto;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.AuthenticationException;
import mediosDeContacto.medioDeContactoException.NoSePuedeEnviarMensajeException;
import usuarios.Contacto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "twilio")
public class MensajeriaTwilio extends MedioDeContacto{
  @Transient
  public static final String ACCOUNT_SID = System.getenv("ACb5vd6j5scv5ngv456cbxD4SKskl54al");
  @Transient
  public static final String AUTH_TOKEN = System.getenv("bb41sdfhnd42s24kncawwQ843smnpl896");
  @Transient
  private String numeroRemitente = "+19723518034";

  public void setupClient() throws AuthenticationException {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
  }

  public void enviarMensaje(String numeroDestino, String mensajeAEnviar) {

    try {
      this.setupClient();

      Message message = this.crearMensaje(numeroDestino, mensajeAEnviar);

      System.out.println(message.getSid());
    } catch (Exception e) {
      e.printStackTrace();
      throw new NoSePuedeEnviarMensajeException("No se pudo realizar el envío del mensaje");
    }
  }

  public Message crearMensaje(String destino, String cuerpoMensaje) {
    return Message.creator(new PhoneNumber(this.numeroRemitente),
        new PhoneNumber(destino), cuerpoMensaje).create();
  }

  @Override
  public void comunicarleA(Contacto contacto, String asunto, String mensaje) {
    String numeroDeTelefono = contacto.getTelefono();
    this.enviarMensaje(numeroDeTelefono, mensaje);
  }

}
