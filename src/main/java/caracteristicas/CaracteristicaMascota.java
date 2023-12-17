package caracteristicas;

import mascotas.Mascota;

import javax.persistence.*;

@Entity
@Table(name = "caracteristicas_mascotas")
public class CaracteristicaMascota {

    @Id
    @GeneratedValue
    private Long id;

    private final String nombre;
    private String valor;


    public CaracteristicaMascota(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setValor(String nuevoValor){
        this.valor = nuevoValor;
    }

    public String getTipo(){
        return this.valor.getClass().getSimpleName();
    }

}