package util.extension

import util.ResourcePath
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun copyFileIfNotExists(resourcePath: String, targetPath: String) {
  val targetFile = File(targetPath)
  if (!targetFile.exists()) {
    ResourcePath::class.java.classLoader.getResourceAsStream(resourcePath)?.let {
      Files.copy(it, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
  }
}