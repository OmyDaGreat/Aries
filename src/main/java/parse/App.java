package parse;

import java.awt.*;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import util.LiveMic;

@Log4j2
public class App {

  public static void main(String[] args) throws InterruptedException {
    log.info("Hello world!");

    // Create and start the GUI thread
    Thread guiThread =
        new Thread(
            () -> {
              try {
                GUI.run();
              } catch (Exception e) {
                log.error("Error starting the GUI", e);
              }
            });
    guiThread.start();

    // Create and start the LiveMicrophoneTranscriber thread
      Thread transcriberThread = getTranscriberThread();
      
      // Wait for the threads to finish
    try {
      transcriberThread.join();
      guiThread.join();
    } catch (InterruptedException e) {
      log.error("Interrupted while waiting for threads to finish", e);
      throw e;
    }

    log.info("Application finished.");
  }
    
    private static Thread getTranscriberThread() {
        Thread transcriberThread =
            new Thread(
                () -> {
                  try {
                    LiveMic.startRecognition();
                  } catch (IOException | AWTException e) {
                    log.error("Error starting the transcriber", e);
                  } catch (InterruptedException e) {
                    log.error("Interrupted while waiting for the transcriber to finish", e);
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                  }
                });
        transcriberThread.start();
        return transcriberThread;
    }
}
