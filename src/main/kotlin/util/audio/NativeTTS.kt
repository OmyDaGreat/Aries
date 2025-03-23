package util.audio

import aries.audio.LiveMic
import io.github.jonelo.tts.engines.SpeechEngineNative
import io.github.jonelo.tts.engines.VoicePreferences
import io.github.jonelo.tts.engines.VoicePreferences.Gender
import util.ResourcePath.getLocalResourcePath
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter

/**
 * Utility class for text-to-speech (TTS) functionalities. This class provides methods to convert
 * text into speech using the native speech engine.
 */
class NativeTTS {
    companion object {
        val voicePreferences = VoicePreferences()

        /**
         * Converts text to speech using the preferred voice settings. If the preferred voice is not
         * found, it uses the first available voice.
         *
         * @param text The text to be converted to speech.
         */
        @JvmStatic
        fun tts(text: String?) {
            val speechEngine = SpeechEngineNative.getInstance()
            val voice = speechEngine.findVoiceByPreferences(voicePreferences) ?: speechEngine.availableVoices.first()
            speechEngine.setVoice(voice.name)
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

        fun saveVoicePreferences() {
            val filePath = getLocalResourcePath(PREFERENCES_FILE)
            BufferedWriter(FileWriter(filePath)).use { writer ->
                writer.write("language=" + voicePreferences.language + "\n")
                writer.write("country=" + voicePreferences.country + "\n")
                writer.write("gender=" + voicePreferences.gender.name + "\n")
                writer.write("maxWords=" + LiveMic.maxWords + "\n")
            }
        }

        fun loadVoicePreferences() {
            val filePath = getLocalResourcePath(PREFERENCES_FILE)
            BufferedReader(FileReader(filePath)).use { reader ->
                reader.forEachLine { line ->
                    val (key, value) = line.split("=", limit = 2)
                    when (key) {
                        "language" -> voicePreferences.language = value
                        "country" -> voicePreferences.country = value
                        "gender" -> voicePreferences.gender = Gender.valueOf(value)
                        "maxWords" -> LiveMic.maxWords = value.toInt()
                    }
                }
            }
        }
    }
}
