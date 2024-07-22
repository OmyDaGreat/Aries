package util

import lombok.experimental.UtilityClass
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import util.Platform.Companion.detectPlatform
import java.net.URISyntaxException
import java.util.*

@UtilityClass
object ResourcePath {
  private val log: Logger = LogManager.getLogger()

  @Throws(URISyntaxException::class)
  fun getResourcePath(path: String?): String {
    return Objects.requireNonNull(ResourcePath::class.java.classLoader.getResource(path)).toURI().toString().replace("file:/", "")
  }

  @JvmStatic
  fun main(args: Array<String>) {
    try {
      log.debug(
        getResourcePath(
          when(detectPlatform()) {
            Platform.WINDOWS -> "hey-parse_en_windows_v3_0_0.ppn"
            Platform.MAC -> "hey-parse_en_mac_v3_0_0.ppn"
            Platform.LINUX -> "hey-parse_en_linux_v3_0_0.ppn"
            else -> throw IllegalArgumentException("Platform not supported")
          }
        )
      )
    } catch (e: URISyntaxException) {
      e.printStackTrace()
    }
  }
}
