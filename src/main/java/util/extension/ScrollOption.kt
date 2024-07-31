package util.extension

import java.awt.Dimension
import javax.swing.*

class ScrollOption: JOptionPane() {
  companion object {
    fun showScrollableMessageDialog(
        parentComponent: java.awt.Component?,
        message: String,
        title: String,
        messageType: Int,
    ) {
      val textArea = JTextArea(message).apply {
        isEditable = false
        lineWrap = true
        wrapStyleWord = true
      }
      val scrollPane = JScrollPane(textArea).apply {
        preferredSize = Dimension(400, 300)
      }
      showMessageDialog(parentComponent, scrollPane, title, messageType)
    }
  }
}