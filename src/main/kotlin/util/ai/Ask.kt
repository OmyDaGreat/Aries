package util.ai

import aries.audio.LiveMic
import aries.visual.SharedState.selectedCountry
import aries.visual.SharedState.selectedLanguage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import util.audio.NativeTTS
import util.extension.ScrollOption
import util.extension.remove

/**
 * Sends a request to Gemini and handles the response.
 *
 * @param input The input string to send to Gemini.
 */
fun ask(input: String) =
    runBlocking {
        var gemini = generateContent(input).replace("*", "")
        gemini =
            generateContent(
                "Translate \"$gemini\" to $selectedLanguage-$selectedCountry if not already in that language. If it is already in that language, return the same text without any precursor.",
            )
        launch {
            if (gemini.split(" ").size > LiveMic.maxWords) {
                NativeTTS.tts("The response is over ${LiveMic.maxWords} words.")
            } else {
                NativeTTS.tts(gemini)
            }
        }
        ScrollOption.requestScrollableMessageDialog(
            "Gemini is responding to ${
                input.remove("Answer the request while staying concise but without contractions: ")
            }",
            gemini,
        )
    }
