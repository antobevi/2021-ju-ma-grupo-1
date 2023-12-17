package hogares;

import com.google.gson.annotations.SerializedName;
import mapa.Ubicacion;
import mascotas.TipoMascota;

import java.util.List;
import java.util.Map;

public class Hogar {
  private String nombre;
  private String telefono;
  private Ubicacion ubicacion;
  private Map<TipoMascota, Boolean> admisiones;
  private int capacidad;
  private boolean patio;
  private List<String> caracteristicas;
  @SerializedName("lugares_disponibles")
  private int lugaresDisponibles;


  public Hogar(String nombre, String telefono, Ubicacion ubicacion, Map<TipoMascota, Boolean> admisiones, int capacidad, int lugaresDisponibles, boolean patio, List<String> caracteristicas) {
    this.nombre = nombre;
    this.telefono = telefono;
    this.ubicacion = ubicacion;
    this.admisiones = admisiones;
    this.capacidad = capacidad;
    this.patio = patio;
    this.caracteristicas = caracteristicas;
    this.lugaresDisponibles = lugaresDisponibles;
  }

  public String getNombre() {
    return nombre;
  }

  public String getTelefono() {
    return telefono;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public int getCapacidad() {
    return capacidad;
  }

  public boolean admite(TipoMascota tipo) {
    return admisiones.get(tipo);
  }

  public boolean tieneDisponibilidad(int cantidadMinima) {
    return lugaresDisponibles >= cantidadMinima;
  }

  public boolean tienePatio() {
    return patio;
  }

  public boolean tieneCaracteristica(String caracteristicaBuscada) {
    return caracteristicas.stream().anyMatch(caracteristica -> caracteristica.equals(caracteristicaBuscada));
  }

  public int getLugaresDisponibles() {
    return lugaresDisponibles;
  }

  @Override
  public String toString() {
    return "Nombre: " + nombre +
        "\nUbicacion: lat~" + ubicacion.getLatitud() + "| lon~" + ubicacion.getLongitud() + "| dir~" + ubicacion.getDireccion() +
        "\nTelefono: " + telefono +
        "\nAdmisiones: " + admisiones +
        "\nPatio: " + patio +
        "\nCapacidad: " + capacidad +
        "\nLugares disponibles: " + lugaresDisponibles +
        "\nCaracteristicas: " + caracteristicas +
        "\n";
  }
}
