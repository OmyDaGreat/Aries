package util.notepad

import util.extension.command
import util.extension.control
import util.extension.enter
import util.extension.type
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException

class MacNotepad : Notepad {
    private val robot = Robot()
    private var process: Process? = null

    override fun openNotepad() {
        try {
            process = ProcessBuilder("open", "-a", "Notes").start()
            Thread.sleep(2000) // allow app to open before typing
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
            ProcessBuilder(
                "osascript",
                "-e",
                """
                tell application \"System Events\"
                    click menu item \"New Note\" of menu 1 of menu bar item \"File\" of menu bar 1 of application \"Notes\"
                end tell
                """.trimIndent(),
            ).start()
                .waitFor()
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
