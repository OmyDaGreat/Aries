package parse

import ai.picovoice.porcupine.Porcupine
import ai.picovoice.porcupine.PorcupineException
import org.apache.logging.log4j.LogManager
import util.Keys.get
import util.Platform
import util.ResourcePath.getResourcePath
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.*

fun main() {
  val log = LogManager.getLogger()
  val porcupine = Porcupine.Builder().setAccessKey(get("pico")).setKeywordPath(
    getResourcePath(
      "Hey-parse-me_en_" +
              when (Platform.detectPlatform()) {
                Platform.WINDOWS -> "windows"
                Platform.MAC -> "mac"
                Platform.LINUX -> "linux"
                else -> throw IllegalArgumentException("Platform not supported")
              }
              + "_v3_0_0.ppn").replace("file:/", "")
  ).build()
  try {
    log.debug(porcupine.sampleRate)
    log.debug(porcupine.frameLength)
    val info = DataLine.Info(TargetDataLine::class.java, null)
    if (!AudioSystem.isLineSupported(info)) {
      throw LineUnavailableException("No line matching provided info found.")
    }
    val line = AudioSystem.getLine(info) as TargetDataLine
    val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, porcupine.sampleRate.toFloat(), 16, 1, 2, porcupine.sampleRate.toFloat(), false)
    line.open(format)
    line.start()

    val buffer = ShortArray(porcupine.frameLength)
    val byteBuffer = ByteArray(buffer.size * 2)
    while (true) {
      val bytesRead = line.read(byteBuffer, 0, byteBuffer.size)
      if (bytesRead > 0) {
        ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(buffer)
        try {
          val keywordIndex: Int = porcupine.process(buffer)
          if (keywordIndex >= 0) {
            log.info("Keyword detected!")
          }
        } catch (e: PorcupineException) {
          System.err.println(e)
        }
      }
    }
  } catch (e: LineUnavailableException) {
    System.err.println(e)
  } catch (e: IOException) {
    System.err.println(e)
  } finally {
    porcupine.delete()
  }
}