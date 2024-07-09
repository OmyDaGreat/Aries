package util.extension

/**
 * Checks if the original string contains any of the provided strings.
 * This method performs a case-sensitive check to determine if any of the specified
 * strings are contained within the original string.
 *
 * @param strings An array of strings to check against the original string.
 * @return True if the original string contains any of the provided strings, false otherwise.
 */
fun String.containsAny(vararg strings: String?): Boolean {
  for (s in strings) {
    if (contains(s!!)) {
      return true
    }
  }
  return false
}

/**
 * Checks if the original string contains any of the provided strings, ignoring case.
 * This method converts the original string to lowercase before performing the check,
 * making the comparison case-insensitive.
 *
 * @param strings An array of strings to check against the original string, case-insensitively.
 * @return True if the original string contains any of the provided strings, false otherwise.
 */
fun String.trueContainsAny(vararg strings: String): Boolean {
  for (string in strings) {
    if (lowercase().contains(string.lowercase())) {
      return true
    }
  }
  return false
}

/**
 * Checks if the original string contains the provided string, ignoring case.
 * This method performs a case-insensitive comparison between the original string
 * and the provided string.
 *
 * @param string The string to check against the original string, case-insensitively.
 * @return True if the original string contains the provided string, false otherwise.
 */
fun String.trueContains(string: String): Boolean {
  return lowercase().contains(string.lowercase())
}