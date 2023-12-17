import hogares.Hogar;
import hogares.apis.HogaresTransitoApi;

import hogares.apis.ServicioHogares;
import hogares.apis.filtrosHogar.*;
import mapa.Ubicacion;
import mascotas.TipoMascota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class ServicioHogaresTest {

  private HogaresTransitoApi api;
  private ServicioHogares servicio;

  //Instancias
  Hogar h1 = new Hogar("Patitas de Perros", "+543512758195", new Ubicacion(-31.38476649512739, -64.22706150177592, ""), admisionDe(true, false), 50, 15, false, new ArrayList<>());
  Hogar h2 = new Hogar("Centro de Adopción IMUS", "+543415781449", new Ubicacion(-31.633451255888765, -60.719963486399, ""), admisionDe(false, true), 90, 20, false, new ArrayList<>());
  Hogar h3 = new Hogar("La Casa de las Mascotas", "+543425394576", new Ubicacion(-32.896460385589585, -60.718943486427726, ""), admisionDe(true, true), 40, 10, true, new ArrayList<>());
  Hogar h4 = new Hogar("Refugio de animales", "", new Ubicacion(-24.19500739359762, -65.29862185877765, ""), admisionDe(true, false), 25, 10, true, unasCaracteristicas("Amistoso", "Delgado", "Manso"));
  Hogar h5 = new Hogar("Hogar de Claudia", "+541147501122", new Ubicacion(-34.58134054856011, -58.53324792868844, ""), admisionDe(true, true), 80, 30, false, unasCaracteristicas("Manso"));
  Hogar h6 = new Hogar("Hogar de Proteccion Lourdes", "+541151835168", new Ubicacion(-34.61657666535632, -58.38580212945297, ""), admisionDe(true, true), 22, 0, true, new ArrayList<>());
  private List<Hogar> listaDeHogaresDeLaApi = Arrays.asList(h1, h2, h3, h4, h5, h6);

  @BeforeEach
  public void init() {
    api = mock(HogaresTransitoApi.class);
    servicio = new ServicioHogares(api);

    when(api.getHogares()).thenReturn(listaDeHogaresDeLaApi);
  }

  @Test
  public void pedirHogaresSinFiltroDevuelveTodosLosHogares() {
    assertEquals(listaDeHogaresDeLaApi, servicio.getHogares());
  }

  @Test
  public void sePuedePedirHogaresQueTenganPatio() {
    FiltroHogar filtroTenerPatio = new FiltroPorPatio(true);
    assertEquals(Arrays.asList(h3, h4, h6), servicio.getHogares(filtroTenerPatio));
  }

  @Test
  public void sePuedePedirHogaresQueNoTenganPatio() {
    FiltroHogar filtroTenerPatio = new FiltroPorPatio(false);
    assertEquals(Arrays.asList(h1, h2, h5), servicio.getHogares(filtroTenerPatio));
  }

  @Test
  public void sePuedePedirHogaresQueTenganUnaCaracteristica() {
    FiltroHogar filtroSerAmistoso = new FiltroPorCaracteristicas("Manso");
    assertEquals(Arrays.asList(h4, h5), servicio.getHogares(filtroSerAmistoso));
  }

  @Test
  public void sePuedePedirHogaresQueTenganDeterminadasCaracteristicas() {
    FiltroHogar filtroSerAmistoso = new FiltroPorCaracteristicas("Amistoso", "Manso");
    assertEquals(Arrays.asList(h4), servicio.getHogares(filtroSerAmistoso));
  }

  @Test
  public void sePuedePedirHogaresQueAdmitanPerros() {
    FiltroHogar filtroAceptanPerros = new FiltroPorTipoMascota(TipoMascota.PERRO);
    assertEquals(Arrays.asList(h1, h3, h4, h5, h6), servicio.getHogares(filtroAceptanPerros));
  }

  @Test
  public void sePuedePedirHogaresQueAdmitanGatos() {
    FiltroHogar filtroAceptanPerros = new FiltroPorTipoMascota(TipoMascota.GATO);
    assertEquals(Arrays.asList(h2, h3, h5, h6), servicio.getHogares(filtroAceptanPerros));
  }

  @Test
  public void sePuedePedirHogaresQueAdmitanPerrosYGatos() {
    FiltroHogar filtroAceptanPerrosYGatos = new FiltroPorTipoMascota(TipoMascota.GATO, TipoMascota.PERRO);
    assertEquals(Arrays.asList(h3, h5, h6), servicio.getHogares(filtroAceptanPerrosYGatos));
  }

  @Test
  public void sePuedePedirHogaresQueTenganAlMenosUnEspacio() {
    FiltroHogar filtroCapacidad = new FiltroPorCapacidad(1);
    assertEquals(Arrays.asList(h1, h2, h3, h4, h5), servicio.getHogares(filtroCapacidad));
  }

  @Test
  public void sePuedePedirHogaresPorCercania() {
    Ubicacion unaUbicacion = new Ubicacion(-31.385, -60.719);
    FiltroHogar filtroCercania = new FiltroPorCercania(unaUbicacion, 0.5);
    assertEquals(Arrays.asList(h4, h6), servicio.getHogares(filtroCercania));
  }

  @Test
  public void sePuedePedirHogaresConMasDeUnFiltro() {
    FiltroHogar filtroAceptanPerros = new FiltroPorTipoMascota(TipoMascota.PERRO);
    FiltroHogar filtroSerAmistoso = new FiltroPorCaracteristicas("Manso");
    assertEquals(Arrays.asList(h4, h5), servicio.getHogares(filtroAceptanPerros, filtroSerAmistoso));
  }

  ///
  private Map<TipoMascota, Boolean> admisionDe(Boolean perro, Boolean gato) {
    Map<TipoMascota, Boolean> admision = new HashMap<>();
    admision.put(TipoMascota.GATO, gato);
    admision.put(TipoMascota.PERRO, perro);
    return admision;
  }

  private List<String> unasCaracteristicas(String... caracteristicas) {
    return Arrays.asList(caracteristicas);
  }
}
