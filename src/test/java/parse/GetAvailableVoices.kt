package parse

import org.apache.logging.log4j.LogManager
import util.jAdapterForNativeTTS.engines.SpeechEngineNative

fun main() {
  SpeechEngineNative.getInstance().availableVoices.forEach {
    LogManager.getLogger().info(it)
  }
}