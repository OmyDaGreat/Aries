package util.extension

import util.extension.RobotUtils.special

/**
 * Extension function to check if the string contains any of the provided strings.
 *
 * @param strings Vararg of strings to check for containment.
 * @return True if any of the provided strings are contained in the string, false otherwise.
 */
fun String.trueContains(vararg strings: String): Boolean {
  return strings.any { this.trueContainsSingle(it) }
}

/**
 * Helper function to check if the string contains the provided string, considering word boundaries and case insensitivity.
 *
 * @param string The string to check for containment.
 * @return True if the string contains the provided string, false otherwise.
 */
fun String.trueContainsSingle(string: String): Boolean {
  val originalWords = this.trim().split("\\s+".toRegex())
  val inputWords = string.trim().split("\\s+".toRegex())

  if (inputWords.size > originalWords.size) return false

  return inputWords.indices.all { originalWords[it].equals(inputWords[it], ignoreCase = true) }
}

/**
 * Extension function to replace special characters in the string based on a predefined map.
 *
 * @return The string with special characters replaced.
 */
fun String.replaceSpecial(): String {
  var result = this
  for ((key, _) in special) {
    result = result.replace(key.first, key.second.toString())
  }
  return result.replace("ten", "10").replace("eleven", "11").replace("twelve", "12")
}

/**
 * Extension function to remove all occurrences of the provided strings from the string.
 *
 * @param string Vararg of strings to remove.
 * @param ignoreCase Boolean flag to indicate if the removal should be case insensitive.
 * @return The string with all occurrences of the provided strings removed.
 */
fun String.remove(vararg string: String, ignoreCase: Boolean = true): String {
  var result = this
  for (s in string) {
    result = result.replace(s, "", ignoreCase)
  }
  return result
}