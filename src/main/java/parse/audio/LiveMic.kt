package parse.audio

import ai.picovoice.leopard.*
import kotlinx.coroutines.*
import lombok.experimental.ExtensionMethod
import parse.visual.GUI.Companion.cbLanguage
import util.*
import util.Keys.get
import util.ResourcePath.getLocalResourcePath
import util.audio.*
import util.extension.*
import util.extension.RobotUtils.special
import util.extension.ScrollOption.Companion.showScrollableMessageDialog
import util.notepad.NotepadProcessor
import java.awt.*
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.IOException
import java.net.URI
import java.util.*
import javax.swing.JOptionPane
import kotlin.time.Duration

@ExtensionMethod(RobotUtils::class)
class LiveMic {
    companion object {
        lateinit var leopardthing: Leopard.Builder

        @JvmField
        var maxWords = 40

        private suspend fun initializeLeopard() {
            NativeTTS.tts("Initializing Leopard.")
            leopardthing = Leopard.Builder().setAccessKey(get("pico"))
                .setModelPath(downloadFile(pv, getLocalResourcePath("Aries.pv")).absolutePath)
                .setLibraryPath(when (Platform.detectPlatform()) {
                    Platform.WINDOWS -> downloadFile(
                        leolibwin, getLocalResourcePath("libpv_leopard_jni.dll")
                    ).absolutePath

                    Platform.MAC -> downloadFile(
                        leolibmac, getLocalResourcePath("libpv_leopard_jni.dylib")
                    ).absolutePath

                    Platform.LINUX -> downloadFile(
                        leoliblin, getLocalResourcePath("libpv_leopard_jni.so")
                    ).absolutePath

                    else -> null.also {
                        NativeTTS.tts("Leopard is not supported on this platform.")
                    }
                })
        }

        fun startRecognition() {
            if (!::leopardthing.isInitialized) {
                runBlocking {
                    initializeLeopard()
                }
            }
            val leopard = leopardthing.build()
            var recorder: Recorder? = null
            println("Aries is ready.")
            NativeTTS.tts("Aries is ready.")
            try {
                processAudio({
                    NativeTTS.tts("Yes?")
                    recorder = Recorder(-1)
                    recorder!!.start()
                }, {
                    recorder!!.end()
                    recorder!!.join()
                    val pcm = recorder!!.pcm
                    recorder = null
                    val transcript = leopard.process(pcm)
                    process(transcript.transcriptString)
                }) {
                    recorder != null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                leopard.delete()
                startRecognition()
            }
        }
    }
}

/**
 * Opens a web page in the system's default browser using a URL string.
 *
 * @param page The URL of the web page to open as a String.
 * @throws IOException If the default browser is not found, or it fails to be launched.
 */
@Throws(IOException::class)
fun open(page: String): String {
    Desktop.getDesktop().browse(URI.create(page))
    return page
}

/**Plays a beep sound. BEEP!*/
suspend fun beep() {
    NativeTTS.tts("Request confirmed.")
    delay(Duration.parse("1s"))
}