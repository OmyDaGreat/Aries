package util.process

import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import parse.audio.LiveMic.Companion.maxWords
import parse.visual.GUI.Companion.cbLanguage
import util.audio.NativeTTS
import util.extension.ScrollOption.Companion.showScrollableMessageDialog
import util.extension.remove
import util.generateContent
import javax.swing.JOptionPane

@OptIn(DelicateCoroutinesApi::class)
suspend fun setAlarm(time: String) {
  val alarmTime = convertToMilliseconds(time)

  // Create a Job to manage the alarm
  val job = Job()

  // Launch the alarm coroutine
  GlobalScope.launch(job) {
    try {
      delay(alarmTime)
      println("Alarm triggered!")
      NativeTTS.tts("Alarm triggered!")
    } catch (e: InterruptedException) {
      println("Alarm interrupted")
    } catch (e: Exception) {
      println("Error occurred in alarm: ${e.message}")
    }
  }

  // Cancel the job after the alarm has been launched
  job.cancel()
}

fun convertToMilliseconds(time: String): Long {
  val seconds = time.split(":")
  return  time.toLong() * 1000
  //h.toLong() * 3600000 + m.toLong() * 60000
}

/** Plays a "Request Confirmed" sound to confirm the request. */
fun beep() {
  NativeTTS.tts("Request Confirmed")
}

/**
 * Sends a request to Gemini and handles the response.
 *
 * @param input The input string to send to Gemini.
 */
fun ask(input: String) {
  runBlocking {
    var gemini = generateContent(input).replace("*", "")
    if (cbLanguage.selectedItem!! != "en") {
      gemini = generateContent("Translate $gemini to ${cbLanguage.selectedItem}")
    }
    launch {
      if (gemini.split(" ").size > maxWords) {
        NativeTTS.tts("The response is over $maxWords words.")
      } else {
        NativeTTS.tts(gemini)
      }
    }
    Thread {
        showScrollableMessageDialog(
          null,
          gemini,
          "Gemini is responding to ${
          input.remove(
            "Answer the request while staying concise but without contractions: "
          ).replaceFirst("Translate ", "").replaceFirst(" to ${cbLanguage.selectedItem}", "")
        }",
          JOptionPane.INFORMATION_MESSAGE,
        )
      }
      .start()
  }
}
