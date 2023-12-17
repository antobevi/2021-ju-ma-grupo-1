package hogares.apis.filtrosHogar;

import hogares.Hogar;
import mapa.Ubicacion;

public class FiltroPorCercania implements FiltroHogar{
  private Ubicacion ubicacion;
  private double radio;

  public FiltroPorCercania(Ubicacion ubicacion, double radio) {
    this.ubicacion = ubicacion;
    this.radio = radio;
  }

  public boolean filtrar(Hogar hogar){
    return ubicacion.distancia(hogar.getUbicacion()) <= radio;
  }
}
