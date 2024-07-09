package parse

import io.github.jonelo.tts.engines.SpeechEngineNative
import org.apache.logging.log4j.LogManager

fun main() {
  SpeechEngineNative.getInstance().availableVoices.forEach {
    LogManager.getLogger().info(it)
  }
}