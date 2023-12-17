package mascotas;

import caracteristicas.Caracteristica;
import caracteristicas.CaracteristicaMascota;
import usuarios.Duenio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MascotaBuilder {
  private TipoMascota tipo;
  private String nombre;
  private String apodo;
  private Integer edad;
  private Sexo sexo;
  private String descripcion;
  private List<String> fotos;
  private List<CaracteristicaMascota> caracteristicas;
//  private EstadoMascota estado;
  private Duenio duenio;

  //Mascota vacia
  public MascotaBuilder() {
  }

  //Mascotas pre-setteadas
  public MascotaBuilder perroConNombre(String nombre) {
    setTipo(TipoMascota.PERRO);
    setNombre(nombre);
    setApodo(nombre);
    setEdad(5);
    setSexo(Sexo.MACHO);
    setDescripcion("Perro por default");
    setFotos(new ArrayList<>());
    setCaracteristicas(new ArrayList<>());
    return this;
  }
  public MascotaBuilder perroConNombreYDuenio(String nombre, Duenio duenio) {
    return this.perroConNombre(nombre).setDuenio(duenio);
  }

  public Mascota build() {
    return new Mascota(tipo, nombre, apodo, edad, sexo, descripcion, fotos, caracteristicas, duenio);
  }

  //Setters
  public MascotaBuilder setTipo(TipoMascota tipo) {
    this.tipo = Objects.requireNonNull(tipo);
    return this;
  }

  public MascotaBuilder setNombre(String nombre) {
    this.nombre = Objects.requireNonNull(nombre);
    return this;
  }

  public MascotaBuilder setApodo(String apodo) {
    this.apodo = Objects.requireNonNull(apodo);
    return this;
  }

  public MascotaBuilder setEdad(Integer edad) {
    this.edad = Objects.requireNonNull(edad);
    return this;
  }

  public MascotaBuilder setSexo(Sexo sexo) {
    this.sexo = Objects.requireNonNull(sexo);
    return this;
  }

  public MascotaBuilder setDescripcion(String descripcion) {
    this.descripcion = Objects.requireNonNull(descripcion);
    return this;
  }

  public MascotaBuilder setFotos(List<String> fotos) {
    this.fotos = Objects.requireNonNull(fotos);
    return this;
  }

  public MascotaBuilder setCaracteristicas(List<CaracteristicaMascota> caracteristicas) {
    this.caracteristicas = Objects.requireNonNull(caracteristicas);
    return this;
  }

  public MascotaBuilder setDuenio(Duenio duenio) {
    this.duenio = Objects.requireNonNull(duenio);
    return this;
  }

//  public MascotaBuilder setEstado(EstadoMascota estado) {
//    this.estado = Objects.requireNonNull(estado);
//    return this;
//  }
}
