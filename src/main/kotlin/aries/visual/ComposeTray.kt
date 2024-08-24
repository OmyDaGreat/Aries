package aries.visual

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import kotlinx.coroutines.runBlocking
import util.ResourcePath.getLocalResourcePath
import util.extension.downloadFile
import util.extension.icon
import java.io.FileInputStream
import kotlin.system.exitProcess

var isWindowOpen = false

val trayIcon =
  BitmapPainter(
    loadImageBitmap(
      FileInputStream(runBlocking { downloadFile(icon, getLocalResourcePath("icon.png")) })
    )
  )

fun tray() = application {
  val trayState = rememberTrayState()

  Tray(state = trayState, icon = trayIcon) {
    Item(
      "Show GUI",
      onClick = {
        if (!isWindowOpen) {
          openWindow()
        }
      },
    )
    Item("Exit", onClick = { exitProcess(0) })
  }
}

fun openWindow() {
  if (!isWindowOpen) {
    isWindowOpen = true
    application {
      Window(
        onCloseRequest = {
          isWindowOpen = false
          exitApplication()
        },
        icon = trayIcon,
      ) {
        ComposeGUI()
      }
    }
  }
}
