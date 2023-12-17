package mediosDeContacto;

import usuarios.Contacto;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public abstract class MedioDeContacto {
    @Id
    @GeneratedValue
    private Long id;
    abstract void comunicarleA(Contacto contacto, String Asunto, String mensaje);

}