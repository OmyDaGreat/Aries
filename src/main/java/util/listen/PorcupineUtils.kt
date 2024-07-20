package util.listen

import ai.picovoice.porcupine.Porcupine
import ai.picovoice.porcupine.PorcupineException
import org.apache.logging.log4j.LogManager
import util.Keys.get
import util.Platform
import util.ResourcePath.getResourcePath
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.Duration
import java.time.Instant
import javax.sound.sampled.*
import kotlin.math.abs

/**
 * Utility functions for working with the Porcupine voice recognition library.
 */

/**
 * Creates a Porcupine instance with "Hey-parse-me" as its keyword.
 * The keyword file is selected based on the operating system.
 *
 * @return A new Porcupine instance configured with the access key and keyword path.
 * @throws IllegalArgumentException If the platform is not supported.
 */
fun createPorcupine(): Porcupine {
  return Porcupine.Builder().setAccessKey(get("pico")).setKeywordPath(
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
}

/**
 * Opens an audio line for the Porcupine instance to process audio.
 * The audio format is set to PCM_SIGNED with the sample rate and frame size
 * matching the Porcupine instance's requirements.
 *
 * @param porcupine The Porcupine instance for which the audio line is opened.
 * @return The opened TargetDataLine ready for audio input.
 * @throws LineUnavailableException If no matching audio line is found.
 */
fun openAudioLine(porcupine: Porcupine): TargetDataLine {
  val info = DataLine.Info(TargetDataLine::class.java, null)
  if (!AudioSystem.isLineSupported(info)) {
    throw LineUnavailableException("No line matching provided info found.")
  }
  val line = AudioSystem.getLine(info) as TargetDataLine
  val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, porcupine.sampleRate.toFloat(), 16, 1, 2, porcupine.sampleRate.toFloat(), false)
  line.open(format)
  line.start()
  return line
}

/**
 * Processes audio input from the TargetDataLine and detects keywords using the Porcupine instance.
 * It also detects periods of silence based on a defined threshold and triggers an event when silence is detected.
 *
 * @param line The audio line from which audio data is read.
 * @param porcupine The Porcupine instance used to process the audio data.
 * @param keywordDetected A lambda function to execute when a keyword is detected.
 * @param onSilence A lambda function to execute when silence is detected for a defined duration.
 */
fun processAudio(line: TargetDataLine, porcupine: Porcupine, keywordDetected: () -> Unit, onSilence: () -> Unit, isRecording: () -> Boolean) {
  val log = LogManager.getLogger()
  val buffer = ShortArray(porcupine.frameLength)
  val byteBuffer = ByteArray(buffer.size * 2)
  var silenceFrames: Instant? = null
  val amplitudeThreshold = 1000 // Amplitude threshold to consider as silence, adjust based on your needs

  while (true) {
    if (isRecording()) {
      if (isSilence(buffer, amplitudeThreshold)) {
        log.debug("Silent...")
        if (Duration.between(silenceFrames ?: Instant.now(), Instant.now()).toSeconds() >= 3) {
          onSilence()
        }
      } else {
        log.debug("Sound detected")
        silenceFrames = Instant.now()
      }
    } else {
      val bytesRead = line.read(byteBuffer, 0, byteBuffer.size)
      if (bytesRead <= 0) continue
      ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()[buffer]

      try {
        val keywordIndex: Int = porcupine.process(buffer)
        if (keywordIndex >= 0) {
          keywordDetected()
          silenceFrames = Instant.now()
        }
      } catch (e: PorcupineException) {
        e.printStackTrace()
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

/**
 * The main function to demonstrate the usage of Porcupine voice recognition.
 * It initializes the Porcupine instance, opens an audio line, and starts processing audio input.
 */
fun main() {
  val log = LogManager.getLogger()
  val porcupine = createPorcupine()
  try {
    val line = openAudioLine(porcupine)
    processAudio(line, porcupine, {
      log.info("Keyword detected")
    }, {
      log.info("Silence detected")
    }) {
      true
    }
  } catch (e: Exception) {
    log.error("Error: {}", e.message)
  } finally {
    porcupine.delete()
  }
}