package aries

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import aries.audio.LiveMic
import aries.visual.ComposableGUI
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity.Verbose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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
        FileInputStream(runBlocking { downloadFile(ICON, getLocalResourcePath("aries.png")) }).readAllBytes().decodeToImageBitmap(),
    )
}

class AriesApplication {
    private val applicationScope = CoroutineScope(SupervisorJob() + Default)
    private val recognitionActive = mutableStateOf(false)

    fun startRecognition() {
        if (!recognitionActive.value) {
            applicationScope.launch {
                Logger.d("Starting recognition")
                LiveMic.startRecognition()
                recognitionActive.value = true
            }
        }
    }

    fun shutdown() {
        Logger.d("Shutting down application")
        applicationScope.cancel()
    }

    fun run() {
        Logger.setMinSeverity(Verbose)
        Logger.d("Starting app")

        startRecognition()

        application {
            val gui = remember { mutableStateOf(true) }

            Logger.d("Starting tray")
            Tray(icon = trayIcon) {
                Item("Toggle GUI", onClick = { gui.value = !gui.value })
                Item("Exit", onClick = {
                    shutdown()
                    exitApplication()
                })
            }

            if (gui.value) {
                Logger.d("Opening GUI")
                MaterialTheme(
                    colors =
                        lightColors(
                            primary = Red,
                            secondary = Red,
                        ),
                ) {
                    ComposableGUI(onCloseRequest = { gui.value = false }, icon = trayIcon)
                }
            }
        }
    }
}

fun main() {
    AriesApplication().run()
}
