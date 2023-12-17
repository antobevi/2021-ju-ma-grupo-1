import com.sun.jersey.api.client.ClientResponse;
import hogares.Hogar;
import hogares.apis.HogaresTransitoApi;

import hogares.apis.RespuestaHogaresTransito;
import hogares.apis.apiExceptions.HttpRequestMalHecha;
import mapa.Ubicacion;
import mascotas.TipoMascota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class HogaresTransitoApiTest {
  HogaresTransitoApi api;

  //Instancias
  Hogar h1 = new Hogar("Patitas de Perros", "+543512758195", new Ubicacion(-31.38476649512739, -64.22706150177592, ""), admisionDe(true, false), 50, 15, false, new ArrayList<>());
  Hogar h2 = new Hogar("Centro de Adopción IMUS", "+543415781449", new Ubicacion(-31.633451255888765, -60.719963486399, ""), admisionDe(false, true), 90, 20, false, new ArrayList<>());
  Hogar h3 = new Hogar("La Casa de las Mascotas", "+543425394576", new Ubicacion(-32.896460385589585, -60.718943486427726, ""), admisionDe(true, true), 40, 10, true, new ArrayList<>());
  Hogar h4 = new Hogar("Refugio de animales", "", new Ubicacion(-24.19500739359762, -65.29862185877765, ""), admisionDe(true, false), 25, 10, true, unasCaracteristicas("Amistoso", "Delgado", "Manso"));
  Hogar h5 = new Hogar("Hogar de Claudia", "+541147501122", new Ubicacion(-34.58134054856011, -58.53324792868844, ""), admisionDe(true, true), 80, 30, false, unasCaracteristicas("Manso"));
  private List<Hogar> pag1 = Arrays.asList(h1, h2);
  private List<Hogar> pag2 = Arrays.asList(h3, h4);
  private List<Hogar> pag3 = Arrays.asList(h5);
  private List<Hogar> listaDeHogaresDeLaApi = Arrays.asList(h1,h2,h3,h4,h5);

  @BeforeEach
  public void init() {
    api = mock(HogaresTransitoApi.class);

    when(api.getHogares()).thenCallRealMethod();
    when(api.getHogaresPorPagina(1)).thenReturn(pag1);
    when(api.getHogaresPorPagina(2)).thenReturn(pag2);
    when(api.getHogaresPorPagina(3)).thenReturn(pag3);
    when(api.getCantidadPaginas()).thenReturn(listaDeHogaresDeLaApi.size());
  }

  @Test
  public void devuelveTodosLosHogaresDeLasPaginas(){
    assertTrue(api.getHogares().containsAll(listaDeHogaresDeLaApi));
  }

  @Test
  public void siCambiaLaFormaDeHacerPeticionesFallaConUnaExcepcion(){
    when(api.getHogaresPorPagina(1)).thenCallRealMethod();

    ClientResponse respuesta = mock(ClientResponse.class);
    when(respuesta.getStatus()).thenReturn(400);
    when(api.requestHogaresPorPagina(1)).thenReturn(respuesta);

    assertThrows(HttpRequestMalHecha.class, () -> api.getHogaresPorPagina(1));
  }

  @Test
  public void devuelveLosHogares() {
    RespuestaHogaresTransito respuestaHogaresTransito = new RespuestaHogaresTransito(listaDeHogaresDeLaApi.size(), listaDeHogaresDeLaApi);

    assertEquals(respuestaHogaresTransito.getHogares(), listaDeHogaresDeLaApi);
  }

  @Test
  public void devuelveElTotalDeHogares() {
    RespuestaHogaresTransito respuestaHogaresTransito = new RespuestaHogaresTransito(listaDeHogaresDeLaApi.size(), listaDeHogaresDeLaApi);

    assertEquals(respuestaHogaresTransito.getTotal(), listaDeHogaresDeLaApi.size());
  }

  ///
  private Hogar unHogar(String nombre){
    return new Hogar(nombre, "+0", new Ubicacion(-31, -64), admisionDe(true, true), 40, 10, true, new ArrayList<>());
  }

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
