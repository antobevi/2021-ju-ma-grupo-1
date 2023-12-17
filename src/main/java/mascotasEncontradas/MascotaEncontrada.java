package mascotasEncontradas;

import mapa.Ubicacion;
import mascotas.mascotaExceptions.FaltaSubirUnaFoto;
import rescatistas.Rescatista;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Embeddable
public class MascotaEncontrada {

  @ElementCollection
  public List<String> fotos;

  @Column(name = "descripcionEstado")
  public String descripcionEstado;

  @Embedded
  public Ubicacion ubicacion;

  @Column(columnDefinition = "DATE")
  public LocalDate fecha;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "id_rescatista", referencedColumnName = "id")
  public Rescatista rescatista;

  public MascotaEncontrada(){}

  public MascotaEncontrada(List<String> fotos, String descripcionEstado, Ubicacion ubicacion, LocalDate fecha, Rescatista rescatista) {
    if(fotos == null || fotos.isEmpty()) {
      throw new FaltaSubirUnaFoto("Tiene que subir al menos una foto!");
    }
    this.ubicacion = ubicacion;
    this.fecha = fecha;
    this.fotos = fotos;
    this.descripcionEstado = descripcionEstado;
    this.rescatista = rescatista;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public List<String> getFotos() {
    return fotos;
  }

  public void setFotos(List<String> fotos) {
    this.fotos = fotos;
  }

  public String getDescripcionEstado() {
    return descripcionEstado;
  }

  public void setDescripcionEstado(String descripcionEstado) {
    this.descripcionEstado = descripcionEstado;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(Ubicacion ubicacion) {
    this.ubicacion = ubicacion;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public Rescatista getRescatista() {
    return rescatista;
  }

  public void setRescatista(Rescatista rescatista) {
    this.rescatista = rescatista;
  }

}
