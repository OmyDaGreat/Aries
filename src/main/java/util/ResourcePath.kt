package util

import java.net.URISyntaxException
import java.util.*

object ResourcePath {
  @JvmStatic
  @Throws(URISyntaxException::class)
  fun getResourcePath(path: String): String {
    return ResourcePath::class.java.classLoader.getResource(path)?.toURI()?.path?.replaceFirst("/","") ?: ""
  }
}
