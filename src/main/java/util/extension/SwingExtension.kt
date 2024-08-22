package util.extension

import javax.swing.JComponent
import javax.swing.JPanel

fun JPanel.addAll(vararg components: Pair<JComponent, String>) {
  for ((component1, constraints) in components) {
    add(component1, constraints)
  }
}
