package parse;

import java.io.FileInputStream;
import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TranscriberDemo {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = new FileInputStream("src/main/resources/gettysburg.wav");
	
	    recognizer.startRecognition(stream);
		SpeechResult result;
	    while ((result = recognizer.getResult()) != null) {
		    log.info("Hypothesis: {}", result.getHypothesis());
		}
		recognizer.stopRecognition();
    }
}