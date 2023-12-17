package repositorios;

import mapa.Ubicacion;
import usuarios.Asociacion;

import java.util.*;

public class RepositorioAsociaciones {
  private static RepositorioAsociaciones instance = new RepositorioAsociaciones();
  private List<Asociacion> asociaciones;

  private RepositorioAsociaciones() {
    this.asociaciones = new ArrayList<>();
  }

  public static RepositorioAsociaciones getRepositorio() {
    return instance;
  }

  public void agregarAsociaciones(Asociacion ... asociaciones){
    Collections.addAll(this.asociaciones, asociaciones);
  }

  public List<Asociacion> getAsociaciones() {
    return asociaciones;
  }

  public Asociacion asociacionMasCercanaA(Ubicacion ubicacion){
    return getAsociaciones().stream()
        .min(Comparator.comparing(asociacion -> asociacion.getUbicacion().otraDistancia(ubicacion)))
        .orElse(null);
  }

  // true si no hay una asociacion mas cercana (si la lista esta vacia) o si la asociacion es la mas cercana a la ubicacion
  public Boolean esLaAsociacionMasCercana(Ubicacion ubicacion, Asociacion asociacion){
    Asociacion asociacionMasCercana = this.asociacionMasCercanaA(ubicacion);
    return asociacionMasCercana == null || asociacionMasCercana.equals(asociacion);
  }

  // Para tests
  public void eliminarAsociacionesGuardadas(){
    this.asociaciones = new ArrayList<>();
  }

}
