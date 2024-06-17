package util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class Keys {

  public static String get(String key) {
    if (Objects.equals(key, "gemini")) {
      return "AIzaSyAylywSkfIpglOjxkRKsVd_VZS_h5J-rao";
    } else if(Objects.equals(key, "pico")){
      return "u2IdJ5z7zacX5h5uWJLL01HWeeIjvgFDFBdW+JOiFedkL2MiPpwB1w==";
    }
    return null;
  }
}
