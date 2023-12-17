import mapa.Ubicacion;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositorios.RepositorioAsociaciones;
import usuarios.Asociacion;

import static org.junit.jupiter.api.Assertions.*;

public class AsociacionesTest {

  @BeforeEach
  public void setUp() {
    RepositorioAsociaciones.getRepositorio().eliminarAsociacionesGuardadas();
  }

  @AfterAll
  public static void limpiarAsociaciones(){
    RepositorioAsociaciones.getRepositorio().eliminarAsociacionesGuardadas();
  }

  @Test
  public void asociacionEsLaMasCercanaSiHayUnaSola() {
    Asociacion asociacion = new Asociacion(new Ubicacion(-31.38476649512739, -64.22706150177592));
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion);
    //
    Asociacion asociacionMasCercana = RepositorioAsociaciones.getRepositorio().asociacionMasCercanaA(new Ubicacion(10, 10));
    //
    assertEquals(asociacion, asociacionMasCercana);
  }

  @Test
  public void entreVariasAsociacionesDevuelveLaMasCercana() {
    Asociacion asociacion1 = new Asociacion(new Ubicacion(-31.38476649512739, -64.22706150177592));
    Asociacion asociacion2 = new Asociacion(new Ubicacion(-80, -80));
    Asociacion asociacion3 = new Asociacion(new Ubicacion(80, 70));
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion1, asociacion2, asociacion3);
    //
    Asociacion asociacionMasCercana = RepositorioAsociaciones.getRepositorio().asociacionMasCercanaA(new Ubicacion(-30, -64));
    //
    assertEquals(asociacion1, asociacionMasCercana);
  }

  @Test
  public void entreVariasAsociacionesDevuelveTrueSiEsLaMasCercana() {
    Asociacion asociacion1 = new Asociacion(new Ubicacion(-31.38476649512739, -64.22706150177592));
    Asociacion asociacion2 = new Asociacion(new Ubicacion(-80, -80));
    Asociacion asociacion3 = new Asociacion(new Ubicacion(80, 70));
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion1, asociacion2, asociacion3);
    //
    assertTrue(RepositorioAsociaciones.getRepositorio().esLaAsociacionMasCercana(new Ubicacion(-30, -64), asociacion1));
  }

  @Test
  public void entreVariasAsociacionesDevuelveFalseSiNoEsLaMasCercana() {
    Asociacion asociacion1 = new Asociacion(new Ubicacion(-31.38476649512739, -64.22706150177592));
    Asociacion asociacion2 = new Asociacion(new Ubicacion(-80, -80));
    Asociacion asociacion3 = new Asociacion(new Ubicacion(80, 70));
    RepositorioAsociaciones.getRepositorio().agregarAsociaciones(asociacion1, asociacion2, asociacion3);
    //
    assertFalse(RepositorioAsociaciones.getRepositorio().esLaAsociacionMasCercana(new Ubicacion(-30, -64), asociacion3));
  }

  @Test
  public void asociacionPasadaEsLaMasCercanaSiNoHayNingunaGuardada() {
    Asociacion asociacion = new Asociacion(new Ubicacion(-31.38476649512739, -64.22706150177592));
    //
    assertTrue(RepositorioAsociaciones.getRepositorio().esLaAsociacionMasCercana(new Ubicacion(-30, -64), asociacion));
  }

}
