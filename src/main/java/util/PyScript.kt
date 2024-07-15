package util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException

object PyScript {
  private val log: Logger = LogManager.getLogger(PyScript::class.java)

  fun run() {
    try {
      val builder = ProcessBuilder("python", "src/util.main/java/util/pypkg/ai.py")
      builder.redirectErrorStream(true)
      val process = builder.start()

      process.inputStream.bufferedReader().use {reader ->
        var line: String
        while (reader.readLine().also {line = it} != null) {
          log.info(line)
        }
      }
      process.waitFor()
    } catch (e: IOException) {
      log.error("Failed to execute Python script", e)
    } catch (e: InterruptedException) {
      log.error("Interrupted while waiting for Python script to finish", e)
    }
  }
}
