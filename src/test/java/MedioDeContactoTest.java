import mediosDeContacto.*;
import mediosDeContacto.medioDeContactoException.NoSePuedeEnviarMailException;
import mediosDeContacto.medioDeContactoException.NoSePuedeEnviarMensajeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import usuarios.Contacto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MedioDeContactoTest {
  private MailerGmail mailerMock;
  private MensajeriaTwilio twilioMock;
  private Contacto contactoJuan = new Contacto("Juan", "Perez", "1198743125", "juanPerez85@gmail.com");
  private Contacto contactoCaro = new Contacto("Carolina", "Almada", "1134956310", "caro_almada@gmail.com");
  private TareaEnviarMensajes tarea = new TareaEnviarMensajes();

  @BeforeEach
  public void init() {
    mailerMock = Mockito.mock(MailerGmail.class);
    twilioMock = Mockito.mock(MensajeriaTwilio.class);

    ServiceLocator.getServiceLocator().registrar(MailerGmail.class, mailerMock);
    ServiceLocator.getServiceLocator().registrar(MensajeriaTwilio.class, twilioMock);
  }

  @AfterEach
  public void borrarServicios() {
    ServiceLocator.getServiceLocator().eliminarServicios();
  }

  @Test
  public void envioDeMailSeRealizaCorrectamente() {
    tarea.enviarMailATodosLosContactos(unaListaDeContactos(contactoJuan), "PruebaTest", "Hola Juan Perez");

    Mockito.verify(mailerMock, only()).enviarMail(any());
  }

  @Test
  public void envioDeSMSSeRealizaCorrectamente() {
    tarea.enviarSMSATodosLosContactos(unaListaDeContactos(contactoJuan),"Hola Juan Perez");

    Mockito.verify(twilioMock, only()).enviarMensaje(anyString(), anyString());
  }

  @Test
  public void seRealizaCorrectamenteEnvioDeMensajesADosContactos() {
    tarea.enviarMensajesPorVariosMediosA(unaListaDeContactos(contactoJuan, contactoCaro), unaListaDeMediosDeContactos(new MailerGmail(), new MensajeriaTwilio()), "Hola", "Prueba para medios de contacto");

    Mockito.verify(mailerMock, times(2)).comunicarleA(any(), anyString(), anyString());
    Mockito.verify(twilioMock, times(2)).comunicarleA(any(), anyString(), anyString());
  }

  @Test
  public void mailerGmailLanzaExcepcionPorAlgunErrorDeConexion() {
    doThrow(new NoSePuedeEnviarMailException("No se pudo realizar el envío del mail")).when(mailerMock).enviarMail(any());

    Assertions.assertThrows(NoSePuedeEnviarMailException.class, () -> mailerMock.enviarMail(new Mail("caro_almada@gmail.com", "Hola", "PruebaTest")));
  }

  @Test
  public void mensajeriaTwilioLanzaExcepcionPorAlgunErrorDeConexion() {
    doThrow(new NoSePuedeEnviarMensajeException("No se pudo realizar el envío del mensaje")).when(twilioMock).enviarMensaje(anyString(), anyString());

    Assertions.assertThrows(NoSePuedeEnviarMensajeException.class, () -> twilioMock.enviarMensaje("1101021194", "Mensaje con error"));
  }

  @Test
  public void alInstanciarUnMailSeCreaCorrectamente() {
    Mail mail = new Mail(null, "Información importante", "Hola ❤");
    mail.setDestinatario("caro_almada@gmail.com");
    Assertions.assertEquals("caro_almada@gmail.com",mail.getDestinatario());
    Assertions.assertEquals("Información importante",mail.getAsunto());
    Assertions.assertEquals("Hola ❤", mail.getCuerpoDelMensaje());
  }

  @Test
  public void mailerGmailEnvioDeMailSeRealizaCorrectamente() {
    MockingDetails details = mockingDetails(mailerMock);
    doCallRealMethod().when(mailerMock).comunicarleA(any(),anyString(),anyString());

    mailerMock.comunicarleA(contactoJuan, "Pruebita", "EjemploTest");

    Assertions.assertEquals(2, details.getInvocations().size());
  }

  @Test
  public void mensajeriaTwilioEnvioDeMailSeRealizaCorrectamente() {
    MockingDetails details = mockingDetails(twilioMock);
    doCallRealMethod().when(twilioMock).comunicarleA(any(),anyString(),anyString());

    twilioMock.comunicarleA(contactoJuan, "Pruebita", "EjemploTest");

    Assertions.assertEquals(2, details.getInvocations().size());
  }

  // Instanciadores
  private List<Contacto> unaListaDeContactos(Contacto... contactos) {
    List<Contacto> lista = new ArrayList<>();
    Collections.addAll(lista, contactos);
    return lista;
  }

  private List<MedioDeContacto> unaListaDeMediosDeContactos(MedioDeContacto ... medios) {
    List<MedioDeContacto> lista = new ArrayList<>();
    Collections.addAll(lista, medios);
    return lista;
  }
}
