package aries.audio

import co.touchlab.kermit.Logger
import io.github.givimad.whisperjni.WhisperContext
import io.github.givimad.whisperjni.WhisperFullParams
import io.github.givimad.whisperjni.WhisperJNI
import util.ResourcePath.getLocalResourcePath
import util.extension.downloadFile
import java.nio.file.Path

/**
 * Whisper-based speech-to-text engine for Aries.
 * Replaces the Picovoice Leopard implementation with OpenAI's Whisper.
 */
object WhisperEngine {
    private var isInitialized = false
    private var isClosed = false
    private lateinit var whisper: WhisperJNI
    private lateinit var ctx: WhisperContext
    private var modelPath: Path? = null

    /**
     * Initializes the Whisper engine by downloading the model if needed.
     */
    suspend fun initialize() {
        if (isInitialized) return

        Logger.d("Initializing Whisper engine.")

        try {
            // Download Whisper model if not exists
            modelPath =
                downloadFile(
                    "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-tiny.en.bin",
                    getLocalResourcePath("whisper-tiny.en.bin"),
                ).toPath()

            // Initialize WhisperJNI
            WhisperJNI.loadLibrary()
            WhisperJNI.setLibraryLogger { }
            whisper = WhisperJNI()

            // Create context
            ctx =
                requireNotNull(whisper.init(modelPath)) {
                    "Failed to initialize Whisper context. Check if model file exists."
                }

            isInitialized = true
            Logger.d("Whisper engine initialized successfully.")
        } catch (e: Exception) {
            Logger.e("Failed to initialize Whisper engine", e)
            throw e
        }
    }

    /**
     * Processes audio data and returns the transcribed text.
     * This method is designed to be compatible with the existing Leopard interface.
     *
     * @param audioData The audio data as a ShortArray (16kHz, 16-bit, mono)
     * @return The transcribed text
     */
    fun process(audioData: ShortArray): String {
        if (!isInitialized || isClosed) {
            Logger.w("Whisper engine not initialized or already closed.")
            return ""
        }

        return try {
            // Convert ShortArray to FloatArray as required by Whisper
            val floatAudio = audioData.map { it / 32768f }.toFloatArray()

            // Set up Whisper parameters
            val params = WhisperFullParams()

            // Process audio with Whisper
            val result = whisper.full(ctx, params, floatAudio, floatAudio.size)
            if (result != 0) {
                Logger.e("Whisper transcription failed with code: $result")
                return ""
            }

            // Extract transcribed text
            val numSegments = whisper.fullNSegments(ctx)
            val transcript = StringBuilder()

            for (i in 0 until numSegments) {
                val segmentText = whisper.fullGetSegmentText(ctx, i).trim()
                if (segmentText.isNotEmpty() &&
                    !segmentText.startsWith("[") && !segmentText.endsWith("]") &&
                    !segmentText.startsWith("(") && !segmentText.endsWith(")")
                ) {
                    transcript.append(segmentText).append(" ")
                }
            }

            transcript.toString().trim()
        } catch (e: Exception) {
            Logger.e("Error during Whisper transcription", e)
            ""
        }
    }

    /**
     * Creates a new Whisper builder instance.
     * This maintains compatibility with the existing Leopard Builder pattern.
     */
    fun builder(): WhisperBuilder = WhisperBuilder()

    /**
     * Closes the Whisper engine and releases resources.
     */
    fun close() {
        if (isClosed || !isInitialized) return

        try {
            whisper.free(ctx)
            isClosed = true
            Logger.d("Whisper engine closed.")
        } catch (e: Exception) {
            Logger.e("Error closing Whisper engine", e)
        }
    }

    /**
     * Builder class to maintain compatibility with existing Leopard code structure.
     */
    class WhisperBuilder {
        suspend fun build(): WhisperInstance {
            initialize()
            return WhisperInstance()
        }
    }

    /**
     * Whisper instance that provides the same interface as Leopard.
     */
    class WhisperInstance {
        fun process(audioData: ShortArray): TranscriptResult {
            val transcript = WhisperEngine.process(audioData)
            return TranscriptResult(transcript)
        }

        fun delete() {
            // Instance deletion is handled by the singleton engine
            Logger.d("Whisper instance delete called (handled by singleton)")
        }
    }

    /**
     * Result class that mimics Leopard's TranscriptResult.
     */
    data class TranscriptResult(
        val transcriptString: String,
    )
}

/**
 * Extension function to maintain compatibility with existing Leopard usage patterns.
 */
fun WhisperEngine.WhisperInstance.convertAudioToText(buffer: ShortArray?): String =
    try {
        if (buffer != null) {
            process(buffer).transcriptString
        } else {
            ""
        }
    } catch (e: Exception) {
        Logger.e("Error converting audio to text", e)
        ""
    }
