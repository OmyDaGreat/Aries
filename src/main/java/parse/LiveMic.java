package parse;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import util.*;

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
        setAll(false);
        keyword = true;
      }
      if (hypothesis.contains("notepad", "note that") && keyword && !isOpenNotepad) {
        log.debug("Notepad is open!");
        WinNotepad.open();
        isOpenNotepad = true;
      }
      if (hypothesis.contains("page") && keyword && !isOpenPage) {
        log.debug("Page is open!");
        OpenPage.open("https://imgur.com/a/kBPQWWd");
        isOpenPage = true;
      }
      if (!isAllTrue()) {
        FileWriter writer = new FileWriter("prompt.txt");
        writer.write(hypothesis);
        writer.close();
        PyScript.run();
        FilePrinter.print("output.txt");
      }
      if (hypothesis.contains("stop")) {
        log.debug("Stopping!");
        System.exit(0);
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

  private void setAll(boolean b) {
    keyword = b;
    isOpenPage = b;
    isOpenNotepad = b;
  }

  private boolean isAllTrue() {
    return keyword && isOpenPage && isOpenNotepad;
  }
}
