package mediosDeContacto;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
  private static ServiceLocator instance = new ServiceLocator();
  Map<Class,Object> servicios = new HashMap<>();

  public void registrar(Class clazz, Object instance) {
    servicios.put(clazz, instance);
  }

  public Object get(Class clazz) {
    return servicios.get(clazz);
  }

  public static ServiceLocator getServiceLocator() {
    return instance;
  }

  // Para testing
  public void eliminarServicios(){
    this.servicios = new HashMap<>();
  }
}
