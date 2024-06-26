package util;

import util.jAdapterForNativeTTS.engines.*;
import util.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class NativeTTS {
  public static void tts(String text) throws SpeechEngineCreationException, IOException {
    SpeechEngine speechEngine = SpeechEngineNative.getInstance();
    List<Voice> voices = speechEngine.getAvailableVoices();

    VoicePreferences voicePreferences = new VoicePreferences();
    voicePreferences.setLanguage("en");
    voicePreferences.setCountry("US");
    voicePreferences.setGender(VoicePreferences.Gender.MALE);
    Voice voice = speechEngine.findVoiceByPreferences(voicePreferences);

    if (voice == null) {
      log.error("Voice has not been found by the voice preferences {}%n", voicePreferences);
      voice = voices.getFirst();
      log.info("Using \"{}\" instead.%n", voice);
    }

    speechEngine.setVoice(voice.getName());
    speechEngine.say(text);
  }

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
}
