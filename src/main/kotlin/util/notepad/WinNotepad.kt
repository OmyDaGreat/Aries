package util.notepad

import util.audio.NativeTTS
import util.extension.control
import util.extension.enter
import util.extension.type
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.File.separator
import java.nio.file.Files
import java.nio.file.Paths

class WinNotepad : Notepad {
    private val homeDirectory = System.getProperty("user.home") + separator + "Aries"
    private val robot = Robot()
    private var process: Process? = null

    init {
        Files.createDirectories(Paths.get(homeDirectory))
    }

    override fun openNotepad() {
        process = ProcessBuilder("C:\\Program Files\\Notepad++\\notepad++.exe").start()
        robot.delay(1000)
    }

    override fun deleteText() {
        robot.apply {
            control(KeyEvent.VK_A)
            type(KeyEvent.VK_BACK_SPACE)
        }
    }

    override fun addNewLine() = robot.enter()

    override fun saveFileAs(name: String) {
        robot.apply {
            println("saving file as $name")
            control(KeyEvent.VK_S)
            delay(1000)
            type(name)
            repeat(7) { type(KeyEvent.VK_TAB) }
            enter()
            type("${System.getProperty("user.home")}${separator}Documents$separator")
            repeat(10) { type(KeyEvent.VK_TAB) }
            enter()
        }
    }

    override fun openNewFile() = robot.control(KeyEvent.VK_N)

    override fun closeNotepad() {
        process?.let {
            it.destroy()
            val exitCode = it.waitFor()
            val message =
                if (exitCode == 1) {
                    "Exited Notepad++"
                } else {
                    "Please open Notepad++ with the open notepad command"
                }
            NativeTTS.tts(message)
        } ?: run { NativeTTS.tts("Notepad++ is not open") }
        process = null
    }
}
