package hogares.apis.filtrosHogar;

import hogares.Hogar;

public class FiltroPorCapacidad implements FiltroHogar{
  private int cantidadMinima;

  public FiltroPorCapacidad(int cantidadMinima) {
    this.cantidadMinima = cantidadMinima;
  }

  public boolean filtrar(Hogar hogar){
    return hogar.tieneDisponibilidad(cantidadMinima);
  }
}
