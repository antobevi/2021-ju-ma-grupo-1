package hogares.apis.filtrosHogar;

import hogares.Hogar;
import mascotas.TipoMascota;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorTipoMascota implements FiltroHogar {
  private List<TipoMascota> tiposAdmitidos;

  public FiltroPorTipoMascota(List<TipoMascota> tiposQueAceptan){
    this.tiposAdmitidos = tiposQueAceptan;
  }

  public FiltroPorTipoMascota(TipoMascota ... tiposAdmitidos) {
    this.tiposAdmitidos = Arrays.asList(tiposAdmitidos);
  }

  public boolean filtrar(Hogar hogar){
    return tiposAdmitidos.stream().allMatch(tipo -> hogar.admite(tipo));
  }
}
