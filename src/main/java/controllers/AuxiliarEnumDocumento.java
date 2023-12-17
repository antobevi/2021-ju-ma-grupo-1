package controllers;

public class AuxiliarEnumDocumento {

  String nombre;

  String valor;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getValor() {
    return valor;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public AuxiliarEnumDocumento(String nombre, String valor) {
    this.nombre = nombre;
    this.valor = valor;
  }
}
