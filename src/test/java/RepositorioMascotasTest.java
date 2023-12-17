import mascotas.Mascota;
import mascotas.MascotaBuilder;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import repositorios.RepositorioMascotas;
import usuarios.Contacto;
import usuarios.Duenio;
import usuarios.TipoDocumento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositorioMascotasTest extends AbstractPersistanceUpdatedTest implements WithGlobalEntityManager {

  @Test
  public void persistirUnaMascotaYRecuperarlaPorIdDeberiaDarLaMismaMascota() {
    List<Contacto> contactos = Arrays.asList(new Contacto("Juan", "Perez", "1112345678", "juanperez@gmail.com"));
    Duenio duenio = new Duenio("Pedro", "2021/05/03", TipoDocumento.DNI, 11111111, "username", "Mypass123$", contactos, new ArrayList<>());

    Mascota mascota = new MascotaBuilder().perroConNombreYDuenio("FirulaisTest", duenio).build();

    withTransaction(() -> RepositorioMascotas.getInstance().agregarMascota(mascota));


    Mascota mascotaPersistida = RepositorioMascotas.getInstance().getMascotaById(mascota.getId());

    assertEquals("FirulaisTest", mascotaPersistida.getNombre());
  }

}
