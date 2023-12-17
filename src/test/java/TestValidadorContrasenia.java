import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import usuarios.*;
import usuarios.usuarioExceptions.EasyPasswordException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public class TestValidadorContrasenia {



  @Test
  void testCrearUsuarioPasswordFacil(){
    Assertions.assertThrows(EasyPasswordException.class,()-> new Admin("Dio", LocalDate.now(),TipoDocumento.DNI,1236548,"ZaWarudo","password"));
  }


  @Test
  public void testCrearUsuarioPasswordSegura() throws NoSuchAlgorithmException {
    Admin usuarioCreado = new Admin("Yu",LocalDate.now(),TipoDocumento.DNI,1236549,"Persona","Yagni3210+");
    Assertions.assertEquals(usuarioCreado.getUsername(),"Persona");
  }


  @Test
  public void testCrearUsuarioPasswordNoCumpleRequisitos(){
    Assertions.assertThrows(EasyPasswordException.class,()-> new Admin("Joker",LocalDate.now(),TipoDocumento.DNI,123987456,"Joker","asdfr4"));
  }



}
