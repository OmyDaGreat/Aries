package parse.listen;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

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
      int bytesRead = micDataLine.read(captureBuffer.array(), 0, captureBuffer.capacity());
      if (bytesRead > 0) {
        captureBuffer.clear();
        captureBuffer.asShortBuffer().get(shortBuffer);
        // Ensure we don't exceed the maximum frame size
        int maxSize = 256;
        if (this.pcmBuffer.size() + shortBuffer.length > maxSize) {
          // Create a temporary list to hold the combined old and new data
          List<Short> tempList = getShortList(maxSize, shortBuffer);
          // Clear the pcmBuffer and refill it with the truncated list
          this.pcmBuffer.clear();
          this.pcmBuffer.addAll(tempList);
        } else {
          for (short value : shortBuffer) {
            this.pcmBuffer.add(value);
          }
        }
        captureBuffer.clear();
      } else {
        log.error("captureBuffer does not contain enough audio data to fill the shortBuffer");
      }
    }
  }

  @NotNull
  private List<Short> getShortList(int maxSize, short[] shortBuffer) {
    List<Short> tempList = new ArrayList<>(this.pcmBuffer);
    // Calculate how many elements we can add from shortBuffer without exceeding maxSize
    int elementsToAdd = Math.min(maxSize - tempList.size(), shortBuffer.length);
    // Manually add each short value to the list, boxing it into a Short object
    for (int i = 0; i < elementsToAdd; i++) {
      tempList.add(shortBuffer[i]);
    }
    // Truncate the list to the maximum size
    tempList = tempList.subList(0, maxSize);
    return tempList;
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
