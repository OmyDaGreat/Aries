package util.notepad

import util.emu.Platform.Companion.currentPlatform
import util.emu.Platform.LINUX
import util.emu.Platform.MAC
import util.emu.Platform.WINDOWS

class NotepadProcessor {
    private val notepad: Notepad =
        when (currentPlatform) {
            WINDOWS -> WinNotepad()
            LINUX -> LinuxNotepad()
            MAC -> MacNotepad()
            else -> error("Platform not supported")
        }

    fun openNotepad() = notepad.openNotepad()

    fun deleteText() = notepad.deleteText()

    fun addNewLine() = notepad.addNewLine()

    fun saveFileAs(name: String) = notepad.saveFileAs(name)

    fun openNewFile() = notepad.openNewFile()

    fun closeNotepad() = notepad.closeNotepad()
}
