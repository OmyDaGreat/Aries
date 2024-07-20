package parse

import ai.picovoice.leopard.Leopard
import org.apache.logging.log4j.LogManager
import util.Keys.get
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.*

// Assuming other necessary imports are here

fun processAudio() {
  val info = DataLine.Info(TargetDataLine::class.java, null)
  if (!AudioSystem.isLineSupported(info)) {
    throw LineUnavailableException("No line matching provided info found.")
  }
  val line = AudioSystem.getLine(info) as TargetDataLine
  val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000F, 16, 1, 2, 16000F, false)
  line.open(format)
  line.start()
  val log = LogManager.getLogger()
  val buffer = ShortArray(512)
  val byteBuffer = ByteArray(buffer.size * 2)
  val amplitudeThreshold = 1000 // Amplitude threshold to consider as silence, adjust based on your needs

  while (true) {
    val bytesRead = line.read(byteBuffer, 0, byteBuffer.size)
    if (bytesRead <= 0) continue
    ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(buffer)

    if (convertAudioToText(buffer).contains("Hey Aries")) {
      keywordDetected()
    }
  }
}

fun convertAudioToText(buffer: ShortArray?): String {
  return Leopard.Builder().setAccessKey(get("pico")).build().process(buffer).transcriptString
}

fun main() {
  processAudio()
}
