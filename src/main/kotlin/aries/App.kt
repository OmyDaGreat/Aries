package aries

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import aries.audio.LiveMic
import aries.visual.ComposableGUI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import util.ResourcePath.getLocalResourcePath
import util.extension.downloadFile
import util.extension.icon
import java.io.FileInputStream

val trayIcon by lazy {
  BitmapPainter(
    loadImageBitmap(
      FileInputStream(runBlocking { downloadFile(icon, getLocalResourcePath("icon.png")) })
    )
  )
}

suspend fun main() = runBlocking {
  val job2 = launch {
    println("Starting app")
    application {
      val gui = remember { mutableStateOf(true) }
      println("Starting tray")
      Tray(icon = trayIcon) {
        Item("Toggle GUI", onClick = { gui.value = !gui.value })
        Item("Exit", onClick = { exitApplication() })
      }
      println("Tray is open")

      if (gui.value) {
        println("Opening GUI")
        ComposableGUI(onCloseRequest = { gui.value = false }, icon = trayIcon)
      }
    }
  }

  launch {
    println("Starting recognition")
    LiveMic.startRecognition()
  }

  job2.join()
}
