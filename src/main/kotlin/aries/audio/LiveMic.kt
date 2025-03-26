package aries.audio

import ai.picovoice.leopard.Leopard
import co.touchlab.kermit.Logger
import kotlinx.coroutines.runBlocking
import util.Keys
import util.ResourcePath.getLocalResourcePath
import util.audio.NativeTTS
import util.audio.Recorder
import util.extension.PV
import util.extension.downloadFile
import util.process.process
import java.awt.Desktop
import java.net.URI

class LiveMic {
    companion object {
        lateinit var leopardBuild: Leopard.Builder

        @JvmField var maxWords = 40

        /**
         * Initializes the Leopard speech-to-text engine by downloading necessary files and setting
         * paths.
         */
        private suspend fun initializeLeopard() {
            NativeTTS.tts("Initializing Leopard.")
            leopardBuild =
                Leopard
                    .Builder()
                    .setAccessKey(Keys["pico"])
                    .setModelPath(downloadFile(PV, getLocalResourcePath("Aries.pv")).absolutePath)
        }

        /**
         * Starts the speech recognition process using the Leopard engine. Initializes the engine if it
         * is not already initialized.
         */
        fun startRecognition() {
            if (!::leopardBuild.isInitialized) {
                runBlocking { initializeLeopard() }
            }
            val leopard = leopardBuild.build()
            var recorder: Recorder? = null
            Logger.i("Aries is ready.")
            NativeTTS.tts("Aries is ready.")
            runCatching {
                processAudio {
                    onKeywordDetected = {
                        NativeTTS.tts("Yes?")
                        recorder = Recorder(-1)
                        recorder!!.start()
                    }
                    onSilence = {
                        recorder!!.end()
                        recorder!!.join()
                        val pcm = recorder!!.pcm
                        recorder = null
                        val transcript = leopard.process(pcm)
                        process(transcript.transcriptString.replaceFirst("yes", "", ignoreCase = true).trim())
                    }
                    isRecording = { recorder != null }
                }
            }.also {
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
 */
fun open(page: String) = page.apply { Desktop.getDesktop().browse(URI.create(page)) }
