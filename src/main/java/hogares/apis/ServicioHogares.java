package hogares.apis;

import hogares.Hogar;
import hogares.apis.filtrosHogar.FiltroHogar;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioHogares {
  HogaresTransitoApi api;

  public ServicioHogares(HogaresTransitoApi api) {
    this.api = api;
  }

  public List<Hogar> getHogares() {
    return api.getHogares();
  }

  public List<Hogar> getHogares(FiltroHogar... filtros) {
    List<Hogar> hogares = api.getHogares();
    hogares = hogares.stream().filter(hogar ->
        Arrays.stream(filtros).allMatch(filtro -> filtro.filtrar(hogar))
    ).collect(Collectors.toList());
    return hogares;
  }
}