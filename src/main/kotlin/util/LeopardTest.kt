package util

import ai.picovoice.leopard.Leopard
import ai.picovoice.leopard.LeopardException
import util.audio.NativeTTS
import util.audio.Recorder

fun main() {
    val accessKey = Keys["pico"]

    try {
        val leopard =
            Leopard
                .Builder()
                .setAccessKey(accessKey)
                .build()

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

                val transcript = leopard.process(pcm)
                println("Transcription: ${transcript.transcriptString}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            leopard.delete()
        }
    } catch (e: LeopardException) {
        e.printStackTrace()
    }
}
