package caracteristicas;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "caracteristicas")
public class Caracteristica {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    public Caracteristica() {
        //Hibernate se enoja si no pongo este constructor :(
    }

    public Caracteristica(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nuevoNombre) { nombre = nuevoNombre;}

    public Long getId() {return id;}
}