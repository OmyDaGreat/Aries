package parse.audio

import ai.picovoice.leopard.*
import io.github.jonelo.tts.engines.exceptions.SpeechEngineCreationException
import kotlinx.coroutines.*
import lombok.experimental.ExtensionMethod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import util.*
import util.Keys.get
import util.ResourcePath.getResourcePath
import util.extension.*
import util.extension.RobotUtils.special
import util.audio.*
import util.notepad.NotepadProcessor
import java.awt.*
import java.io.IOException
import java.net.URI
import java.sql.SQLException
import java.util.*
import javax.swing.JOptionPane

@ExtensionMethod(RobotUtils::class)
class LiveMic {
  companion object {
    private val log: Logger = LogManager.getLogger()
    private val n = NotepadProcessor()
    @JvmField
    var maxWords = 40

    @Throws(AWTException::class, IOException::class, InterruptedException::class, SpeechEngineCreationException::class)
    private fun process(input: String) {
      when {
        input.trueContains("ask gemini") -> {
          ask("Answer the request while staying concise: $input")
        }

        input.trueContains("write special") -> {
          input.replace("write special", "", ignoreCase = true).trim {it <= ' '}
            .split(" ").forEach {c ->
              if(special.containsKeyFirst(c)) {
                special.getFromFirst(c).forEach {key ->
                  Robot().type(key)
                }
              } else {
                Robot().type(c)
              }
          }
        }

        input.trueContains("write") -> {
          Robot().type(input.replace("write", "", ignoreCase = true).trim {it <= ' '})
        }

        input.trueContains("search") -> {
          open("https://www.google.com/search?q=" + input.replace("search", "", ignoreCase = true).trim {it <= ' '}.replace(" ", "+"))
        }

        input.trueContains("mouse") -> {
          Robot().mouseMoveString(input.replace(".", "").replace("mouse", "", ignoreCase = true).trim {it <= ' '})
        }

        input.trueContainsAny("open notepad", "opened notepad") -> {
          n.openNotepad()
        }

        input.trueContainsAny("close notepad", "closed notepad", "clothes notepad") -> {
          n.closeNotepad()
        }

        input.trueContains("open new") -> {
          n.openNewFile()
        }

        input.trueContains("delete everything") -> {
          n.deleteText()
        }

        input.trueContains("save file") -> {
          n.saveFileAs(input.replace("save file", "", ignoreCase = true).trim {it <= ' '}.removeForIfFirst().replace(" ", "_"))
        }

        input.trueContains("enter") -> {
          n.addNewLine()
        }

        input.trueContains("arrow") -> {
          Robot().arrow(input.replace("arrow", "", ignoreCase = true).trim {it <= ' '})
        }

        else -> {
          ask("Answer the request while staying concise: $input")
        }
      }
    }

    private fun ask(input: String) {
      runBlocking {
        val gemini = generateContent(input).replace("*", "")
        log.info(gemini)
        launch {
          if (gemini.split(" ").size > maxWords) {
            NativeTTS.tts("The response is over $maxWords words.")
          } else {
            log.debug(gemini)
            NativeTTS.tts(gemini)
          }
        }
        Thread {
          JOptionPane.showMessageDialog(null, gemini)
        }.start()
      }
    }

    private fun String.removeForIfFirst(): String {
      return if (startsWith("for ")) {
        removePrefix("for ").trimStart()
      } else {
        this
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
      val leopard = Leopard.Builder().setAccessKey(get("pico")).setModelPath(getResourcePath("Aries.pv")).build()
      log.debug("Leopard version: {}", leopard.version)
      log.info("Ready...")
      NativeTTS.tts("Aries is starting up...")
      var recorder: Recorder? = null

      try {
        processAudio({
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
        }) {
          recorder != null
        }
      } catch (e: Exception) {
        log.error("Error: {}", e.message)
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
fun open(page: String) {
  Desktop.getDesktop().browse(URI.create(page))
}