package parse;

import ai.picovoice.leopard.*;
import com.google.common.base.Preconditions;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import javax.sound.sampled.*;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import util.*;
import util.extension.RobotExtension;
import util.extension.StringExtension;
import util.notepad.NotepadProcessor;
import io.github.jonelo.tts.engines.exceptions.SpeechEngineCreationException;

import static util.FilePrinterKt.printFileContents;

@Getter
@Setter
@UtilityClass
@Log4j2
@ExtensionMethod({StringExtension.class, RobotExtension.class})
public class LiveMic {
  private boolean keyword = false;
  private boolean isOpenPage = false;
  private boolean isOpenNotepad = false;
  private boolean mouseMove = false;
  private boolean type = false;

  public void startRecognition()
          throws LeopardException, IOException, InterruptedException, AWTException, SpeechEngineCreationException, SQLException {
    Leopard leopard =
        new Leopard.Builder()
            .setAccessKey(Keys.get("pico"))
            .setModelPath("src/main/resources/leopard.pv")
            .setEnableAutomaticPunctuation(true)
            .build();
    log.debug("Leopard version: {}", leopard.getVersion());
    Recorder recorder = null;
    Scanner scanner = new Scanner(System.in);

    LeopardTranscript transcript;
    while (System.in.available() == 0) {
      if (recorder != null) {
        log.info(">>> Recording ... Press 'ENTER' to stop:");
        scanner.nextLine();
        recorder.end();
        recorder.join();
        short[] pcm = recorder.getPCM();
        transcript = leopard.process(pcm);
        log.info("{}\n", transcript.getTranscriptString());
        process(transcript.getTranscriptString());
        recorder = null;
        log.info("Ready...");
      } else {
        log.info(">>> Press 'ENTER' to start:");
        scanner.nextLine();
        recorder = new Recorder(-1);
        recorder.start();
      }
    }
    leopard.delete();
  }

  private static void process(String input)
      throws AWTException, IOException, InterruptedException, SpeechEngineCreationException {
    Preconditions.checkState(!StringUtils.isBlank(input), "Hypothesis cannot be blank");
    if (input.trueContains("write special")) {
      // TODO: Implement special characters
    } else if (input.trueContains("write")) {
      new Robot().type(input.replace("write", "").trim());
    } else if (input.trueContains("mouse")) {
      new Robot().mouseMoveString(input.replace(".","").trim());
    } else if (input.trueContains("notepad")) {
      NotepadProcessor n = new NotepadProcessor();
      n.openNotepad();
    } else if (input.trueContains("browse")) {
      OpenPage.open("https://google.com");
      // TODO: Implement browsing
    } else {
      gemini(input);
      NativeTTS.ttsFromFile("output.txt");
    }
  }
  
  private static void gemini(String string) throws IOException, InterruptedException {
    try (FileWriter writer = new FileWriter("prompt.txt")) {
      writer.write(string);
    }
    PyScript.run();
    printFileContents("output.txt");
  }
}

@Log4j2
class Recorder extends Thread {
  private TargetDataLine micDataLine = null;
  private boolean stop = false;
  private ArrayList<Short> pcmBuffer = null;

  public Recorder(int audioDeviceIndex) {
    AudioFormat format = new AudioFormat(16000f, 16, 1, true, false);
    DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, format);
    TargetDataLine dataLine;
    try {
      dataLine = getAudioDevice(audioDeviceIndex, dataLineInfo);
      dataLine.open(format);
    } catch (LineUnavailableException e) {
      log.error(
          "Failed to get a valid capture device. Use --show_audio_devices to show available capture devices and their indices");
      System.exit(1);
      return;
    }

    this.micDataLine = dataLine;
    this.stop = false;
    this.pcmBuffer = new ArrayList<>();
  }

  private static TargetDataLine getDefaultCaptureDevice(DataLine.Info dataLineInfo)
      throws LineUnavailableException {
    if (!AudioSystem.isLineSupported(dataLineInfo)) {
      throw new LineUnavailableException(
          "Default capture device does not support the audio format required by Picovoice (16kHz, 16-bit, linearly-encoded, single-channel PCM).");
    }

    return (TargetDataLine) AudioSystem.getLine(dataLineInfo);
  }

  private static TargetDataLine getAudioDevice(int deviceIndex, DataLine.Info dataLineInfo)
      throws LineUnavailableException {
    if (deviceIndex >= 0) {
      try {
        Mixer.Info mixerInfo = AudioSystem.getMixerInfo()[deviceIndex];
        Mixer mixer = AudioSystem.getMixer(mixerInfo);

        if (mixer.isLineSupported(dataLineInfo)) {
          return (TargetDataLine) mixer.getLine(dataLineInfo);
        } else {
          log.error(
              "Audio capture device at index {} does not support the audio format required by Picovoice. Using default capture device.",
                  Optional.of(deviceIndex));
        }
      } catch (Exception e) {
        log.error(
            "No capture device found at index {}. Using default capture device.", Optional.of(deviceIndex));
      }
    }

    // use default capture device if we couldn't get the one requested
    return getDefaultCaptureDevice(dataLineInfo);
  }

  @Override
  public void run() {
    micDataLine.start();

    ByteBuffer captureBuffer = ByteBuffer.allocate(512);
    captureBuffer.order(ByteOrder.LITTLE_ENDIAN);
    short[] shortBuffer = new short[256];

    while (!stop) {
      micDataLine.read(captureBuffer.array(), 0, captureBuffer.capacity());
      captureBuffer.asShortBuffer().get(shortBuffer);
      for (short value : shortBuffer) {
        this.pcmBuffer.add(value);
      }
    }
  }

  public void end() {
    this.stop = true;
    // Close the micDataLine to release resources
    if (micDataLine != null) {
      micDataLine.close();
    }
  }

  public short[] getPCM() {
    short[] pcm = new short[this.pcmBuffer.size()];
    for (int i = 0; i < this.pcmBuffer.size(); ++i) {
      pcm[i] = this.pcmBuffer.get(i);
    }
    return pcm;
  }
}
