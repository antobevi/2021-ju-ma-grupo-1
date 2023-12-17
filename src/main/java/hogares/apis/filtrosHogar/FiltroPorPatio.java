package hogares.apis.filtrosHogar;

import hogares.Hogar;

public class FiltroPorPatio implements FiltroHogar{
  private boolean conPatio;

  public FiltroPorPatio(boolean conPatio) {
    this.conPatio = conPatio;
  }

  public boolean filtrar(Hogar hogar){
    return hogar.tienePatio() == this.conPatio;
  }
}
