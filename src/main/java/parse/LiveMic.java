package parse;

import ai.picovoice.leopard.*;
import com.google.common.base.Preconditions;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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

  public void startRecognition()
      throws LeopardException, IOException, InterruptedException, AWTException {
    Leopard leopard =
        new Leopard.Builder()
            .setAccessKey(Keys.get("pico"))
            .setModelPath("src/main/resources/leopard.pv")
            .setEnableAutomaticPunctuation(true)
            .build();
    log.debug("Leopard version: {}", leopard.getVersion());
    Recorder recorder = null;
    Scanner scanner = new Scanner(System.in);

    LeopardTranscript transcript = null;
    while (System.in.available() == 0) {
      if (recorder != null) {
        log.info(">>> Recording ... Press 'ENTER' to stop:");
        scanner.nextLine();
        recorder.end();
        while (recorder.isAlive()) {
          // Waiting until the recorder is finished
        }
        short[] pcm = recorder.getPCM();

        transcript = leopard.process(pcm);
        log.info("{}\n", transcript.getTranscriptString());
        setAll(false);
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

  private static void process(String string)
      throws AWTException, IOException, InterruptedException {
    Preconditions.checkState(!StringUtils.isBlank(string), "Hypothesis cannot be blank");
    if (string.toLowerCase().contains("stop")) {
      log.debug("Stopping!");
      System.exit(0);
    }
    if (string.toLowerCase().contains("open")) {
      log.debug("Someone needs help!");
      setAll(false);
      keyword = true;
    }
    if (string.toLowerCase().contains("notepad", "note that") && keyword && !isOpenNotepad) {
      log.debug("Notepad is open!");
      WinNotepad.open();
    }
    if (string.toLowerCase().contains("page") && keyword && !isOpenPage) {
      log.debug("Page is open!");
      OpenPage.open("https://imgur.com/a/kBPQWWd");
    }
    if (!isAllFalse()) {
      FileWriter writer = new FileWriter("prompt.txt");
      writer.write(string);
      writer.close();
      PyScript.run();
      FilePrinter.print("output.txt");
    }
  }

  private void setAll(boolean b) {
    keyword = b;
    isOpenPage = b;
    isOpenNotepad = b;
  }

  private boolean isAllFalse() {
    return !keyword && !isOpenPage && !isOpenNotepad;
  }
}

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
      System.err.println(
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
          System.err.printf(
              "Audio capture device at index %s does not support the audio format required by Picovoice. Using default capture device.",
              deviceIndex);
        }
      } catch (Exception e) {
        System.err.printf(
            "No capture device found at index %s. Using default capture device.", deviceIndex);
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
