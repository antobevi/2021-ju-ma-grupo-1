package adopciones.notificador;

import repositorios.RepositorioAsociaciones;
import usuarios.Asociacion;

import java.util.List;

public class NotificadorDeAdopciones {

  public void notificar(){
    // Para pensar a futuro: guardar quienes recibieron notificaciones y cuando, y controlar que no se vuelva a enviar lo mismo
    RepositorioAsociaciones.getRepositorio().getAsociaciones().forEach(Asociacion::recomendarMascotasParaAdoptar);
  }

}
