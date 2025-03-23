package aries

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import aries.audio.LiveMic
import aries.visual.ComposableGUI
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity.Verbose
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import util.ResourcePath.getLocalResourcePath
import util.extension.ICON
import util.extension.downloadFile
import java.io.FileInputStream

val trayIcon by lazy {
    BitmapPainter(
        loadImageBitmap(
            FileInputStream(runBlocking { downloadFile(ICON, getLocalResourcePath("icon.png")) }),
        ),
    )
}

fun main() =
    runBlocking {
        Logger.setMinSeverity(Verbose)
        val job2 =
            launch {
                Logger.d("Starting app")
                application {
                    val gui = remember { mutableStateOf(true) }
                    Logger.d("Starting tray")
                    Tray(icon = trayIcon) {
                        Item("Toggle GUI", onClick = { gui.value = !gui.value })
                        Item("Exit", onClick = { exitApplication() })
                    }
                    Logger.d("Tray is open")
                    if (gui.value) {
                        Logger.d("Opening GUI")
                        MaterialTheme(
                            colors =
                                lightColors(
                                    primary = Color.Red,
                                    secondary = Color.Red,
                                ),
                        ) {
                            ComposableGUI(onCloseRequest = { gui.value = false }, icon = trayIcon)
                        }
                    }
                }
            }

        launch {
            Logger.d("Starting recognition")
            LiveMic.startRecognition()
        }

        job2.join()
    }
