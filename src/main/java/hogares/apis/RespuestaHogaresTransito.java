package hogares.apis;

import hogares.Hogar;

import java.util.ArrayList;
import java.util.List;

public class RespuestaHogaresTransito {
  private int total;
  private List<Hogar> hogares;

  public RespuestaHogaresTransito(int total, List<Hogar> hogares) {
    this.total = total;
    this.hogares = hogares;
  }

  public List<Hogar> getHogares() {
    return hogares;
  }

  public int getTotal() {
    return total;
  }
}
