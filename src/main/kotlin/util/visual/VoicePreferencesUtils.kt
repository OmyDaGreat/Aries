package util.visual

import util.ResourcePath.getLocalResourcePath
import util.audio.NativeTTS
import java.io.File
import java.io.FileWriter

object SharedState {
    var selectedLanguage: String = ""
    var selectedCountry: String = ""
    var selectedGender: String = ""
}

fun writeVoicePreferencesToFile(filePath: String) {
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

fun updateGUIFromPreferences(
    setSelectedLanguage: (String) -> Unit,
    setSelectedCountry: (String) -> Unit,
    setSelectedGender: (String) -> Unit,
) {
    val preferences = NativeTTS.voicePreferences
    setSelectedLanguage(preferences.language)
    setSelectedCountry(preferences.country)
    setSelectedGender(preferences.gender.name)
}

fun initializeVoicePreferences() {
    writeVoicePreferencesToFile(getLocalResourcePath("voicePreferences.txt"))
    NativeTTS.loadVoicePreferences()
}
