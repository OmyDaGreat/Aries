package util.audio

import ai.picovoice.leopard.Leopard
import org.apache.logging.log4j.LogManager
import util.Keys.get
import util.ResourcePath.getResourcePath
import util.extension.trueContainsAny
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.Duration
import java.time.Instant
import javax.sound.sampled.*
import kotlin.math.abs

fun processAudio(keywordDetected: () -> Unit, onSilence: () -> Unit, isRecording: () -> Boolean) {
  val info = DataLine.Info(TargetDataLine::class.java, null)
  if (!AudioSystem.isLineSupported(info)) {
    throw LineUnavailableException("No line matching provided info found.")
  }
  val leopard = Leopard.Builder().setAccessKey(get("pico")).setModelPath(getResourcePath("Aries.pv")).build()
  val line = AudioSystem.getLine(info) as TargetDataLine
  val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000F, 16, 1, 2, 16000F, false)
  line.open(format)
  line.start()
  val log = LogManager.getLogger()
  val buffer = ShortArray(33332) // 20,000 is too little; 40,000 is too much: slow to process
  val byteBuffer = ByteArray(buffer.size * 2)
  var silenceFrames: Instant? = null
  val amplitudeThreshold = 1000

  while (true) {
    if (isRecording()) {
      if (isSilence(buffer, amplitudeThreshold)) {
        log.debug("Silent...")
        if (Duration.between(silenceFrames ?: Instant.now(), Instant.now()).toSeconds() >= 7) {
          onSilence()
          line.open(format)
          line.start()
        }
      } else {
        log.debug("Sound detected")
        silenceFrames = Instant.now()
      }
    } else {
      val bytesRead = line.read(byteBuffer, 0, byteBuffer.size)
      if (bytesRead <= 0) continue
      log.debug("Bytes read: $bytesRead")
      ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()[buffer]

      val conversion = convertAudioToText(buffer, leopard)

      log.debug("Conversion: $conversion")

      if (conversion.trueContainsAny("Hey Aries", "Aries", "Harry")) {
        line.close()
        keywordDetected()
        silenceFrames = Instant.now()
      }
    }
  }
}

/**
 * Determines if a given audio buffer contains silence.
 * Silence is defined as having an average amplitude below a specified threshold.
 *
 * @param buffer The audio buffer to analyze.
 * @param threshold The amplitude threshold below which the audio is considered silent.
 * @return True if the buffer is considered silent, false otherwise.
 */
fun isSilence(buffer: ShortArray, threshold: Int): Boolean {
  var sum = 0.0
  for (short in buffer) sum += abs(short.toInt())
  val average = sum / buffer.size
  return average < threshold
}

fun convertAudioToText(buffer: ShortArray?, leopard: Leopard): String {
  val log = LogManager.getLogger()
  try {
    log.info("Processing audio buffer of size ${buffer?.size ?: "null"}")
    return leopard.process(buffer).transcriptString
  } catch (e: Exception) {
    log.error("Error processing audio: ${e.message}")
    return ""
  }
}
