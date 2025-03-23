package util.notepad

import util.emu.Platform.Companion.currentPlatform
import util.emu.Platform.LINUX
import util.emu.Platform.MAC
import util.emu.Platform.WINDOWS
import java.awt.AWTException
import java.io.IOException

class NotepadProcessor {
    private val notepad: Notepad =
        when (currentPlatform) {
            WINDOWS -> WinNotepad()
            LINUX -> LinuxNotepad()
            MAC -> MacNotepad()
            else -> error("Platform not supported")
        }

    @Throws(IOException::class, InterruptedException::class, AWTException::class)
    fun openNotepad() = notepad.openNotepad()

    fun writeText(text: String) = notepad.writeText(text)

    fun deleteText() = notepad.deleteText()

    fun addNewLine() = notepad.addNewLine()

    fun saveFileAs(name: String) = notepad.saveFileAs(name)

    fun openNewFile() = notepad.openNewFile()

    @Throws(InterruptedException::class)
    fun closeNotepad() = notepad.closeNotepad()
}
