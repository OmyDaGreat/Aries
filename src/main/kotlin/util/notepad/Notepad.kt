package util.notepad

interface Notepad {
    fun openNotepad()

    fun deleteText()

    fun addNewLine()

    fun saveFileAs(name: String)

    fun openNewFile()

    fun closeNotepad()
}
