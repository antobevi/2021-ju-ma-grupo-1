package mascotas;

import caracteristicas.CaracteristicaMascota;
import usuarios.Duenio;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "mascotas")
public class Mascota {

  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private TipoMascota tipo;

  private String nombre;
  private String apodo;
  private Integer edad;
  private String descripcion;

  @Enumerated(value = EnumType.STRING)
  private Sexo sexo;

  @Enumerated(value = EnumType.STRING)
  private EstadoMascota estado;

  @ElementCollection
  private List<String> fotos;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "mascota_id")
  private List<CaracteristicaMascota> caracteristicas;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "duenio_id")
  private Duenio duenio;

  public Mascota(TipoMascota tipo, String nombre, String apodo, Integer edad, Sexo sexo, String descripcion, List<String> fotos, List<CaracteristicaMascota> caracteristicas, Duenio duenio) {
    this.tipo = Objects.requireNonNull(tipo);
    this.nombre = Objects.requireNonNull(nombre);
    this.apodo = Objects.requireNonNull(apodo);
    this.edad = Objects.requireNonNull(edad);
    this.sexo = Objects.requireNonNull(sexo);
    this.descripcion = Objects.requireNonNull(descripcion);
    this.fotos = Objects.requireNonNull(fotos);
    this.caracteristicas = Objects.requireNonNull(caracteristicas);
    this.estado = EstadoMascota.REGISTRADA;
    this.duenio = duenio;
  }

  public Mascota() {
  }

  public Long getId() {
    return id;
  }

  public TipoMascota getTipo() {
    return tipo;
  }

  public String getNombre() {
    return nombre;
  }

  public EstadoMascota getEstado() {
    return estado;
  }

  public void setEstado(EstadoMascota estado) {
    this.estado = estado;
  }

  public String getApodo() {
    return apodo;
  }

  public List<CaracteristicaMascota> getCaracteristicas() {
    return caracteristicas;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Integer getEdad() {
    return edad;
  }

  public List<String> getFotos() {
    return fotos;
  }

  public Sexo getSexo() {
    return sexo;
  }

  public Duenio getDuenio() {
    return duenio;
  }

  public void setFotos(List<String> fotos) {
    this.fotos = fotos;
  }

  public void setCaracteristicas(List<CaracteristicaMascota> caracteristicas) {
    this.caracteristicas = caracteristicas;
  }

  public Boolean estaPerdida() {
    return this.estado.toString().equals("PERDIDA");
  }

  public void cambiarEstadoAPerdida() {
    this.estado = EstadoMascota.PERDIDA;
  }
}
