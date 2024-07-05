package util.extension;

import lombok.experimental.UtilityClass;

/**
 * Utility class for string extensions.
 * Provides additional methods for string manipulation and checks.
 */
@UtilityClass
public class StringExtension {
  /**
   * Checks if the original string contains any of the provided strings.
   * This method performs a case-sensitive check to determine if any of the specified
   * strings are contained within the original string.
   *
   * @param string  The original string to check.
   * @param strings An array of strings to check against the original string.
   * @return True if the original string contains any of the provided strings, false otherwise.
   */
  public static boolean containsAny(String string, String... strings) {
    for (String s : strings) {
      if (string.contains(s)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the original string contains any of the provided strings, ignoring case.
   * This method converts the original string to lowercase before performing the check,
   * making the comparison case-insensitive.
   *
   * @param string  The original string to check, case-insensitively.
   * @param strings An array of strings to check against the original string, case-insensitively.
   * @return True if the original string contains any of the provided strings, false otherwise.
   */
  public static boolean trueContainsAny(String s, String... strings) {
    s = s.toLowerCase();
    for (String string : strings) {
      if (s.contains(string.toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the original string contains the provided string, ignoring case.
   * This method performs a case-insensitive comparison between the original string
   * and the provided string.
   *
   * @param s      The original string to check, case-insensitively.
   * @param string The string to check against the original string, case-insensitively.
   * @return True if the original string contains the provided string, false otherwise.
   */
  public static boolean trueContains(String s, String string) {
    return s.toLowerCase().contains(string.toLowerCase());
  }
}