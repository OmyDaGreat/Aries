package aries

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
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
                val isDarkTheme = remember { mutableStateOf(true) }
                
                MaterialTheme(
                    colors = if (isDarkTheme.value) {
                        darkColors(
                            primary = Color(0xFF64B5F6), // Light blue for dark theme
                            primaryVariant = Color(0xFF42A5F5),
                            secondary = Color(0xFF26C6DA), // Cyan accent
                            secondaryVariant = Color(0xFF00ACC1),
                            background = Color(0xFF121212),
                            surface = Color(0xFF1E1E1E),
                            onPrimary = Color(0xFF000000),
                            onSecondary = Color(0xFF000000),
                            onBackground = Color(0xFFE1E1E1),
                            onSurface = Color(0xFFE1E1E1),
                        )
                    } else {
                        lightColors(
                            primary = Color(0xFF2196F3), // Modern blue
                            primaryVariant = Color(0xFF1976D2),
                            secondary = Color(0xFF00BCD4), // Cyan accent
                            secondaryVariant = Color(0xFF0097A7),
                            background = Color(0xFFF5F5F5),
                            surface = Color(0xFFFFFFFF),
                            onPrimary = Color.White,
                            onSecondary = Color.White,
                            onBackground = Color(0xFF212121),
                            onSurface = Color(0xFF212121),
                        )
                    }
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
