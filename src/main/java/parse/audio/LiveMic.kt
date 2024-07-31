package parse.audio

import ai.picovoice.leopard.*
import kotlinx.coroutines.*
import lombok.experimental.ExtensionMethod
import util.*
import util.audio.*
import util.extension.*
import util.extension.RobotUtils.special
import util.extension.ScrollOption.Companion.showScrollableMessageDialog
import util.notepad.NotepadProcessor
import java.awt.*
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.IOException
import java.net.URI
import java.util.*
import javax.swing.JOptionPane

@ExtensionMethod(RobotUtils::class)
class LiveMic {
  companion object {
    private val n = NotepadProcessor()

    @JvmField
    var maxWords = 40

    private fun process(input: String) {
      when {
        input.trueContainsAny("write special", "right special") -> {
          input.split(" ").forEach {c ->
            if (special.containsKeyFirst(c)) {
              special.getFromFirst(c).forEach {key ->
                Robot().keyPress(key)
              }
              special.getFromFirst(c).reversed().forEach {key ->
                Robot().keyRelease(key)
              }
            }
          }
        }

        input.trueContainsAny("write", "right") -> {
          Robot().type(input.replace("write", "", ignoreCase = true).replace("right", "", ignoreCase = true).trim())
        }

        input.trueContains("ask gemini") -> {
          ask("Answer the request while staying concise but without contractions: $input")
        }

        input.trueContains("cap") -> {
          Robot().type(KeyEvent.VK_CAPS_LOCK)
        }

        input.trueContainsAny("switch window") -> {
          input.replace("switch window", "", ignoreCase = true).trim().split(" ").forEach {
            it.replaceSpecial().toIntOrNull()?.let { n ->
              Robot().alt{repeat(n) {Robot().tab()}}
            }
          }
        }

        input.trueContains("left press") -> {
          Robot().mousePress(InputEvent.BUTTON1_DOWN_MASK)
        }

        input.trueContains("left release") -> {
          Robot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        }

        input.trueContains("right press") -> {
          Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
        }

        input.trueContains("right release") -> {
          Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
        }

        input.trueContains("middle click") -> {
          Robot().apply {
            mousePress(InputEvent.BUTTON2_DOWN_MASK)
            mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
          }
        }

        input.trueContains("left click") -> {
          Robot().leftClick()
        }

        input.trueContains("right click") -> {
          Robot().rightClick()
        }

        input.trueContains("alte f") -> {
          Robot().alt{it.f(input.replace("alte f", "", ignoreCase = true).replaceSpecial().trim().toIntOrNull())}
        }

        input.trueContains("windows shift") -> {
          Robot().windows{r1 -> r1.shift{r2 -> r2.type(input.replace("windows shift", "", ignoreCase = true).replaceSpecial().trim().lowercase())}}
        }

        input.trueContains("windows") -> {
          Robot().windows{r -> r.type(input.replace("windows", "", ignoreCase = true).replaceSpecial().trim().lowercase())}
        }

        input.trueContains("command shift") -> {
          Robot().command{r1 -> r1.shift{r2 -> r2.type(input.replace("command shift", "", ignoreCase = true).replaceSpecial().trim().lowercase())}}
        }

        input.trueContainsAny("control shift", "controlled shift") -> {
          Robot().control{r1 -> r1.shift{r2 -> r2.type(input.replace("control shift", "", ignoreCase = true).replace("controlled shift", "", ignoreCase = true).replaceSpecial().trim().lowercase())}}
        }

        input.trueContains("shift") -> {
          Robot().shift{r -> r.type(input.replace("shift", "", ignoreCase = true).replaceSpecial().trim().lowercase())}
        }

        input.trueContainsAny("control", "controlled") -> {
          Robot().control{r -> r.type(input.replace("control", "", ignoreCase = true).replace("controlled", "", ignoreCase = true).replaceSpecial().trim().lowercase())}
        }

        input.trueContains("f") -> {
          Robot().f(input.replace("f", "", ignoreCase = true).replaceSpecial().trim().toIntOrNull())
        }

        input.trueContainsAny("command") -> {
          Robot().command{r -> r.type(input.replace("command", "", ignoreCase = true).replaceSpecial().trim().lowercase())}
        }

        input.trueContains("search") -> {
          open("https://www.google.com/search?q=" + input.replace("search", "", ignoreCase = true).trim().removeForIfFirst()
            .replace(" ", "+"))
        }

        input.trueContains("mouse") -> {
          Robot().mouseMoveString(input.replace("mouse", "", ignoreCase = true).trim())
        }

        input.trueContainsAny("scroll", "scrolled") -> {
          Robot().scroll(input.replace("scroll", "", ignoreCase = true).replace("scrolled", "", ignoreCase = true).trim { it <= ' ' })
        }

        input.trueContainsAny("open notepad", "opened notepad") -> {
          n.openNotepad()
        }

        input.trueContainsAny("close notepad", "closed notepad", "clothes notepad") -> {
          n.closeNotepad()
        }

        input.trueContainsAny("open new", "open knew") -> {
          n.openNewFile()
        }

        input.trueContains("delete everything") -> {
          n.deleteText()
        }

        input.trueContains("save file") -> {
          n.saveFileAs(
            input.replace("save file", "", ignoreCase = true).trim().removeForIfFirst().replace(" ", "_")
          )
        }

        input.trueContains("enter") -> {
          n.addNewLine()
        }

        input.trueContains("arrow") -> {
          Robot().arrow(input.replace("arrow", "", ignoreCase = true).trim())
        }

        else -> {
          ask("Answer the request while staying concise but without contractions: $input")
        }
      }
    }

    private fun ask(input: String) {
      runBlocking {
        val gemini = generateContent(input).replace("*", "")
        launch {
          if (gemini.split(" ").size > maxWords) {
            NativeTTS.tts("The response is over $maxWords words.")
          } else {
            NativeTTS.tts(gemini)
          }
        }
        Thread {
          showScrollableMessageDialog(null, gemini, "Gemini", JOptionPane.INFORMATION_MESSAGE)
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

    fun startRecognition() {
      val leopard = leopardthing.build()
      var recorder: Recorder? = null
      println("Aries is ready.")
      NativeTTS.tts("Aries is ready.")
      try {
        processAudio({
          NativeTTS.tts("Yes?")
          recorder = Recorder(-1)
          recorder!!.start()
        }, {
          recorder!!.end()
          recorder!!.join()
          val pcm = recorder!!.pcm
          recorder = null
          val transcript = leopard.process(pcm)
          process(transcript.transcriptString)
        }) {
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
fun open(page: String) {
  Desktop.getDesktop().browse(URI.create(page))
}