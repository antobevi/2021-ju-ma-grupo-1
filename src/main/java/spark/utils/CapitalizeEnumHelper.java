package spark.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public enum CapitalizeEnumHelper implements Helper<Enum> {

  capitalizeEnum {
    @Override
    public CharSequence apply(Enum arg0, Options arg1) throws IOException {
      if (arg0.toString().length() > 0){
        return arg0.toString().substring(0, 1).toUpperCase() + arg0.toString().substring(1).toLowerCase();
      } else {
        return arg0.toString();
      }
    }
  }

}