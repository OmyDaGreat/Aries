package util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class Keys {

  public static String get(String key) {
    if (Objects.equals(key, "gemini")) {
      return "AIzaSyAylywSkfIpglOjxkRKsVd_VZS_h5J-rao";
    } else if(Objects.equals(key, "pico")){
      return "1xP2gVkGyADXxDV5GzlCfJP7GH1c0EktDUpv7TwTMlnkd6+Z1Vs/EA==";
    }
    return null;
  }
}
