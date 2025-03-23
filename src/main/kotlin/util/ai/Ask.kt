package util.ai

import aries.audio.LiveMic
import aries.visual.SharedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
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
        val selectedLanguage = SharedState.selectedLanguage // Access the selectedLanguage
        val selectedCountry = SharedState.selectedCountry // Access the selectedCountry
        gemini =
            generateContent(
                "Translate \"$gemini\" to $selectedLanguage-$selectedCountry if not already in that language. If it is already in that language, return the same text without any precursor.",
            )
        launch {
            if (gemini.split(" ").size > LiveMic.Companion.maxWords) {
                NativeTTS.Companion.tts("The response is over ${LiveMic.Companion.maxWords} words.")
            } else {
                NativeTTS.Companion.tts(gemini)
            }
        }
        launch(Dispatchers.Swing) {
            ScrollOption.showScrollableMessageDialog(
                null,
                gemini,
                "Gemini is responding to ${
                    input.remove("Answer the request while staying concise but without contractions: ")
                }",
            )
        }
    }
