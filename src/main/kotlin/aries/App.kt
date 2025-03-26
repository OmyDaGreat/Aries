package aries

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import aries.audio.LiveMic
import aries.visual.ComposableGUI
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity.Verbose
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import util.ResourcePath.getLocalResourcePath
import util.extension.ICON
import util.extension.downloadFile
import java.io.FileInputStream

@OptIn(ExperimentalResourceApi::class)
val trayIcon by lazy {
    BitmapPainter(
        FileInputStream(runBlocking { downloadFile(ICON, getLocalResourcePath("icon.png")) }).readAllBytes().decodeToImageBitmap(),
    )
}

fun main() {
    Logger.setMinSeverity(Verbose)
    Logger.d("Starting app")

    // Start application on main thread
    application {
        val gui = remember { mutableStateOf(true) }

        // Launch LiveMic in a separate coroutine AFTER Compose is initialized
        val scope = rememberCoroutineScope()

        DisposableEffect(Unit) {
            val job =
                scope.launch {
                    Logger.d("Starting recognition")
                    LiveMic.startRecognition()
                }
            onDispose {
                job.cancel()
            }
        }

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
