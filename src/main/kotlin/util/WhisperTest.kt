package util

import aries.audio.WhisperEngine
import kotlinx.coroutines.runBlocking
import util.audio.NativeTTS
import util.audio.Recorder

fun main() {
    runBlocking {
        try {
            val whisper = WhisperEngine.builder().build()

            var recorder: Recorder?
            println("Aries is ready.")
            NativeTTS.tts("Aries is ready.")

            try {
                while (true) {
                    NativeTTS.tts("Yes?")
                    recorder = Recorder(-1)
                    recorder.start()

                    // Simulate some delay to capture audio
                    Thread.sleep(5000)

                    recorder.end()
                    recorder.join()
                    val pcm = recorder.pcm

                    val transcript = whisper.process(pcm)
                    println("Transcription: ${transcript.transcriptString}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                whisper.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
