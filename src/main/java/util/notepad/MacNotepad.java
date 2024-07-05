package util.notepad;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

@Log4j2
public class MacNotepad implements Notepad {

    public void openNotepad() {
      String scriptPath = "";
      try {
        scriptPath = Paths.get(Objects.requireNonNull(MacNotepad.class.getClassLoader().getResource("openNotepad.scpt")).toURI()).toString();
        ProcessBuilder pb = new ProcessBuilder("osascript", scriptPath);
        pb.start().waitFor();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
      } catch (URISyntaxException e) {
        //noinspection StringConcatenationArgumentToLogCall bc of throwable
        log.error("Error getting the script path (" + scriptPath + ")", e);
      }
    }

    public void writeText(String text) {
        // TODO: Implement this method
    }

    public void deleteText() {
        // TODO: Implement this method
    }

    public void addNewLine() {
        // TODO: Implement this method
    }

    public void saveFileAs(String name) {
        // TODO: Implement this method
    }

    public void openNewFile() {
        // TODO: Implement this method
    }

    public void closeFile() {
        // TODO: Implement this method
    }

    public void closeNotepad() {
        // TODO: Implement this method
    }
}