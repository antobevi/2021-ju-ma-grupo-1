package usuarios;

import caracteristicas.Caracteristica;
import repositorios.RepositorioCaracteristicas;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private LocalDate fechaNacimiento;
    private TipoDocumento tipoDocumento;
    private int numeroDocumento;
    private String username;
    private String password;

    public Admin(String nombre, LocalDate fechaNacimiento, TipoDocumento tipoDocumento, int numeroDocumento, String username, String password) throws NoSuchAlgorithmException {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.username = username;
        this.password = password;
        ValidadorContrasenias.getValidadorContrasenias().validarPassword(password);
    }

    public Admin() {

    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {

        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento.toString();
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}
