package util;

import lombok.Getter;
import org.apache.commons.lang3.SystemUtils;

@Getter
public enum Platform {
  WINDOWS("Windows"),
  LINUX("Linux"),
  MAC("Mac"),
  UNKNOWN("Unknown");

  final String fullName;
  final String displayName;

  Platform(String displayName) {
    this.displayName = displayName;
    fullName = System.getProperty("os.name");
  }

  public static Platform detectPlatform() {
    if (SystemUtils.IS_OS_WINDOWS) {
      return WINDOWS;
    } else if (SystemUtils.IS_OS_LINUX) {
      return LINUX;
    } else if (SystemUtils.IS_OS_MAC) {
      return MAC;
    }
    return UNKNOWN;
  }
}
