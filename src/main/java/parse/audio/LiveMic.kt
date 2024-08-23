package parse.audio

import ai.picovoice.leopard.*
import kotlinx.coroutines.*
import lombok.experimental.ExtensionMethod
import util.*
import util.Keys.get
import util.ResourcePath.getLocalResourcePath
import util.audio.*
import util.extension.*
import java.awt.*
import java.io.IOException
import java.net.URI
import java.util.*

@ExtensionMethod(RobotUtils::class)
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
        Leopard.Builder()
          .setAccessKey(get("pico"))
          .setModelPath(downloadFile(pv, getLocalResourcePath("Aries.pv")).absolutePath)
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
      println("Aries is ready.")
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
