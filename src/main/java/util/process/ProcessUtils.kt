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

suspend fun setAlarm(time: String) {
  val validationPrompt =
    "Convert the given time string to ISO-8601 format. If just a time was given, use today's date with the given time (unless the time has passed, in which case you should use tomorrow): $time"
  val validationResponse = generateContent(validationPrompt).trim()
  val delayDuration = Instant.parse(validationResponse) - Clock.System.now()

  if (delayDuration.inWholeMilliseconds > 0) {
    println("Setting alarm for $time")
    delay(delayDuration.inWholeMilliseconds)
    NativeTTS.tts("Alarm ringing for $time")
  } else {
    println("The specified time has already passed.")
    NativeTTS.tts("The specified time has already passed.")
  }
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
