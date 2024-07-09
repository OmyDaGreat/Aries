package util

import org.apache.logging.log4j.LogManager
import java.nio.file.Files
import java.nio.file.Path

fun printFileContents(fileName: String?) {
  try {
    LogManager.getLogger().info("\n\n{}", Files.readString(fileName?.let {Path.of(it)}))
  } catch (e: Exception) {
    LogManager.getLogger().error("Failed to read file", e)
  }
}