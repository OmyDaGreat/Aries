package util.extension

import util.ResourcePath
import util.extension.RobotUtils.log
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun copyFileIfNotExists(resourcePath: String, targetPath: String) {
  val targetFile = File(targetPath)
  if (!targetFile.exists()) {
    ResourcePath::class.java.classLoader.getResourceAsStream(resourcePath)?.let {
      Files.copy(it, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
      log.debug("File copied to $targetPath")
    } ?: run {
      log.error("Resource not found: $resourcePath")
    }
  } else {
    log.debug("File already exists at $targetPath")
  }
}