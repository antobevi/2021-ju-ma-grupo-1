package hogares.apis;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import hogares.Hogar;
import hogares.apis.apiExceptions.*;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


public class HogaresTransitoApi {
  private Client client;
  private static final String API_URL = "https://api.refugiosdds.com.ar/api/hogares";
  private String TOKEN = "Bearer lr5LvKU9DVWyf6WzjU64LKW3EjxCwhw7DPxd0yA960PId9JfqDW5T7pP3a6Q";

  public HogaresTransitoApi() {
    this.client = Client.create();
  }

  public List<Hogar> getHogares() {
    List<Hogar> hogares = new ArrayList<>();
    for (int i = 1; i <= getCantidadPaginas(); i++)
      hogares.addAll(getHogaresPorPagina(i));
    return hogares;
  }

  public ClientResponse requestHogaresPorPagina(int numeroPagina) {
    String offset = Integer.toString(numeroPagina);
    WebResource recurso = this.client.resource(API_URL).queryParam("offset", offset);
    WebResource.Builder builder = recurso
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", TOKEN);

    return builder.get(ClientResponse.class);
  }

  public List<Hogar> getHogaresPorPagina(int pagina) {
    ClientResponse respuesta = requestHogaresPorPagina(pagina);
    if (respuesta.getStatus() >= 400)
      throw new HttpRequestMalHecha("Token invalido o la página pedida estaba fuera del rango aceptable");

    String json = respuesta.getEntity(String.class);
    List<Hogar> hogares = new Gson().fromJson(json, RespuestaHogaresTransito.class).getHogares();
    return hogares;
  }

  public int getCantidadPaginas() {
    ClientResponse respuesta = requestHogaresPorPagina(1);
    String json = respuesta.getEntity(String.class);
    int totalHogares = new Gson().fromJson(json, RespuestaHogaresTransito.class).getTotal();
    return (int) Math.ceil(totalHogares / 10.0);
  }
}
