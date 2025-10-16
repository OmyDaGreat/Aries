package aries.visual

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import aries.audio.LiveMic.Companion.maxWords
import aries.visual.SharedState.selectedCountry
import aries.visual.SharedState.selectedGender
import aries.visual.SharedState.selectedLanguage
import co.touchlab.kermit.Logger
import io.github.jonelo.tts.engines.VoicePreferences
import util.ResourcePath.getLocalResourcePath
import util.audio.NativeTTS
import util.audio.NativeTTS.Companion.loadVoicePreferences
import util.audio.NativeTTS.Companion.saveVoicePreferences
import util.extension.LocaleUtils
import util.extension.ScrollOption
import util.extension.ScrollableDropdownMenu
import java.io.File
import java.io.FileWriter

object SharedState {
    var selectedLanguage: String = ""
    var selectedCountry: String = ""
    var selectedGender: String = ""
}

@Composable
fun ComposableGUI(
    onCloseRequest: () -> Unit,
    icon: BitmapPainter,
) {
    Logger.d("ComposableGUI called")
    Window(onCloseRequest = onCloseRequest, icon = icon, title = "Aries Settings") {
        writeVoicePreferencesToFile(getLocalResourcePath("voicePreferences.txt"))
        loadVoicePreferences()

        updateGUIFromPreferences(
            setSelectedLanguage = { selectedLanguage = it },
            setSelectedCountry = { selectedCountry = it },
            setSelectedGender = { selectedGender = it },
        )

        val languages = LocaleUtils.getFormattedLanguages()
        val countries = LocaleUtils.getFormattedCountries()
        val genders = listOf("Male" to "MALE", "Female" to "FEMALE")

        // Find current display names for selected values
        val selectedLanguageDisplay =
            languages.find {
                LocaleUtils.extractLanguageCode(it) == selectedLanguage
            } ?: selectedLanguage
        val selectedCountryDisplay =
            countries.find {
                LocaleUtils.extractCountryCode(it) == selectedCountry
            } ?: selectedCountry
        val selectedGenderDisplay = genders.find { it.second == selectedGender }?.first ?: selectedGender

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
                    // Header
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = MaterialTheme.colors.primary,
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Aries Voice Settings",
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.padding(bottom = 4.dp),
                            )
                            Text(
                                "Configure your voice preferences and commands",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Settings Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Voice Preferences",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onSurface,
                                modifier = Modifier.padding(bottom = 16.dp),
                            )

                            ScrollableDropdownMenu(
                                options = languages,
                                initialSelectedItem = selectedLanguageDisplay,
                                label = "Language",
                                onItemSelected = { displayName ->
                                    val languageCode = LocaleUtils.extractLanguageCode(displayName)
                                    Logger.d("Selected language set to: $languageCode")
                                    selectedLanguage = languageCode
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            ScrollableDropdownMenu(
                                options = countries,
                                initialSelectedItem = selectedCountryDisplay,
                                label = "Country",
                                onItemSelected = { displayName ->
                                    val countryCode = LocaleUtils.extractCountryCode(displayName)
                                    Logger.d("Selected country set to: $countryCode")
                                    selectedCountry = countryCode
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            ScrollableDropdownMenu(
                                options = genders.map { it.first },
                                initialSelectedItem = selectedGenderDisplay,
                                label = "Voice Gender",
                                searchEnabled = false, // No need for search with only 2 options
                                onItemSelected = { displayName ->
                                    val genderCode = genders.find { it.first == displayName }?.second ?: displayName
                                    Logger.d("Selected gender set to: $genderCode")
                                    selectedGender = genderCode
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Actions Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Button(
                                    onClick = {
                                        NativeTTS.voiceLanguage(selectedLanguage)
                                        NativeTTS.voiceCountry(selectedCountry)
                                        NativeTTS.voiceGender(VoicePreferences.Gender.valueOf(selectedGender))
                                        saveVoicePreferences()
                                        Logger.i("Selected language: $selectedLanguage")
                                        Logger.i("Selected country: $selectedCountry")
                                        Logger.i("Selected gender: $selectedGender")
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors =
                                        ButtonDefaults.buttonColors(
                                            backgroundColor = MaterialTheme.colors.primary,
                                            contentColor = MaterialTheme.colors.onPrimary,
                                        ),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = ButtonDefaults.elevation(defaultElevation = 2.dp),
                                ) {
                                    Text("Apply Settings", style = MaterialTheme.typography.button)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                var maxWordsText by remember { mutableStateOf(maxWords.toString()) }
                                TextField(
                                    value = maxWordsText,
                                    onValueChange = { newNum ->
                                        Logger.d("New max words: $newNum")
                                        val newValue = newNum.toIntOrNull()?.coerceIn(1..100000) ?: maxWords
                                        maxWordsText = newValue.toString()
                                        maxWords = newValue
                                        saveVoicePreferences()
                                    },
                                    label = { Text("Max Words") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors =
                                        TextFieldDefaults.textFieldColors(
                                            backgroundColor = MaterialTheme.colors.surface,
                                            focusedIndicatorColor = MaterialTheme.colors.primary,
                                        ),
                                    shape = RoundedCornerShape(8.dp),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Commands Reference Section
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Voice Commands Reference",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onSurface,
                                modifier = Modifier.padding(bottom = 12.dp),
                            )
                            Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(
                                            MaterialTheme.colors.background,
                                            RoundedCornerShape(8.dp),
                                        ).padding(16.dp)
                                        .verticalScroll(rememberScrollState()),
                            ) {
                                Text(
                                    COMMAND_INFO,
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onBackground,
                                    lineHeight = MaterialTheme.typography.body2.lineHeight,
                                )
                            }
                        }
                    }
                }

                println("Active dialogs: ${ScrollOption.activeDialogs}")
                ScrollOption.DialogHost()
            }
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
        """.trimIndent()
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

private const val COMMAND_INFO =
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
