package aries.audio

import util.audio.NativeTTS
import javax.sound.sampled.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.system.exitProcess

class Recorder(audioDeviceIndex: Int) : Thread() {
  private var micDataLine: TargetDataLine? = null
  private val pcmBuffer = mutableListOf<Short>()
  private var stop = false

  init {
    val format = AudioFormat(16000f, 16, 1, true, false)
    val dataLineInfo = DataLine.Info(TargetDataLine::class.java, format)

    try {
      micDataLine = getAudioDevice(audioDeviceIndex, dataLineInfo)?.apply { open(format) }
        ?: throw LineUnavailableException("Failed to initialize audio device")
    } catch (e: LineUnavailableException) {
      NativeTTS.tts("There is no available microphone.")
      exitProcess(1)
    }
  }

  private fun getDefaultCaptureDevice(dataLineInfo: DataLine.Info): TargetDataLine? {
    return if (AudioSystem.isLineSupported(dataLineInfo)) {
      AudioSystem.getLine(dataLineInfo) as? TargetDataLine
    } else {
      null
    }
  }

  private fun getAudioDevice(deviceIndex: Int, dataLineInfo: DataLine.Info): TargetDataLine? {
    return if (deviceIndex >= 0) {
      try {
        val mixerInfo = AudioSystem.getMixerInfo()[deviceIndex]
        val mixer = AudioSystem.getMixer(mixerInfo)
        if (mixer.isLineSupported(dataLineInfo)) {
          mixer.getLine(dataLineInfo) as? TargetDataLine
        } else {
          null
        }
      } catch (e: LineUnavailableException) {
        e.printStackTrace()
        null
      }
    } else {
      getDefaultCaptureDevice(dataLineInfo)
    }
  }

  override fun run() {
    micDataLine?.let { line ->
      line.start()

      val captureBuffer = ByteBuffer.allocate(512).apply { order(ByteOrder.LITTLE_ENDIAN) }
      val shortBuffer = ShortArray(256)

      while (!stop) {
        line.read(captureBuffer.array(), 0, captureBuffer.capacity())
        captureBuffer.asShortBuffer()[shortBuffer]
        pcmBuffer.addAll(shortBuffer.toList())
      }
    }
  }

  fun end() {
    stop = true
    micDataLine?.close()
  }

  val pcm: ShortArray
    get() = ShortArray(pcmBuffer.size) { index -> pcmBuffer[index] }
}
