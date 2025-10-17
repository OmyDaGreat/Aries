package aries.audio

import co.touchlab.kermit.Logger
import kotlinx.coroutines.runBlocking
import util.audio.NativeTTS
import util.audio.Recorder
import util.process.process
import java.awt.Desktop
import java.net.URI

class LiveMic {
    companion object {
        lateinit var whisperInstance: WhisperEngine.WhisperInstance

        @JvmField var maxWords = 40

        /**
         * Initializes the Whisper speech-to-text engine by downloading necessary model files.
         */
        private suspend fun initializeWhisper() {
            NativeTTS.tts("Initializing Whisper.")
            Logger.d("Initializing Whisper.")
            whisperInstance = WhisperEngine.builder().build()
            Logger.d("Whisper initialized.")
        }

        /**
         * Starts the speech recognition process using the Whisper engine. Initializes the engine if it
         * is not already initialized.
         */
        fun startRecognition() {
            if (!::whisperInstance.isInitialized) {
                runBlocking { initializeWhisper() }
            }
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
                        val transcript = whisperInstance.process(pcm)
                        process(transcript.transcriptString.replaceFirst("yes", "", ignoreCase = true).trim())
                    }
                    isRecording = { recorder != null }
                }
            }.also {
                whisperInstance.delete()
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
