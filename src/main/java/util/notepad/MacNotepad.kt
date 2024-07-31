package util.notepad

import util.extension.*
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException
import java.nio.file.Paths

class MacNotepad: Notepad {
  private val robot = Robot()

  override fun openNotepad() {
    try {
      val scriptPath = Paths.get(javaClass.classLoader.getResource("openNotepad.scpt")?.toURI()?.toString() ?: "")
      ProcessBuilder("osascript", scriptPath.toString()).start().waitFor()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun writeText(text: String) {
    robot.type(text)
  }

  override fun deleteText() {
    robot.control(KeyEvent.VK_A)
    robot.type(KeyEvent.VK_BACK_SPACE)
  }

  override fun addNewLine() {
    robot.enter()
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
      e.printStackTrace()
    }
  }

  override fun closeNotepad() {
    try {
      ProcessBuilder("osascript", "-e", "tell application \"Notes\" to quit").start().waitFor()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
}