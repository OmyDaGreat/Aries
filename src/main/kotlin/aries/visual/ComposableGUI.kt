package aries.visual

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
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
    Window(
        onCloseRequest = onCloseRequest,
        icon = icon,
        title = "Aries Settings",
        state = WindowState(width = 900.dp, height = 700.dp),
    ) {
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
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                // Responsive calculations based on window size
                val isCompact = maxWidth < 600.dp
                val isMedium = maxWidth in 600.dp..900.dp
                val isExpanded = maxWidth > 900.dp

                val padding = when {
                    isCompact -> 12.dp
                    isMedium -> 16.dp
                    else -> 24.dp
                }

                val cardElevation = when {
                    isCompact -> 1.dp
                    else -> 2.dp
                }

                val headerElevation = when {
                    isCompact -> 2.dp
                    else -> 4.dp
                }

                val spacing = when {
                    isCompact -> 12.dp
                    isMedium -> 16.dp
                    else -> 20.dp
                }

                val cornerRadius = when {
                    isCompact -> 8.dp
                    else -> 12.dp
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (isCompact) {
                        // Compact layout - single column, scrollable
                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            HeaderCard(headerElevation, cornerRadius, isCompact)
                            Spacer(modifier = Modifier.height(spacing))
                            SettingsCard(
                                languages = languages,
                                countries = countries,
                                genders = genders,
                                selectedLanguageDisplay = selectedLanguageDisplay,
                                selectedCountryDisplay = selectedCountryDisplay,
                                selectedGenderDisplay = selectedGenderDisplay,
                                cardElevation = cardElevation,
                                cornerRadius = cornerRadius,
                                spacing = spacing,
                                isCompact = isCompact
                            )
                            Spacer(modifier = Modifier.height(spacing))
                            CommandsCard(cardElevation, cornerRadius, spacing, isCompact)
                        }
                    } else {
                        // Medium/Expanded layout - use available space efficiently
                        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                            HeaderCard(headerElevation, cornerRadius, isCompact)
                            Spacer(modifier = Modifier.height(spacing))

                            Row(
                                modifier = Modifier.weight(1f).fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(spacing)
                            ) {
                                // Left column - Settings
                                Column(
                                    modifier = Modifier
                                        .weight(if (isMedium) 1f else 0.45f)
                                        .fillMaxHeight()
                                ) {
                                    SettingsCard(
                                        languages = languages,
                                        countries = countries,
                                        genders = genders,
                                        selectedLanguageDisplay = selectedLanguageDisplay,
                                        selectedCountryDisplay = selectedCountryDisplay,
                                        selectedGenderDisplay = selectedGenderDisplay,
                                        cardElevation = cardElevation,
                                        cornerRadius = cornerRadius,
                                        spacing = spacing,
                                        isCompact = isCompact,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                // Right column - Commands Reference
                                Column(
                                    modifier = Modifier
                                        .weight(if (isMedium) 1f else 0.55f)
                                        .fillMaxHeight()
                                ) {
                                    CommandsCard(
                                        cardElevation,
                                        cornerRadius,
                                        spacing,
                                        isCompact,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    ScrollOption.DialogHost()
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(elevation: Dp, cornerRadius: Dp, isCompact: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = elevation,
        shape = RoundedCornerShape(cornerRadius),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 12.dp else 20.dp)) {
            Text(
                "Aries Voice Settings",
                style = if (isCompact) MaterialTheme.typography.h6 else MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                "Configure your voice preferences and commands",
                style = if (isCompact) MaterialTheme.typography.caption else MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onPrimary.copy(alpha = 0.9f),
            )
        }
    }
}

@Composable
private fun SettingsCard(
    languages: List<String>,
    countries: List<String>,
    genders: List<Pair<String, String>>,
    selectedLanguageDisplay: String,
    selectedCountryDisplay: String,
    selectedGenderDisplay: String,
    cardElevation: Dp,
    cornerRadius: Dp,
    spacing: Dp,
    isCompact: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = cardElevation,
        shape = RoundedCornerShape(cornerRadius),
    ) {
        Column(
            modifier = Modifier
                .padding(if (isCompact) 12.dp else 16.dp)
                .then(if (!isCompact) Modifier.verticalScroll(rememberScrollState()) else Modifier)
        ) {
            Text(
                "Voice Preferences",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = spacing),
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

            Spacer(modifier = Modifier.height(spacing))

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

            Spacer(modifier = Modifier.height(spacing))

            ScrollableDropdownMenu(
                options = genders.map { it.first },
                initialSelectedItem = selectedGenderDisplay,
                label = "Voice Gender",
                searchEnabled = false,
                onItemSelected = { displayName ->
                    val genderCode = genders.find { it.first == displayName }?.second ?: displayName
                    Logger.d("Selected gender set to: $genderCode")
                    selectedGender = genderCode
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(spacing))

            // Actions Section
            if (isCompact) {
                // Vertical layout for compact mode
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                        label = { Text("Max Words", fontWeight = FontWeight.Medium) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.background,
                            focusedIndicatorColor = MaterialTheme.colors.primary,
                            unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            focusedLabelColor = MaterialTheme.colors.primary,
                        ),
                        shape = RoundedCornerShape(10.dp),
                    )

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
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 3.dp,
                            pressedElevation = 6.dp,
                            hoveredElevation = 4.dp
                        ),
                    ) {
                        Text(
                            "Apply Settings",
                            style = MaterialTheme.typography.button,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                // Horizontal layout for medium/expanded mode
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                        label = { Text("Max Words", fontWeight = FontWeight.Medium) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.background,
                            focusedIndicatorColor = MaterialTheme.colors.primary,
                            unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            focusedLabelColor = MaterialTheme.colors.primary,
                        ),
                        shape = RoundedCornerShape(10.dp),
                    )

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
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 3.dp,
                            pressedElevation = 6.dp,
                            hoveredElevation = 4.dp
                        ),
                    ) {
                        Text(
                            "Apply Settings",
                            style = MaterialTheme.typography.button,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommandsCard(
    elevation: Dp,
    cornerRadius: Dp,
    spacing: Dp,
    isCompact: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = elevation,
        shape = RoundedCornerShape(cornerRadius),
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 12.dp else 16.dp).fillMaxSize()) {
            Text(
                "Voice Commands Reference",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = spacing),
            )
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(spacing))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        MaterialTheme.colors.background.copy(alpha = 0.5f),
                        RoundedCornerShape(8.dp),
                    )
                    .padding(if (isCompact) 8.dp else 12.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = buildAnnotatedString {
                        val lines = COMMAND_INFO.split("\n")
                        lines.forEachIndexed { index, line ->
                            when {
                                // Section headers (lines without bullet points and not empty)
                                line.isNotBlank() && !line.trimStart().startsWith("•") -> {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.primary
                                        )
                                    ) {
                                        append(line)
                                    }
                                }
                                // Regular bullet point lines
                                else -> {
                                    append(line)
                                }
                            }
                            // Add newline except for the last line
                            if (index < lines.size - 1) {
                                append("\n")
                            }
                        }
                    },
                    style = if (isCompact) MaterialTheme.typography.caption else MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    lineHeight = if (isCompact) 16.sp else 20.sp,
                )
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
    """Hey Aries...
• "write special [text]": Writes special characters.
• "write [text]": Writes the specified text.
• "search [query]": Searches Google for the specified query.
• "ask gemini [query]": Queries gemini and processes the response.
• "[query]": Queries gemini and processes the response.

Keyboard Commands:
• "control shift [text]": Types text with Control + Shift modifier.
• "shift [text]": Types text with Shift modifier.
• "control [text]": Types text with Control modifier.
• "command [text]": Types text with Command modifier (on macOS).
• "arrow [direction(s)]": Moves the arrow keys in the specified direction(s).
• "cap": Presses the Caps Lock key.
• "switch window [number]": Switches to the specified window.
• "f [number]": Presses the specified function key.
• "alt f [number]": Presses ALT + specified function key.
• "windows shift [text]": Types text with Windows + Shift modifier.
• "windows [text]": Types text with Windows modifier.
• "command shift [text]": Types text with Command + Shift modifier.
• "enter": Presses the enter key.
• "tab": Presses the Tab key.

Mouse Commands:
• "left click": Performs a left mouse click.
• "right click": Performs a right mouse click.
• "left press": Presses the left mouse button.
• "left release": Releases the left mouse button.
• "right press": Presses the right mouse button.
• "right release": Releases the right mouse button.
• "middle click": Performs a middle mouse click.
• "mouse [direction(s)]": Moves the mouse in the specified direction(s).
• "scroll [direction(s)]": Scrolls the mouse wheel in the specified direction(s).

Notepad-specific commands:
• "open notepad": Opens the notepad.
• "close notepad": Closes the notepad.
• "open new": Opens a new file in notepad.
• "delete everything": Deletes all text in notepad.
• "save file [name]": Saves the file with the specified name.
• "save file as [name]": Saves the file with the specified name (alternative phrasing)."""
