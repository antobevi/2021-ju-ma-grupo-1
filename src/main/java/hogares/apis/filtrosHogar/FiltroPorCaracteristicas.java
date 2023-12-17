package hogares.apis.filtrosHogar;

import hogares.Hogar;

import java.util.Arrays;
import java.util.List;

public class FiltroPorCaracteristicas implements FiltroHogar{
  List<String> caracteristicas;

  public FiltroPorCaracteristicas(List<String> caracteristicas) {
    this.caracteristicas = caracteristicas;
  }

  public FiltroPorCaracteristicas(String ... caracteristicas){
    this.caracteristicas = Arrays.asList(caracteristicas);
  }

  public boolean filtrar(Hogar hogar){
    return caracteristicas.stream().allMatch(caracteristica -> hogar.tieneCaracteristica(caracteristica));
  }
}
