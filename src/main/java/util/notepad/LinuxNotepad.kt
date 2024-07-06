package util.notepad

import org.apache.logging.log4j.LogManager
import util.extension.*
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.File
import java.io.IOException

class LinuxNotepad : Notepad {
  private val log = LogManager.getLogger()
  private val robot = Robot()
  private var process: Process? = null

  companion object {
    val homeDirectory = System.getProperty("user.home") + File.separator + "ParseButPro"
  }

  @Throws(IOException::class)
  override fun openNotepad() {
    process = ProcessBuilder("notepadqq").start().also {
      robot.apply {
        delay(1000)
        mouseMove(RobotUtils.screenWidth / 2, RobotUtils.screenHeight / 2)
        leftClick()
      }
    }
  }

  override fun writeText(text: String) {
    robot.type(text)
  }

  override fun deleteText() {
    robot.apply {
      control(KeyEvent.VK_A)
      type(KeyEvent.VK_DELETE)
    }
  }

  override fun addNewLine() {
    robot.enter()
  }

  override fun saveFileAs(name: String) {
    robot.apply {
      type(name)
      control(KeyEvent.VK_S)
      repeat(7) { type(KeyEvent.VK_TAB) }
      enter()
      type(homeDirectory)
      enter()
    }
  }

  override fun openNewFile() {
    robot.control(KeyEvent.VK_N)
  }

  override fun closeFile() {
    robot.control(KeyEvent.VK_W)
  }

  @Throws(InterruptedException::class)
  override fun closeNotepad() {
    robot.control(KeyEvent.VK_F4)
    process?.waitFor()?.also { log.debug("Exited Notepad++ with code: {}", it) }
  }
}