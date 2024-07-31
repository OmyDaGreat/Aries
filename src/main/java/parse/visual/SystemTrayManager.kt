package parse.visual

import org.apache.logging.log4j.LogManager
import util.ResourcePath.getResourcePath
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.JFrame

class SystemTrayManager(private val guiFrame: JFrame) {

    private lateinit var trayIcon: TrayIcon
    private var log = LogManager.getLogger()

    fun setupSystemTray() {
        if (!SystemTray.isSupported()) {
            log.error("System tray is not supported")
            return
        }

        val popupMenu = PopupMenu()
        val showGuiItem = MenuItem("Show/Hide GUI")
        showGuiItem.addActionListener { toggleGuiVisibility() }
        popupMenu.add(showGuiItem)

        val iconUrl = "file:" + getResourcePath("java.png")
        val image: Image = ImageIO.read(URI(iconUrl).toURL()).getScaledInstance(16, 16, Image.SCALE_SMOOTH)
        trayIcon = TrayIcon(image)
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