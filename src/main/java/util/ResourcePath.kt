package util

import java.io.File

object ResourcePath {
  fun getResourcePath(path: String): String {
    return ResourcePath::class.java.classLoader.getResource(path)?.toURI()?.path?.replaceFirst("/","") ?: ""
  }

  fun getLocalResourcePath(path: String): String {
    return System.getProperty("user.home") + File.separator + "ParseButPro" + File.separator + path
  }
}
