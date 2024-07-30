package util.audio;

import io.github.jonelo.tts.engines.SpeechEngine;
import io.github.jonelo.tts.engines.SpeechEngineNative;
import io.github.jonelo.tts.engines.Voice;
import io.github.jonelo.tts.engines.VoicePreferences;
import io.github.jonelo.tts.engines.exceptions.SpeechEngineCreationException;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import parse.audio.LiveMic;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

import static util.ResourcePath.getResourcePath;

/**
 * Utility class for text-to-speech (TTS) functionalities.
 * This class provides methods to convert text into speech using the native speech engine.
 */
@Log4j2
@UtilityClass
public class NativeTTS {
  public VoicePreferences voicePreferences = new VoicePreferences();

  /**
   * Converts text to speech using the preferred voice settings.
   * If the preferred voice is not found, it uses the first available voice.
   *
   * @param text The text to be converted to speech.
   * @throws SpeechEngineCreationException If there is an error creating the speech engine.
   * @throws IOException                   If there is an input/output error.
   */
  public static void tts(String text) throws SpeechEngineCreationException, IOException {
    SpeechEngine speechEngine = SpeechEngineNative.getInstance();
    List<Voice> voices = speechEngine.getAvailableVoices();
    Voice voice = speechEngine.findVoiceByPreferences(voicePreferences);

    if (voice == null) {
      log.error("Voice has not been found by the voice preferences {}\n", voicePreferences);
      voice = voices.getFirst();
      log.info("Using \"{}\" instead.\n", voice);
    }

    speechEngine.setVoice(voice.getName());
    speechEngine.say(text);
  }

  /**
   * Sets the language preference for the voice.
   *
   * @param text The language code (e.g., "en" for English).
   */
  public static void voiceLanguage(String text) {
    voicePreferences.setLanguage(text);
  }

  /**
   * Sets the country preference for the voice.
   *
   * @param country The country code (e.g., "US" for the United States).
   */
  public static void voiceCountry(String country) {
    voicePreferences.setCountry(country);
  }

  /**
   * Sets the gender preference for the voice.
   *
   * @param gender The gender (Enum from {@link VoicePreferences}).
   */
  public static void voiceGender(VoicePreferences.Gender gender) {
    voicePreferences.setGender(gender);
  }

  /**
   * Converts the text from a file to speech.
   * This method reads the file, replaces asterisks (*) with spaces, and then converts the text to speech.
   *
   * @param filePath The path to the text file.
   * @throws SpeechEngineCreationException If there is an error creating the speech engine.
   * @throws IOException                   If there is an input/output error.
   */
  public static void ttsFromFile(String filePath)
          throws SpeechEngineCreationException, IOException {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        contentBuilder.append(line).append("\n");
      }
    }
    tts(contentBuilder.toString().replace("*", " "));
  }

  private static final String PREFERENCES_FILE = "voicePreferences.txt";

  public static void saveVoicePreferences() throws URISyntaxException {
    String filePath = getResourcePath(PREFERENCES_FILE);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write("language=" + voicePreferences.getLanguage() + "\n");
      writer.write("country=" + voicePreferences.getCountry() + "\n");
      writer.write("gender=" + voicePreferences.getGender().name() + "\n");
      writer.write("maxWords=" + LiveMic.maxWords + "\n");
      log.debug("Voice preferences saved to {}", filePath);
    } catch (IOException e) {
      log.error("Error saving voice preferences: {}", e.getMessage());
    }
  }

  public static void loadVoicePreferences() throws URISyntaxException {
    String filePath = getResourcePath(PREFERENCES_FILE);
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("=");
        if (parts.length == 2) {
          switch (parts[0]) {
            case "language":
              voicePreferences.setLanguage(parts[1]);
              break;
            case "country":
              voicePreferences.setCountry(parts[1]);
              break;
            case "gender":
              voicePreferences.setGender(VoicePreferences.Gender.valueOf(parts[1]));
              break;
            case "maxWords":
              LiveMic.maxWords = Integer.parseInt(parts[1]);
              break;
          }
        }
      }
      log.debug("Voice preferences loaded from {}", filePath);
    } catch (IOException e) {
      log.error("Error loading voice preferences: {}", e.getMessage());
    }
  }
}