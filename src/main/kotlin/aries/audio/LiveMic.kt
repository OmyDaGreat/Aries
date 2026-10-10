package aries.audio

import co.touchlab.kermit.Logger
import kotlinx.coroutines.runBlocking
import util.audio.NativeTTS
import util.audio.Recorder
import util.process.process
import java.awt.Desktop
import java.net.URI
import java.util.concurrent.atomic.AtomicBoolean

object LiveMic {
    lateinit var whisperInstance: WhisperEngine.WhisperInstance
    private val recognitionRunning = AtomicBoolean(false)

    @JvmField var maxWords = 40

    /**
     * Initializes the Whisper speech-to-text engine by downloading necessary model files.
     */
    private suspend fun initializeWhisper() {
        NativeTTS.tts("Initializing Whisper.")
        Logger.d(tag = "Whisper") { "Initializing Whisper." }
        whisperInstance = WhisperEngine.builder().build()
        Logger.d(tag = "Whisper") { "Whisper initialized." }
    }

    /**
     * Starts the speech recognition process using the Whisper engine. Initializes the engine if it
     * is not already initialized.
     */
    fun startRecognition() {
        if (!recognitionRunning.compareAndSet(false, true)) return
        var recorder: Recorder? = null
        try {
            if (!::whisperInstance.isInitialized) {
                runBlocking { initializeWhisper() }
            }
            Logger.i(tag = "Aries") { "Aries is ready." }
            NativeTTS.tts("Aries is ready.")
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
                isRunning = recognitionRunning::get
            }
        } finally {
            recorder?.let {
                it.end()
                it.join()
            }
            if (::whisperInstance.isInitialized) {
                WhisperEngine.close()
            }
            recognitionRunning.set(false)
        }
    }

    fun stopRecognition() {
        if (!recognitionRunning.getAndSet(false)) return
        stopAudioProcessing()
    }
}

/**
 * Opens a web page in the system's default browser using a URL string.
 *
 * @param page The URL of the web page to open as a String.
 */
fun open(page: String) = page.apply { Desktop.getDesktop().browse(URI.create(page)) }
