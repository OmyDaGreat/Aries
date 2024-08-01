package parse.visual

import kotlinx.coroutines.runBlocking
import util.ResourcePath.getLocalResourcePath
import util.extension.downloadFile
import util.extension.icon
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.imageio.ImageIO
import javax.swing.JFrame

class SystemTrayManager(private val guiFrame: JFrame) {
  private lateinit var trayIcon: TrayIcon

  fun setupSystemTray() {
    if (!SystemTray.isSupported()) {
      return
    }

    val popupMenu = PopupMenu()
    val showGuiItem = MenuItem("Show/Hide GUI")
    showGuiItem.addActionListener {toggleGuiVisibility()}
    popupMenu.add(showGuiItem)

    runBlocking {
      val image: Image = ImageIO.read(downloadFile(icon, getLocalResourcePath("icon.png")))
        .getScaledInstance(16, 16, Image.SCALE_SMOOTH)
      trayIcon = TrayIcon(image)
    }
    trayIcon.popupMenu = popupMenu

    trayIcon.addMouseListener(object: MouseAdapter() {
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
