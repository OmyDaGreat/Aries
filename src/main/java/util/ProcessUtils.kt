package util

import kotlinx.coroutines.delay
import util.audio.NativeTTS
import kotlinx.datetime.*

suspend fun setAlarm(time: String) {
  val validationPrompt = "Convert the given time string to ISO-8601 Duration format. If just a time was given, use today's date with the given time (unless the time has passed, in which case you should use tomorrow): $time"
  val validationResponse = generateContent(validationPrompt).trim()

  try {
    val alarmDuration = Instant.parse(validationResponse)
    val now = Clock.System.now()
    val delayDuration = alarmDuration - now

    if (delayDuration.inWholeMilliseconds > 0) {
      println("Setting alarm for $time")
      delay(delayDuration.inWholeMilliseconds)
      NativeTTS.tts("Alarm ringing for $time")
    } else {
      println("The specified time has already passed.")
      NativeTTS.tts("The specified time has already passed.")
    }
  } catch (e: Exception) {
    println("Invalid time format: $time")
    NativeTTS.tts("Invalid time format: $time")
  }
}