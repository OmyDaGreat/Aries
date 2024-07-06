package util.notepad

import java.awt.AWTException
import java.io.IOException

interface Notepad {
  @Throws(IOException::class, InterruptedException::class, AWTException::class)
  fun openNotepad()
  fun writeText(text: String)
  fun deleteText()
  fun addNewLine()
  fun saveFileAs(name: String)
  fun openNewFile()
  fun closeFile()
  @Throws(InterruptedException::class)
  fun closeNotepad()
}