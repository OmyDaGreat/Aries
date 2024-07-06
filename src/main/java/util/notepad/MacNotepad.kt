package util.notepad

import org.apache.logging.log4j.LogManager
import java.nio.file.Paths

class MacNotepad : Notepad {
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

  override fun writeText(text: String) {
    //TODO: Implement writing text
  }
  override fun deleteText() {
    //TODO: Implement deleting text
  }
  override fun addNewLine() {
    //TODO: Implement adding a new line
  }
  override fun saveFileAs(name: String) {
    //TODO: Implement saving file
  }
  override fun openNewFile() {
    //TODO: Implement opening a new file
  }
  override fun closeFile() {
    //TODO: Implement closing a file
  }
  override fun closeNotepad() {
    //TODO: Implement closing notepad
  }
}