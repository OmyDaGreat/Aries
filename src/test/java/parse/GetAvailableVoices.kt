package parse

import org.apache.logging.log4j.LogManager
import io.github.jonelo.tts.engines.SpeechEngineNative

fun main() {
  SpeechEngineNative.getInstance().availableVoices.forEach {
    LogManager.getLogger().info(it)
  }
}