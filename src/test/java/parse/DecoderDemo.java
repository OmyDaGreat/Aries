package parse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vosk.LogLevel;
import org.vosk.Recognizer;

import lombok.extern.log4j.Log4j2;

import org.vosk.LibVosk;
import org.vosk.Model;

@Log4j2
public class DecoderDemo {

    public static void main(String[] argv) throws IOException, UnsupportedAudioFileException {
        LibVosk.setLogLevel(LogLevel.DEBUG);

        try (Model model = new Model("src/main/resources/voskModel");
             InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Files.newInputStream(Paths.get("src/main/resources/fromSphinx.wav"))));
             Recognizer recognizer = new Recognizer(model, 16000)) {

            int nbytes;
            byte[] b = new byte[4096];
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    logger.info(recognizer.getResult());
                } else {
                    logger.info(recognizer.getPartialResult());
                }
            }

            logger.info(recognizer.getFinalResult());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}