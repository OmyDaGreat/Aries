package util.notepad

import org.apache.logging.log4j.LogManager
import java.nio.file.Paths

class MacNotepad: Notepad {
  companion object {
    private val log = LogManager.getLogger(MacNotepad::class.java)
  }

  override fun openNotepad() {
    try {
      val scriptPath = Paths.get(javaClass.classLoader.getResource("openNotepad.scpt")!!.toURI()).toString()
      ProcessBuilder("osascript", scriptPath).start().waitFor()
    } catch (e: Exception) {
      log.error("Error opening notepad with script", e)
      Thread.currentThread().interrupt()
    }
  }

@Override
public void writeText(String text) {
  try {
    // Build AppleScript command string
    StringBuilder script = new StringBuilder("tell application \"System Events\"");
    for (char c : text.toCharArray()) {
      script.append("  keystroke \"").append(c).append("\"");
    }
    script.append("end tell");

    // Execute AppleScript with keystrokes
    ProcessBuilder builder = new ProcessBuilder("osascript", "-e", script.toString());
    builder.start().waitFor();
  } catch (Exception e) {
    log.error("Error writing text to notepad", e);
  }
}

@Override
public void deleteText() {
  try {
    // Simulate multiple delete key presses (adjust count as needed)
    for (int i = 0; i < 5; i++) {
      ProcessBuilder builder = new ProcessBuilder("osascript", "-e", "tell application \"System Events\" key code 51 end tell");
      builder.start().waitFor();
      Thread.sleep(100); // Add a short delay between keystrokes
    }
  } catch (Exception e) {
    log.error("Error deleting text from notepad", e);
  }
}

@Override
public void addNewLine() {
  try {
    ProcessBuilder builder = new ProcessBuilder("osascript", "-e", "tell application \"System Events\" keystroke \"return\" end tell");
    builder.start().waitFor();
  } catch (Exception e) {
    log.error("Error adding new line to notepad", e);
  }
}

@Override
public void openNewNote() {
  try {
    // Simulate opening the "File" menu and selecting "New Note"
    ProcessBuilder builder = new ProcessBuilder("osascript", "-e",
        "tell application \"System Events\"\n" +
        "  click menu item \"New Note\" of menu 1 of menu bar item \"File\" of menu bar 1 of application \"Notes\"\n" +
        "end tell");
    builder.start().waitFor();
  } catch (Exception e) {
    log.error("Error opening new note in notepad", e);
  }
}

  override fun deleteNote() {

  }

override fun openExistingNote() {

}

@Override
public void closeNotepad() {
  try {
    ProcessBuilder builder = new ProcessBuilder("osascript", "-e",
        "tell application \"Notes\" to quit");
    builder.start().waitFor();
  } catch (Exception e) {
    log.error("Error closing notepad", e);
  }
}
}