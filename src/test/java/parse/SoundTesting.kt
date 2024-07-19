package parse

import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.*
import kotlin.math.abs

fun captureSound(durationMillis: Long): Triple<Int, Int, Double> {
  val format = AudioFormat(44100f, 16, 1, true, false)
  val info = DataLine.Info(TargetDataLine::class.java, format)
  val line = AudioSystem.getLine(info) as TargetDataLine
  line.open(format)
  line.start()

  val endTime = System.currentTimeMillis() + durationMillis
  val buffer = ByteArray(line.bufferSize / 5)
  val shortBuffer = ShortArray(buffer.size / 2)
  var maxAmplitude = Integer.MIN_VALUE
  var minAmplitude = Integer.MAX_VALUE
  var totalAmplitude = 0L
  var sampleCount = 0

  while (System.currentTimeMillis() < endTime) {
    val bytesRead = line.read(buffer, 0, buffer.size)
    if (bytesRead > 0) {
      ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortBuffer)
      shortBuffer.forEach { sample ->
        val amplitude = abs(sample.toInt())
        totalAmplitude += amplitude
        sampleCount++
        if (amplitude > maxAmplitude) {
          maxAmplitude = amplitude
        }
        if (amplitude < minAmplitude) {
          minAmplitude = amplitude
        }
      }
    }
  }

  line.stop()
  line.close()

  val averageAmplitude = if (sampleCount > 0) totalAmplitude.toDouble() / sampleCount else 0.0
  return Triple(maxAmplitude, minAmplitude, averageAmplitude)
}

fun main() {
  val (maxAmplitude, minAmplitude, averageAmplitude) = captureSound(20000)
  println("Max amplitude: $maxAmplitude")
  println("Min amplitude: $minAmplitude")
  println("Average amplitude: $averageAmplitude")
}