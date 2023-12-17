package rescatistas;

import mascotasEncontradas.MascotaEncontrada;
import repositorios.RepositorioMascotasEncontradas;
import rescatistas.rescatistaExceptions.RescatistaDebeTenerAlMenosUnContacto;
import usuarios.Contacto;
import usuarios.TipoDocumento;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "rescatistas")
public class Rescatista {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Enumerated(value = EnumType.STRING)
  private TipoDocumento tipoDocumento;

  @Column(name = "numeroDeDocumento")
  private int numeroDeDocumento;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "rescatista_id")
  private List<Contacto> contactos;

  public Rescatista(String nombre, String apellido, TipoDocumento tipoDocumento, int numeroDeDocumento, List<Contacto> contactos) {
    this.nombre = Objects.requireNonNull(nombre);
    this.apellido = Objects.requireNonNull(apellido);
    this.tipoDocumento = Objects.requireNonNull(tipoDocumento);
    this.numeroDeDocumento = numeroDeDocumento;
    if(contactos == null || contactos.isEmpty()){
      throw new RescatistaDebeTenerAlMenosUnContacto("Rescatista debe tener al menos un contacto");
    }
    this.contactos = contactos;
  }

  //Getters para testing

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public int getNumeroDeDocumento() {
    return numeroDeDocumento;
  }

  public List<Contacto> getContactos() {
    return contactos;
  }

  public String obtenerMailsComoString() {
    return getContactos().stream().map(Contacto::getEmail).collect(Collectors.joining(", "));
  }
}
