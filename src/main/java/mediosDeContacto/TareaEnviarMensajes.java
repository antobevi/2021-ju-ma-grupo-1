package mediosDeContacto;

import usuarios.Contacto;

import java.util.List;

public class TareaEnviarMensajes {

  public void enviarMensajesPorVariosMediosA(List<Contacto> contactos, List<MedioDeContacto> mediosDeContacto, String asunto, String mensaje) {
    mediosDeContacto.forEach(medio -> {
      MedioDeContacto medioDeContacto;
        medioDeContacto = (MedioDeContacto) ServiceLocator.getServiceLocator().get(medio.getClass());
      contactos.forEach(contacto -> {
        medioDeContacto.comunicarleA(contacto, asunto, mensaje);
      });
    });
  }

  public void enviarMailATodosLosContactos(List<Contacto> contactos, String asunto, String mensaje) {
    MailerGmail mailer = (MailerGmail) ServiceLocator.getServiceLocator().get(MailerGmail.class);

    contactos.stream().forEach(contacto -> mailer.enviarMail(new Mail(contacto.getEmail(), asunto, mensaje)));
  }

  public void enviarSMSATodosLosContactos(List<Contacto> contactos, String mensaje) {
    MensajeriaTwilio twilio = (MensajeriaTwilio) ServiceLocator.getServiceLocator().get(MensajeriaTwilio.class);

    contactos.stream().forEach(contacto -> twilio.enviarMensaje(contacto.getTelefono(),mensaje));
  }
}
