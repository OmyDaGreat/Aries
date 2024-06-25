package util.Extension;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringExtension {
  /**
   * Checks if the original string contains any of the provided strings
   *
   * @param string Static call
   * @param strings Strings to be checked
   * @return True if the original string contains any of the provided strings
   */
  public static boolean contains(String string, String... strings) {
    for (String s : strings) {
      if (string.contains(s)) {
        return true;
      }
    }
    return false;
  }
}
