package parse;

import lombok.extern.log4j.Log4j2;

import static parse.CoroutinesKt.startGUI;
import static parse.CoroutinesKt.startTranscriber;

@Log4j2
public class App {

  public static void main(String[] args) {
    log.info("Starting coroutines...");
    startGUI();
    startTranscriber();
  }
}
