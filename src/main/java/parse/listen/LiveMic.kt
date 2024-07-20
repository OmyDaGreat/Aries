package parse.listen

import ai.picovoice.leopard.*
import io.github.jonelo.tts.engines.exceptions.SpeechEngineCreationException
import kotlinx.coroutines.*
import lombok.experimental.ExtensionMethod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import util.*
import util.Keys.get
import util.extension.*
import util.listen.*
import util.notepad.NotepadProcessor
import java.awt.*
import java.io.IOException
import java.net.URI
import java.sql.SQLException
import java.util.*

@ExtensionMethod(RobotUtils::class)
class LiveMic {
  companion object {
    val log: Logger = LogManager.getLogger()

    @Throws(AWTException::class, IOException::class, InterruptedException::class, SpeechEngineCreationException::class)
    private fun process(input: String) {
      check(input.isNotBlank()) {"Hypothesis cannot be blank"}
      when {
        input.trueContains("write special") -> {
          // TODO: Implement special characters
        }

        input.trueContains("write") -> {
          Robot().type(input.replace("write", "").trim {it <= ' '})
        }

        input.trueContains("mouse") -> {
          Robot().mouseMoveString(input.replace(".", "").trim {it <= ' '})
        }

        input.trueContains("notepad") -> {
          val n = NotepadProcessor()
          n.openNotepad()
        }

        input.trueContains("browse") -> {
          open("https://google.com")
          // TODO: Implement browsing
        }

        else -> {
          runBlocking {
            val gemini = generateContent(input.replace("*", ""))
            log.info(gemini)
            NativeTTS.tts(gemini)
          }
        }
      }
    }

    @Throws(
      LeopardException::class,
      IOException::class,
      InterruptedException::class,
      AWTException::class,
      SpeechEngineCreationException::class,
      SQLException::class
    )
    fun startRecognition() {
      val leopard = Leopard.Builder().setAccessKey(get("pico")).build()
      val porcupine = createPorcupine()
      log.debug("Leopard version: {}", leopard.version)
      log.debug("Porcupine version: {}", porcupine.version)
      var recorder: Recorder? = null

      try {
        var line = openAudioLine(porcupine)
        processAudio(line, porcupine, {
          line.close()
          log.info(">>> Wake word detected.")
          recorder = Recorder(-1)
          recorder!!.start()
          log.info(">>> Recording...")
        }, {
          log.info(">>> Silence detected.")
          recorder!!.end()
          recorder!!.join()
          val pcm = recorder!!.pcm
          recorder = null
          val transcript = leopard.process(pcm)
          log.info("{}\n", transcript.transcriptString)
          process(transcript.transcriptString)
          line = openAudioLine(porcupine)
        }) {
          recorder != null
        }
      } catch (e: Exception) {
        log.error("Error: {}", e.message)
        e.printStackTrace()
      } finally {
        leopard.delete()
        porcupine.delete()
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
fun open(page: String) {
  Desktop.getDesktop().browse(URI.create(page))
}