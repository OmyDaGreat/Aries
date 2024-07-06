package parse

import ai.picovoice.leopard.Leopard
import ai.picovoice.leopard.LeopardException
import ai.picovoice.leopard.LeopardTranscript
import io.github.jonelo.tts.engines.exceptions.SpeechEngineCreationException
import lombok.experimental.ExtensionMethod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import util.Keys.get
import util.NativeTTS
import util.OpenPage
import util.PyScript
import util.extension.RobotUtils
import util.extension.*
import util.notepad.NotepadProcessor
import util.printFileContents
import java.awt.AWTException
import java.awt.Robot
import java.io.FileWriter
import java.io.IOException
import java.sql.SQLException
import java.util.*

@ExtensionMethod(RobotUtils::class)
class LiveMic {
  companion object {
    val log: Logger = LogManager.getLogger()
    @Throws(AWTException::class, IOException::class, InterruptedException::class, SpeechEngineCreationException::class)
    private fun process(input: String) {
      check(input.isNotBlank()) { "Hypothesis cannot be blank" }
      when {
        input.trueContains("write special") -> {
          // TODO: Implement special characters
        }
        input.trueContains("write") -> {
          Robot().type(input.replace("write", "").trim { it <= ' ' })
        }
        input.trueContains("mouse") -> {
          Robot().mouseMoveString(input.replace(".", "").trim { it <= ' ' })
        }
        input.trueContains("notepad") -> {
          val n = NotepadProcessor()
          n.openNotepad()
        }
        input.trueContains("browse") -> {
          OpenPage.open("https://google.com")
          // TODO: Implement browsing
        }
        else -> {
          gemini(input)
          NativeTTS.ttsFromFile("output.txt")
        }
      }
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun gemini(string: String) {
      FileWriter("prompt.txt").use { writer ->
        writer.write(string)
      }
      PyScript.run()
      printFileContents("output.txt")
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
      val leopard =
        Leopard.Builder()
          .setAccessKey(get("pico"))
          .setModelPath("src/main/resources/leopard.pv")
          .setEnableAutomaticPunctuation(true)
          .build()
      log.debug("Leopard version: {}", leopard.version)
      var recorder: Recorder? = null
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
          recorder = Recorder(-1)
          recorder.start()
        }
      }
      leopard.delete()
    }
  }
}
