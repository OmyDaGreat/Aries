package util;

import lombok.Getter;

@Getter
public enum Platform {
  WINDOWS11,
  WINDOWS10,
  WINDOWS7,
  LINUX,
  MACOSX,
  UNKNOWN;

  public static Platform detectPlatform() {
    if(System.getProperty("os.name").contains("Windows 11")) {
      return WINDOWS11;
    } else if(System.getProperty("os.name").contains("Windows 10")) {
      return WINDOWS10;
    } else if(System.getProperty("os.name").contains("Windows 7")) {
      return WINDOWS7;
    } else if(System.getProperty("os.name").contains("Linux")) {
      return LINUX;
    } else if(System.getProperty("os.name").contains("Mac OS X")) {
      return MACOSX;
    }
    return UNKNOWN;
  }
}