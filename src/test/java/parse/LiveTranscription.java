package parse;

import org.vosk.*;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class LiveTranscription {
    
    private static final AtomicBoolean running = new AtomicBoolean(true);
    
    public static void main(String[] args) {

        try {
            // Load the model
            @Cleanup Model model = new Model("src/main/resources/voskModel");
            @Cleanup Recognizer recognizer = new Recognizer(model, 16000.0f);

            // Set up audio input
            AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            @Cleanup TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Transcribe audio
            byte[] buffer = new byte[4096];
            while (running.get()) { // Infinite
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    // Convert to 16-bit little-endian format
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead).order(ByteOrder.LITTLE_ENDIAN);
                    short[] shorts = new short[bytesRead / 2];
                    byteBuffer.asShortBuffer().get(shorts);

                    // Feed audio to Vosk
                    recognizer.acceptWaveForm(shorts, bytesRead / 2);
                    String result = recognizer.getResult();
                    logger.debug(result);
                }
            }
        } catch (LineUnavailableException e) {
            logger.error("Error accessing the microphone: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error loading the model: " + e.getMessage());
        }
    }
}