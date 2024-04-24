package parse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.vosk.*;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class LiveTranscription {

    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static final double ENERGY_THRESHOLD = 16000; // Adjust based on your requirements. Make it higher if you don't want it to pick up quieter speech

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

            ArrayList<String> lastWords = new ArrayList<>();

            // Transcribe audio
            byte[] buffer = new byte[4096];
            while (running.get()) { // Infinite
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    // Convert to 16-bit little-endian format
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead).order(ByteOrder.LITTLE_ENDIAN);
                    short[] shorts = new short[bytesRead / 2];
                    byteBuffer.asShortBuffer().get(shorts);

                    // Check if the audio is considered silent
                    if (!isSilence(shorts)) {
                        // Feed audio to Vosk
                        recognizer.acceptWaveForm(shorts, bytesRead / 2);
                        String result = recognizer.getResult();
                        logger.debug(result);
                        // Parse and process the result
                        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                        String text = jsonObject.get("text").getAsString();

                        // Split the text into words
                        String[] words = text.split("\\s+");
                        for (String word : words) {
                            if (!word.isEmpty()) {
                                lastWords.add(word);
                                if (lastWords.size() > 5) {
                                    lastWords.removeFirst(); // Keep only the last three words
                                }
                            }
                        }

                        // Print the last three words
                        logger.debug(lastWords);
                    }
                }
            }
        } catch (LineUnavailableException e) {
            logger.error("Error accessing the microphone: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error loading the model: " + e.getMessage());
        }
    }

    // Method to calculate the energy of the audio data
    private static boolean isSilence(short[] audioData) {
        double energy = 0.0;
        for (short sample : audioData) {
            energy += sample * sample;
        }
        energy /= audioData.length;
        return energy < ENERGY_THRESHOLD;
    }
}