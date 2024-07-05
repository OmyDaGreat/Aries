package parse

import util.notepad.NotepadProcessor

fun main() {
  NotepadProcessor().apply {
    try {
      openNotepad()
      writeText("Hello World\nThis is a test")
      saveFileAs("test.txt")
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}