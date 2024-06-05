package util;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.awt.*;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@UtilityClass
@Log4j2
@ExtensionMethod(StringExtension.class)
public class LiveMic {
  private boolean keyword = false;
  private boolean isOpenPage = false;
  private boolean isOpenNotepad = false;

  public void startRecognition() throws IOException, InterruptedException, AWTException {
    LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(getConfiguration());
    recognizer.startRecognition(true);

    SpeechResult result;
    while ((result = recognizer.getResult()) != null) {
      String hypothesis = result.getHypothesis();
      log.info("Hypothesis: {}", hypothesis);
      if (hypothesis.contains("open")) {
        log.debug("Someone needs help!");
        allFalse();
        keyword = true;
      }
      if (hypothesis.contains("notepad", "note that") && keyword) {
        log.debug("Notepad is open!");
        WinNotepad.open();
        isOpenNotepad = true;
      }
      if (hypothesis.contains("page") && keyword) {
        log.debug("Page is open!");
        OpenPage.open("https://imgur.com/a/kBPQWWd");
        isOpenPage = true;
      }
    }
    recognizer.stopRecognition();
  }

  private static Configuration getConfiguration() {
    Configuration configuration = new Configuration();
    configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
    configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
    configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
    return configuration;
  }

  private void allFalse() {
    keyword = false;
    isOpenPage = false;
    isOpenNotepad = false;
  }
}
