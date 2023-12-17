import caracteristicas.Caracteristica;
import mascotas.*;
import org.junit.jupiter.api.Test;
import usuarios.Contacto;
import usuarios.Duenio;
import usuarios.TipoDocumento;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MascotaTest {
    public MascotaTest() throws NoSuchAlgorithmException {
    }

    Duenio elDuenio = unDuenio();
//    Mascota firulais = new MascotaBuilder()
//        .setTipo(TipoMascota.PERRO)
//        .setNombre("Firulais")
//        .setApodo("Firu")
//        .setEdad(5)
//        .setSexo(Sexo.MACHO)
//        .setDescripcion("Perro leal")
//        .setFotos(new ArrayList<>())
//        .setCaracteristicas(new ArrayList<>())
//        .setChapita(unaChapita)
//        .build();
    Mascota firulais = new MascotaBuilder().perroConNombreYDuenio("Firulais", unDuenio()).build();

    @Test
    public void unaMascotaPerroEsDeTipoPerro(){
        assertEquals(firulais.getTipo().toString(), "PERRO");
    }

    @Test
    public void unaMascotaSeLlamaComoSuNombre(){
        assertEquals(firulais.getNombre(), "Firulais");
    }

    @Test
    public void unaNuevaMascotaEsTipoRegistrada(){
        assertEquals(firulais.getEstado().toString(), "REGISTRADA");
    }

    private Duenio unDuenio() throws NoSuchAlgorithmException {
        Contacto contacto = new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com");
        List<Contacto> contactos = new ArrayList<>();
        contactos.add(contacto);
        return new Duenio("Pedro", "2021/05/03", TipoDocumento.DNI, 11111111, "username", "Mypass123$", contactos, new ArrayList<>());
    }

}
