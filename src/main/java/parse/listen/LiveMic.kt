package parse.listen

import ai.picovoice.leopard.*
import ai.picovoice.porcupine.*
import io.github.jonelo.tts.engines.exceptions.SpeechEngineCreationException
import kotlinx.coroutines.*
import lombok.experimental.ExtensionMethod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import util.Keys.get
import util.NativeTTS
import util.Platform
import util.ResourcePath
import util.extension.*
import util.generateContent
import util.notepad.NotepadProcessor
import java.awt.*
import java.io.IOException
import java.net.URI
import java.sql.SQLException
import java.util.*

@ExtensionMethod(RobotUtils::class)
class LiveMic {
  companion object {
    val platform: Platform = Platform.detectPlatform()

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
            val gemini = generateContent(input)
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
      val porcupine = Porcupine.Builder()
              .setAccessKey(get("pico")) // Ensure ACCESS_KEY is defined somewhere in your code
              .setKeywordPaths(arrayOf("src/main/resources/" +
                      if (platform == Platform.WINDOWS) "hey-parse_en_windows_v3_0_0.ppn"
                      else if (platform == Platform.MAC) "hey-parse_en_mac_v3_0_0.ppn"
                      else if (platform == Platform.LINUX) "hey-parse_en_linux_v3_0_0.ppn"
                        else throw IllegalArgumentException("Platform not supported")
              )).build()
      val leopard = Leopard.Builder().setAccessKey(get("pico"))
        .setEnableAutomaticPunctuation(true).build()
      log.debug("Leopard version: {}", leopard.version)
      var recorder: Recorder? = null
      var wakeUpWord: Recorder?
      val scanner = Scanner(System.`in`)

      var transcript: LeopardTranscript
      while (System.`in`.available() == 0) {
        if (recorder != null) {
          log.info(">>> Recording ... Press 'ENTER' to stop:")
          scanner.nextLine()
          recorder.end()
          recorder.join()
          val pcm = recorder.pcm
          transcript = leopard.process(pcm)
          log.info("{}\n", transcript.transcriptString)
          process(transcript.transcriptString)
          recorder = null
          log.info("Ready...")
        } else {
          log.info(">>> Press 'ENTER' to start:")
          scanner.nextLine()
          wakeUpWord = Recorder(-1)
          wakeUpWord.start()
          log.info(">>> Recording ... Press 'ENTER' to stop:")
          scanner.nextLine()
          wakeUpWord.end()
          wakeUpWord.join()
          val pcm = wakeUpWord.pcm
          log.info("Audio frame size: ${pcm.size}")
          val keywordIndex = porcupine.process(pcm)
          log.info("$keywordIndex")
          when (keywordIndex) {
            0 -> {
              log.info("Wake word detected")
              recorder = Recorder(-1)
              recorder.start()
            }
//            else -> {
//              log.info("Wake word detected")
//              recorder = Recorder(-1)
//              recorder.start()
//            }
          }

        }
      }
      porcupine.delete();
      leopard.delete()
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