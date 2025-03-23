package util.process

import co.touchlab.kermit.Logger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import util.ai.generateContent
import util.audio.NativeTTS

/**
 * Sets an alarm for the specified time.
 *
 * @param time The time string to set the alarm for.
 */
@OptIn(DelicateCoroutinesApi::class)
fun setAlarm(time: String) =
    GlobalScope.launch {
        val validationPrompt =
            "Convert the given time string to ISO-8601 format. If just a time was given, use today's date with the given time (unless the time has passed, in which case you should use tomorrow): $time"
        val validationResponse = generateContent(validationPrompt).trim()
        val delayDuration = Instant.parse(validationResponse) - Clock.System.now()

        if (delayDuration.inWholeMilliseconds > 0) {
            Logger.d("Setting alarm for $time")
            delay(delayDuration.inWholeMilliseconds)
            NativeTTS.tts("Alarm ringing for $time")
        } else {
            Logger.d("The specified time has already passed.")
            NativeTTS.tts("The specified time has already passed.")
        }
    }

/** Plays a "Request Confirmed" sound to confirm the request. */
fun beep() {
    NativeTTS.tts("Request Confirmed")
}
