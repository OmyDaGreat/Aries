package util.audio

import io.github.jonelo.tts.engines.SpeechEngineNative
import io.github.jonelo.tts.engines.VoicePreferences
import io.github.jonelo.tts.engines.VoicePreferences.Gender
import io.github.jonelo.tts.engines.exceptions.SpeechEngineCreationException
import org.apache.logging.log4j.LogManager
import parse.audio.LiveMic
import util.ResourcePath.getResourcePath
import java.io.*
import java.net.URISyntaxException

/**
 * Utility class for text-to-speech (TTS) functionalities.
 * This class provides methods to convert text into speech using the native speech engine.
 */
class NativeTTS {

  companion object {
    val voicePreferences: VoicePreferences = VoicePreferences()
    private val log = LogManager.getLogger()

    /**
     * Converts text to speech using the preferred voice settings.
     * If the preferred voice is not found, it uses the first available voice.
     *
     * @param text The text to be converted to speech.
     * @throws SpeechEngineCreationException If there is an error creating the speech engine.
     * @throws IOException                   If there is an input/output error.
     */
    @Throws(SpeechEngineCreationException::class, IOException::class)
    fun tts(text: String?) {
      val speechEngine = SpeechEngineNative.getInstance()
      val voices = speechEngine.availableVoices
      var voice = speechEngine.findVoiceByPreferences(voicePreferences)

      if (voice == null) {
        log.error("Voice has not been found by the voice preferences {}\n", voicePreferences)
        voice = voices.first()
        log.info("Using \"{}\" instead.\n", voice)
      }

      speechEngine.setVoice(voice!!.name)
      speechEngine.say(text)
    }

    /**
     * Sets the language preference for the voice.
     *
     * @param text The language code (e.g., "en" for English).
     */
    fun voiceLanguage(text: String?) {
      voicePreferences.language = text
    }

    /**
     * Sets the country preference for the voice.
     *
     * @param country The country code (e.g., "US" for the United States).
     */
    fun voiceCountry(country: String?) {
      voicePreferences.country = country
    }

    /**
     * Sets the gender preference for the voice.
     *
     * @param gender The gender (Enum from [VoicePreferences]).
     */
    fun voiceGender(gender: Gender?) {
      voicePreferences.gender = gender
    }

    private const val PREFERENCES_FILE = "voicePreferences.txt"

    @Throws(URISyntaxException::class)
    fun saveVoicePreferences() {
      val filePath = getResourcePath(PREFERENCES_FILE)
      try {
        BufferedWriter(FileWriter(filePath)).use {writer ->
          writer.write("language=" + voicePreferences.language + "\n")
          writer.write("country=" + voicePreferences.country + "\n")
          writer.write("gender=" + voicePreferences.gender.name + "\n")
          writer.write("maxWords=" + LiveMic.maxWords + "\n")
          log.debug("Voice preferences saved to {}", filePath)
        }
      } catch (e: IOException) {
        log.error("Error saving voice preferences: {}", e.message)
      }
    }

    @Throws(URISyntaxException::class)
    fun loadVoicePreferences() {
      val filePath = getResourcePath(PREFERENCES_FILE)
      try {
        BufferedReader(FileReader(filePath)).use {reader ->
          var line: String
          while ((reader.readLine().also {line = it}) != null) {
            val parts = line.split("=".toRegex()).dropLastWhile {it.isEmpty()}.toTypedArray()
            if (parts.size == 2) {
              when (parts[0]) {
                "language" -> voicePreferences.language = parts[1]
                "country" -> voicePreferences.country = parts[1]
                "gender" -> voicePreferences.gender = Gender.valueOf(parts[1])
                "maxWords" -> LiveMic.maxWords = parts[1].toInt()
              }
            }
          }
          log.debug("Voice preferences loaded from {}", filePath)
        }
      } catch (e: IOException) {
        log.error("Error loading voice preferences: {}", e.message)
      }
    }
  }
}