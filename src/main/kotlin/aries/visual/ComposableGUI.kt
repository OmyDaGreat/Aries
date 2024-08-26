package aries.visual

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import aries.audio.LiveMic.Companion.maxWords
import io.github.jonelo.tts.engines.VoicePreferences
import util.ResourcePath.getLocalResourcePath
import util.audio.NativeTTS
import util.audio.NativeTTS.Companion.loadVoicePreferences
import util.extension.Spinner
import util.extension.toRichHtmlString
import java.io.File
import java.io.FileWriter
import java.util.*

object SharedState {
  var selectedLanguage: String = ""
  var selectedCountry: String = ""
}

@Composable
@Preview
fun ComposableGUI(onCloseRequest: () -> Unit, icon: BitmapPainter) {
  Window(onCloseRequest = onCloseRequest, icon = icon) {
    writeVoicePreferencesToFile(getLocalResourcePath("voicePreferences.txt"))
    loadVoicePreferences()

    var selectedLanguage by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    updateGUIFromPreferences(
      setSelectedLanguage = { selectedLanguage = it },
      setSelectedCountry = { selectedCountry = it },
      setSelectedGender = { selectedGender = it },
    )

    val languages = Locale.getISOLanguages().toList()
    val countries = Locale.getISOCountries().toList()
    val genders = listOf("MALE", "FEMALE")

    selectedLanguage = languages[0]
    selectedCountry = countries[0]
    selectedGender = genders[0]

    // Update SharedState with the selected language and country
    SharedState.selectedLanguage = selectedLanguage
    SharedState.selectedCountry = selectedCountry

    Column(modifier = Modifier.padding(16.dp)) {
      DropdownMenu(expanded = true, onDismissRequest = {}) {
        languages.forEach { language ->
          DropdownMenuItem(
            onClick = {
              selectedLanguage = language
              SharedState.selectedLanguage = language // Update SharedState
            }
          ) {
            Text(language)
          }
        }
      }
      Box {
        DropdownMenu(expanded = false, onDismissRequest = {}) {
          countries.forEach { country ->
            DropdownMenuItem(
              onClick = {
                selectedCountry = country
                SharedState.selectedCountry = country // Update SharedState
              }
            ) {
              Text(country)
            }
          }
        }
      }
      Box {
        DropdownMenu(expanded = false, onDismissRequest = {}) {
          genders.forEach { gender ->
            DropdownMenuItem(onClick = { selectedGender = gender }) { Text(gender) }
          }
        }
      }
      Text(commandInfo.toRichHtmlString())

      Button(
        onClick = {
          NativeTTS.voiceLanguage(selectedLanguage)
          NativeTTS.voiceCountry(selectedCountry)
          NativeTTS.voiceGender(VoicePreferences.Gender.valueOf(selectedGender))
          NativeTTS.saveVoicePreferences()
        }
      ) {
        Text("Apply Settings")
      }

      Spinner(
        value = maxWords,
        onValueChange = { newValue -> maxWords = newValue },
        range = 1..100000,
      )
    }
  }
}

private fun writeVoicePreferencesToFile(filePath: String) {
  val content =
    """
        language=en
        country=US
        gender=MALE
        maxWords=40
      """
      .trimIndent()
  File(filePath).apply {
    if (!exists()) {
      createNewFile()
      FileWriter(this).use { it.write(content) }
    }
  }
}

private fun updateGUIFromPreferences(
  setSelectedLanguage: (String) -> Unit,
  setSelectedCountry: (String) -> Unit,
  setSelectedGender: (String) -> Unit,
) {
  val preferences = NativeTTS.voicePreferences
  setSelectedLanguage(preferences.language)
  setSelectedCountry(preferences.country)
  setSelectedGender(preferences.gender.name)
}

private const val commandInfo =
  """
<strong>Hey Aries...</strong><br/>
- "write special [text]": Writes special characters.<br/>
- "write [text]": Writes the specified text.<br/>
- "search [query]": Searches Google for the specified query.<br/>
- "ask gemini [query]": Queries gemini and processes the response.<br/>
- "[query]": Queries gemini and processes the response.<br/>
<strong>Keyboard Commands:</strong><br/>
- "control shift [text]": Types text with Control + Shift modifier.<br/>
- "shift [text]": Types text with Shift modifier.<br/>
- "control [text]": Types text with Control modifier.<br/>
- "command [text]": Types text with Command modifier (on macOS).<br/>
- "arrow [direction(s)]": Moves the arrow keys in the specified direction(s).<br/>
- "cap": Presses the Caps Lock key.<br/>
- "switch window [number]": Switches to the specified window.<br/>
- "f [number]": Presses the specified function key.<br/>
- "alt f [number]": Presses ALT + specified function key.<br/>
- "windows shift [text]": Types text with Windows + Shift modifier.<br/>
- "windows [text]": Types text with Windows modifier.<br/>
- "command shift [text]": Types text with Command + Shift modifier.<br/>
- "enter": Presses the enter key.<br/>
- "tab": Presses the Tab key.<br/>
<strong>Mouse Commands:</strong><br/>
- "left click": Performs a left mouse click.<br/>
- "right click": Performs a right mouse click.<br/>
- "left press": Presses the left mouse button.<br/>
- "left release": Releases the left mouse button.<br/>
- "right press": Presses the right mouse button.<br/>
- "right release": Releases the right mouse button.<br/>
- "middle click": Performs a middle mouse click.<br/>
- "mouse [direction(s)]": Moves the mouse in the specified direction(s).<br/>
- "scroll [direction(s)]": Scrolls the mouse wheel in the specified direction(s).<br/>
<strong>Notepad-specific commands:</strong><br/>
- "open notepad": Opens the notepad.<br/>
- "close notepad": Closes the notepad.<br/>
- "open new": Opens a new file in notepad.<br/>
- "delete everything": Deletes all text in notepad.<br/>
- "save file [name]": Saves the file with the specified name.<br/>
- "save file as [name]": Saves the file with the specified name (alternative phrasing).
"""
