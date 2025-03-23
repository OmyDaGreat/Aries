package util.process

import aries.audio.LiveMic.Companion.maxWords
import aries.visual.SharedState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import util.audio.NativeTTS
import util.extension.ScrollOption.showScrollableMessageDialog
import util.extension.remove
import util.generateContent

/**
 * Sets an alarm for the specified time.
 *
 * @param time The time string to set the alarm for.
 */
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
        val selectedLanguage = SharedState.selectedLanguage // Access the selectedLanguage
        val selectedCountry = SharedState.selectedCountry // Access the selectedCountry
        gemini =
            generateContent(
                "Translate \"$gemini\" to $selectedLanguage-$selectedCountry if not already in that language. If it is already in that language, return the same text.",
            )
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
                    input.remove("Answer the request while staying concise but without contractions: ")
                }",
            )
        }.start()
    }
}
