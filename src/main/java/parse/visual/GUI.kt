package parse.visual

import com.formdev.flatlaf.FlatDarkLaf
import io.github.jonelo.tts.engines.VoicePreferences
import net.miginfocom.swing.MigLayout
import parse.audio.LiveMic
import util.ResourcePath.getLocalResourcePath
import util.audio.NativeTTS
import util.extension.RobotUtils.special
import util.extension.ScrollOption.Companion.showScrollableMessageDialog
import util.extension.addAll
import util.extension.copyFileIfNotExists
import java.awt.Dimension
import java.awt.SystemTray
import javax.swing.*

class GUI {

  companion object {
    private lateinit var cbLanguage: JComboBox<String>
    private lateinit var cbCountry: JComboBox<String>
    private lateinit var cbGender: JComboBox<String>
    private lateinit var spMaxWords: JSpinner

    fun run() {
      SwingUtilities.invokeLater {setupGUI}
    }

    private val setupGUI: Unit
      get() {
        FlatDarkLaf.setup()
        JFrame.setDefaultLookAndFeelDecorated(true)
        val frame = JFrame("Aries").apply {
          defaultCloseOperation =
            if (SystemTray.isSupported()) WindowConstants.HIDE_ON_CLOSE else WindowConstants.EXIT_ON_CLOSE
          setSize(500, 600)
          setLocationRelativeTo(null)
        }

        val languages = arrayOf("en", "es", "zh", "hi", "ar", "fr")
        cbLanguage = JComboBox<String>(languages)

        val countries = arrayOf("US", "GB", "CN", "IN", "MX", "CA")
        cbCountry = JComboBox<String>(countries)

        val gender = arrayOf("MALE", "FEMALE")
        cbGender = JComboBox<String>(gender)

        spMaxWords = JSpinner(SpinnerNumberModel(40, 1, 100000, 1))

        val btn = getBtn(cbLanguage, cbCountry, cbGender, spMaxWords)

        val specialKeysButton = JButton("Show Special Keys").apply {
          addActionListener {
            val specialKeys = special.keys.joinToString("\n") {it.first}
            showScrollableMessageDialog(null, specialKeys, "Special Keys", JOptionPane.INFORMATION_MESSAGE)
          }
        }

        val textArea = JLabel("<html>$commandInfo</html>")
        val scrollPane = JScrollPane(textArea).apply {
          preferredSize = Dimension(450, 350)
        }

        val span2wrap = "span 2, wrap"
        val split2span = "split 2, span"
        val infoPanel = JPanel().apply {
          add(scrollPane)
        }
        val panel = JPanel(MigLayout("center", "[grow,fill]")).apply {
          addAll(
            JLabel("Language:") to split2span,
            cbLanguage to span2wrap,
            JLabel("Country:") to split2span,
            cbCountry to span2wrap,
            JLabel("Gender") to split2span,
            cbGender to span2wrap,
            JLabel("Max Words") to split2span,
            spMaxWords to span2wrap,
            btn to span2wrap,
            infoPanel to span2wrap,
            specialKeysButton to span2wrap
          )
        }

        frame.apply {
          add(panel)
          copyFileIfNotExists("voicePreferences.txt", getLocalResourcePath("voicePreferences.txt"))
          NativeTTS.loadVoicePreferences()
          updateGUIFromPreferences()
          isVisible = true
          rootPane.defaultButton = btn
        }

        SystemTrayManager(frame).setupSystemTray()
      }

    private fun getBtn(
      cbLanguage: JComboBox<String>,
      cbCountry: JComboBox<String>,
      cbGender: JComboBox<String>,
      spMaxWords: JSpinner,
    ): JButton {
      return JButton("Apply Settings").apply {
        addActionListener {
          val selectedLanguage = cbLanguage.selectedItem?.toString()
          val selectedCountry = cbCountry.selectedItem?.toString()
          val selectedGender = cbGender.selectedItem?.toString()
          val maxWords = spMaxWords.value as Int
          NativeTTS.voiceLanguage(selectedLanguage)
          NativeTTS.voiceCountry(selectedCountry)
          NativeTTS.voiceGender(VoicePreferences.Gender.valueOf(selectedGender!!))
          LiveMic.maxWords = maxWords
          NativeTTS.saveVoicePreferences()
        }
      }
    }

    private fun updateGUIFromPreferences() {
      val preferences = NativeTTS.voicePreferences
      cbLanguage.selectedItem = preferences.language
      cbCountry.selectedItem = preferences.country
      cbGender.selectedItem = preferences.gender.name
      spMaxWords.value = LiveMic.maxWords
    }
  }
}

private const val commandInfo = """
  "<strong>Hey Aries..."</strong><br/>
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
  - "enter": Adds a new line in notepad.
"""
