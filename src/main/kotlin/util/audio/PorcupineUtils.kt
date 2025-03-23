package util.audio

import ai.picovoice.leopard.Leopard
import aries.audio.LiveMic.Companion.leopardBuild
import util.extension.trueContains
import java.nio.ByteBuffer
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.time.Duration
import java.time.Instant
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.TargetDataLine
import kotlin.math.abs

/**
 * Configuration for audio processing.
 */
class AudioProcessingConfig {
    var onKeywordDetected: () -> Unit = {}
    var onSilence: () -> Unit = {}
    var isRecording: () -> Boolean = { false }
}

/**
 * Processes audio input, detecting keywords and handling silence.
 *
 * @param block Configuration for audio processing.
 * @throws LineUnavailableException If no matching audio line is found.
 */
fun processAudio(block: AudioProcessingConfig.() -> Unit) {
    AudioProcessingConfig().apply(block).apply {
        val info = DataLine.Info(TargetDataLine::class.java, null)
        if (!AudioSystem.isLineSupported(info)) {
            throw LineUnavailableException("No line matching provided info found.")
        }
        val leopard = leopardBuild.build()
        val line = AudioSystem.getLine(info) as TargetDataLine
        val format = AudioFormat(PCM_SIGNED, 16000F, 16, 1, 2, 16000F, false)
        line.open(format)
        line.start()
        val buffer = ShortArray(33332)
        val byteBuffer = ByteArray(buffer.size * 2)
        var silenceFrames: Instant? = null

        while (true) {
            if (isRecording()) {
                handleRecording(buffer, silenceFrames, onSilence, line, format) {
                    silenceFrames = it
                }
            } else {
                handleNonRecording(byteBuffer, buffer, line, leopard, onKeywordDetected)
            }
        }
    }
}

/**
 * Handles the recording state, checking for silence and invoking the appropriate callback.
 *
 * @param buffer The audio buffer to analyze.
 * @param silenceFrames The timestamp of the last detected silence frame.
 * @param onSilence Callback function to be invoked when silence is detected.
 * @param line The target data line for audio input.
 * @param format The audio format.
 * @param setSilence Function to update the timestamp of the last detected silence frame.
 */
private fun handleRecording(
    buffer: ShortArray,
    silenceFrames: Instant?,
    onSilence: () -> Unit,
    line: TargetDataLine,
    format: AudioFormat,
    setSilence: (Instant) -> Unit,
) {
    if (buffer.isSilence(1000)) {
        if (Duration.between(silenceFrames ?: Instant.now(), Instant.now()).toSeconds() >= 7) {
            onSilence()
            line.open(format)
            line.start()
        }
    } else {
        setSilence(Instant.now())
    }
}

/**
 * Handles the non-recording state, reading audio data and detecting keywords.
 *
 * @param byteBuffer The byte buffer for audio data.
 * @param buffer The audio buffer to analyze.
 * @param line The target data line for audio input.
 * @param leopard The Leopard instance for audio processing.
 * @param keywordDetected Callback function to be invoked when a keyword is detected.
 */
private fun handleNonRecording(
    byteBuffer: ByteArray,
    buffer: ShortArray,
    line: TargetDataLine,
    leopard: Leopard,
    keywordDetected: () -> Unit,
) {
    val bytesRead = line.read(byteBuffer, 0, byteBuffer.size)
    if (bytesRead <= 0) return
    ByteBuffer.wrap(byteBuffer).order(LITTLE_ENDIAN).asShortBuffer()[buffer]

    val conversion = leopard.convertAudioToText(buffer)

    if (conversion.trueContains("Hey Aries", "Aries", "Harry")) {
        line.close()
        keywordDetected()
    }
}

/**
 * Checks if the audio buffer contains silence.
 *
 * @param threshold The threshold for silence detection.
 * @return True if the buffer contains silence, false otherwise.
 */
fun ShortArray.isSilence(threshold: Int): Boolean {
    var sum = 0.0
    for (short in this) sum += abs(short.toInt())
    val average = sum / size
    return average < threshold
}

/**
 * Converts audio data to text using the Leopard instance.
 *
 * @param buffer The audio buffer to convert.
 * @param this@convertAudioToText The Leopard instance for audio processing.
 * @return The transcribed text.
 */
fun Leopard.convertAudioToText(buffer: ShortArray?): String =
    try {
        process(buffer).transcriptString
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
