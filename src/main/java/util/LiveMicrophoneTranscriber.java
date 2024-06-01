package util;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@UtilityClass
@Log4j2
public class LiveMicrophoneTranscriber {
    
    private final String targetWord = "help";

    public void startRecognition() throws IOException {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        
        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
        recognizer.startRecognition(true);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
	        log.info("Hypothesis: {}", result.getHypothesis());
            if (result.getHypothesis().contains(targetWord)) {
                log.debug("Target word '{}' found!", targetWord);
                OpenPage.open("https://imgur.com/a/kBPQWWd");
                // TODO: Add commands
                recognizer.stopRecognition();
            }
        }
        recognizer.stopRecognition();
    }
}
