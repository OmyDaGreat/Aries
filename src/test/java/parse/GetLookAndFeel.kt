package parse

import org.apache.logging.log4j.LogManager
import javax.swing.UIManager

fun main() {
  UIManager.getInstalledLookAndFeels().forEach {
    LogManager.getLogger().info(it.className)
  }
}