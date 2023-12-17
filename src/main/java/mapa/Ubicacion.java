package mapa;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Embeddable
public class Ubicacion {

  @SerializedName("lat")
  private double latitud;
  @SerializedName("long")
  private double longitud;
  private String direccion;


  public Ubicacion(){}


  public Ubicacion(double latitud, double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
    this.direccion = "";
  }

  public Ubicacion(double latitud, double longitud, String direccion) {
    this.latitud = latitud;
    this.longitud = longitud;
    this.direccion = direccion;
  }

  public double getLatitud() {
    return latitud;
  }

  public void setLatitud(double latitud) {
    this.latitud = latitud;
  }

  public double getLongitud() {
    return longitud;
  }

  public void setLongitud(double longitud) {
    this.longitud = longitud;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  // Genera una circunferencia y de ahi devuelve la distnacia entre esos 2 puntos.
  public double distancia(Ubicacion otraUbicacion) {
    double theta = this.longitud - otraUbicacion.getLongitud();
    double dist = Math.sin(deg2rad(this.latitud)) * Math.sin(deg2rad(otraUbicacion.getLatitud())) +
        Math.cos(deg2rad(this.latitud)) * Math.cos(deg2rad(otraUbicacion.getLatitud())) *
        Math.cos(rad2deg(theta));
    return dist;
  }

  public double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  public double rad2deg(double rad) {
    return (rad * 180.0 / Math.PI);
  }

  public double otraDistancia(Ubicacion otraUbicacion){
      double radioTierra = 6371;//en kilómetros
      double dLat = Math.toRadians(otraUbicacion.latitud - this.latitud);
      double dLng = Math.toRadians(otraUbicacion.longitud - this.longitud);
      double sindLat = Math.sin(dLat / 2);
      double sindLng = Math.sin(dLng / 2);
      double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
          * Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(otraUbicacion.latitud));
      double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
      double distancia = radioTierra * va2;

      return distancia;
  }
}
