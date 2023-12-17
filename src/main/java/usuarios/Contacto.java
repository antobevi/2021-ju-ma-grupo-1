package usuarios;


import javax.persistence.*;
@Entity
public class Contacto {

  @Id@GeneratedValue
  private Long id_contacto;
  private String nombre;
  private String apellido;
  private String telefono;
  private String email;


  public Contacto(String nombre, String apellido, String telefono, String email) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.telefono = telefono;
    this.email = email;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public String getEmail() {
    return email;
  }

  public String getTelefono() {
    return telefono;
  }
}
