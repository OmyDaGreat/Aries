package aries.audio

import ai.picovoice.leopard.Leopard
import co.touchlab.kermit.Logger
import kotlinx.coroutines.runBlocking
import util.Keys.get
import util.ResourcePath.getLocalResourcePath
import util.audio.NativeTTS
import util.audio.processAudio
import util.extension.PV
import util.extension.downloadFile
import java.awt.Desktop
import java.io.IOException
import java.net.URI

class LiveMic {
    companion object {
        lateinit var leopardthing: Leopard.Builder

        @JvmField var maxWords = 40

        /**
         * Initializes the Leopard speech-to-text engine by downloading necessary files and setting
         * paths.
         */
        private suspend fun initializeLeopard() {
            NativeTTS.tts("Initializing Leopard.")
            leopardthing =
                Leopard
                    .Builder()
                    .setAccessKey(get("pico"))
                    .setModelPath(downloadFile(PV, getLocalResourcePath("Aries.pv")).absolutePath)
        }

        /**
         * Starts the speech recognition process using the Leopard engine. Initializes the engine if it
         * is not already initialized.
         */
        fun startRecognition() {
            if (!::leopardthing.isInitialized) {
                runBlocking { initializeLeopard() }
            }
            val leopard = leopardthing.build()
            var recorder: Recorder? = null
            Logger.i("Aries is ready.")
            NativeTTS.tts("Aries is ready.")
            try {
                processAudio(
                    {
                        NativeTTS.tts("Yes?")
                        recorder = Recorder(-1)
                        recorder!!.start()
                    },
                    {
                        recorder!!.end()
                        recorder!!.join()
                        val pcm = recorder!!.pcm
                        recorder = null
                        val transcript = leopard.process(pcm)
                        process(transcript.transcriptString.replaceFirst("yes", "", ignoreCase = true).trim())
                    },
                ) {
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
