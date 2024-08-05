package parse.visual

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import kotlin.system.exitProcess

class SystemTrayManager(private val guiFrame: JFrame) {
  private lateinit var trayIcon: TrayIcon

  fun setupSystemTray() {
    if (!SystemTray.isSupported()) {
      return
    }

    trayIcon = TrayIcon(guiFrame.iconImage)

    val popupMenu = PopupMenu()
    val showGuiItem = MenuItem("Show/Hide GUI")
    showGuiItem.addActionListener { toggleGuiVisibility() }
    popupMenu.add(showGuiItem)

    val exitItem = MenuItem("Exit")
    exitItem.addActionListener { exitProcess(0) }
    popupMenu.add(exitItem)

    trayIcon.popupMenu = popupMenu

    trayIcon.addMouseListener(object : MouseAdapter() {
      override fun mouseClicked(e: MouseEvent) {
        if (e.button == MouseEvent.BUTTON1) {
          toggleGuiVisibility()
        }
      }
    })

    SystemTray.getSystemTray().add(trayIcon)
  }

  private fun toggleGuiVisibility() {
    guiFrame.isVisible = !guiFrame.isVisible
  }
}
