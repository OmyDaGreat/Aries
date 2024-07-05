package parse

import org.apache.logging.log4j.LogManager
import util.notepad.MacNotepad

fun main() {
  LogManager.getLogger().info(MacNotepad::class.java.getResource("/openNotepad.scpt")?.toString() ?: "null")
}