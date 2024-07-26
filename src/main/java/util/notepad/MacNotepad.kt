package util.notepad

import org.apache.logging.log4j.LogManager
import util.extension.command
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

class MacNotepad: Notepad {
  private val log = LogManager.getLogger()
  private val robot = Robot()

  override fun openNotepad() {
    try {
      val scriptPath = Paths.get(javaClass.classLoader.getResource("openNotepad.scpt")?.toURI()?.toString() ?: "")
      ProcessBuilder("osascript", scriptPath.toString()).start().waitFor()
    } catch (e: Exception) {
      log.error("Error opening notepad with script", e)
    }
  }

  override fun writeText(text: String) {
    try {
      val script = StringBuilder("tell application \"System Events\"")
      for (c in text.toCharArray()) {
        script.append("  keystroke \"").append(c).append("\"")
      }
      script.append("end tell")

      ProcessBuilder("osascript", "-e", script.toString()).start().waitFor()
    } catch (e: Exception) {
      log.error("Error writing text to notepad", e)
    }
  }

  override fun deleteText() {
    try {
      repeat(5) {
        ProcessBuilder("osascript", "-e", "tell application \"System Events\" key code 51 end tell").start().waitFor()
        TimeUnit.MILLISECONDS.sleep(100) // Kotlin equivalent of Thread.sleep
      }
    } catch (e: Exception) {
      log.error("Error deleting text from notepad", e)
    }
  }

  override fun addNewLine() {
    try {
      ProcessBuilder("osascript", "-e", "tell application \"System Events\" keystroke \"return\" end tell").start().waitFor()
    } catch (e: Exception) {
      log.error("Error adding new line to notepad", e)
    }
  }

  override fun saveFileAs(name: String) {
    robot.command(KeyEvent.VK_S)
  }

  override fun openNewFile() {
    try {
      ProcessBuilder("osascript", "-e", """
            tell application \"System Events\"
                click menu item \"New Note\" of menu 1 of menu bar item \"File\" of menu bar 1 of application \"Notes\"
            end tell
        """.trimIndent()).start().waitFor()
    } catch (e: Exception) {
      log.error("Error opening new note in notepad", e)
    }
  }

  override fun closeNotepad() {
    try {
      ProcessBuilder("osascript", "-e", "tell application \"Notes\" to quit").start().waitFor()
    } catch (e: IOException) {
      log.error("Error closing notepad", e)
    }
  }
}