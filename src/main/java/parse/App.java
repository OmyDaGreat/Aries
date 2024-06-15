package parse;

import java.awt.*;
import java.io.IOException;

import ai.picovoice.leopard.LeopardException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class App {

  public static void main(String[] args) throws InterruptedException {
    log.info("Hello world!");

    Thread guiThread = getGUIThread();
    guiThread.start();

    Thread transcriberThread = getTranscriberThread();
    transcriberThread.start();

    try {
      transcriberThread.join();
      guiThread.join();
    } catch (InterruptedException e) {
      log.error("Interrupted while waiting for threads to finish", e);
      throw e;
    }

    log.info("Application finished.");
  }

  private static Thread getGUIThread() {
    return new Thread(
        () -> {
          try {
            GUI.run();
          } catch (Exception e) {
            log.error("Error starting the GUI", e);
          }
        });
  }

  private static Thread getTranscriberThread() {
    return new Thread(
        () -> {
          try {
            LiveMic.startRecognition();
          } catch (IOException | AWTException | LeopardException e) {
            log.error("Error starting the transcriber", e);
          } catch (InterruptedException e) {
            log.error("Interrupted while waiting for the transcriber to finish", e);
            e.printStackTrace();
            Thread.currentThread().interrupt();
          }
        });
  }
}
