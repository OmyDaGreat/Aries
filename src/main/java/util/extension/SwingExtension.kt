package util.extension

import javax.swing.*

fun JTextArea.transformLabel() {
  setLineWrap(true)
  wrapStyleWord = true
  isEditable = false
  isOpaque = false
  isFocusable = false
}

fun JPanel.addAll(vararg components: Pair<JComponent, String>) {
  for ((component1, constraints) in components) {
    add(component1, constraints)
  }
}