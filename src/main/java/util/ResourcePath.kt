package util

import java.io.File

/**
 * Utility object for handling resource paths.
 */
object ResourcePath {

  /**
   * Returns the local resource path for the given relative path.
   *
   * @param path The relative path to the resource.
   * @return The absolute path to the resource in the user's home directory.
   */
  fun getLocalResourcePath(path: String): String {
    return System.getProperty("user.home") + File.separator + "Aries" + File.separator + path
  }
}