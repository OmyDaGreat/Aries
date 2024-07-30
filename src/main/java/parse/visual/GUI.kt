package parse.visual

import com.formdev.flatlaf.FlatDarkLaf
import io.github.jonelo.tts.engines.VoicePreferences
import net.miginfocom.swing.MigLayout
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import parse.audio.LiveMic
import util.extension.addAll
import util.listen.NativeTTS
import java.awt.SystemTray
import java.awt.event.ActionEvent
import java.io.IOException
import javax.swing.*

class GUI {

  companion object {
    private lateinit var cbLanguage: JComboBox<String>
    private lateinit var cbCountry: JComboBox<String>
    private lateinit var cbGender: JComboBox<String>
    private lateinit var spMaxWords: JSpinner
    private val log: Logger = LogManager.getLogger()

    fun run() {
      SwingUtilities.invokeLater { GUI }
    }

    private val GUI: Unit
      get() {
        FlatDarkLaf.setup()
        JFrame.setDefaultLookAndFeelDecorated(true)
        val frame = JFrame("ParseButPro")
        frame.defaultCloseOperation = if (SystemTray.isSupported()) WindowConstants.HIDE_ON_CLOSE else WindowConstants.EXIT_ON_CLOSE
        frame.setSize(500, 425)
        frame.setLocationRelativeTo(null)

        // Adjusted MigLayout to center components with whitespace
        val panel = JPanel(MigLayout("center", "[grow,fill]"))
        frame.add(panel)

        val languages = arrayOf("en", "es", "zh", "hi", "ar", "fr")
        cbLanguage = JComboBox<String>(languages)

        val countries = arrayOf("US", "GB", "CN", "IN", "MX", "CA")
        cbCountry = JComboBox<String>(countries)

        val gender = arrayOf("MALE", "FEMALE")
        cbGender = JComboBox<String>(gender)

        spMaxWords = JSpinner(SpinnerNumberModel(40, 1, 100000, 1))

        val btn = getBtn(cbLanguage, cbCountry, cbGender, spMaxWords)

        // Define the infoPanel
        val infoPanel = JPanel()
        val infoLabel = JLabel("<html>$commandInfo</html>")
        infoPanel.add(infoLabel)

        // Add the infoPanel to the main panel
        val span2wrap = "span 2, wrap"
        panel.addAll(
          cbLanguage to span2wrap,
          cbCountry to span2wrap,
          cbGender to span2wrap,
          spMaxWords to span2wrap,
          btn to span2wrap,
          infoPanel to span2wrap
        )

        frame.isVisible = true
        frame.rootPane.defaultButton = btn

        SystemTrayManager(frame).setupSystemTray()
      }

    private fun getBtn(cbLanguage: JComboBox<String>, cbCountry: JComboBox<String>, cbGender: JComboBox<String>, spMaxWords: JSpinner): JButton {
      val btn = JButton("Parse")
      btn.addActionListener { _: ActionEvent? ->
        val selectedLanguage = cbLanguage.selectedItem?.toString()
        val selectedCountry = cbCountry.selectedItem?.toString()
        val selectedGender = cbGender.selectedItem?.toString()
        val maxWords = spMaxWords.value as Int
        try {
          NativeTTS.voiceLanguage(selectedLanguage)
          log.debug("Language set to: {}", selectedLanguage)
          NativeTTS.voiceCountry(selectedCountry)
          log.debug("Country set to: {}", selectedCountry)
          if (selectedGender == "FEMALE") {
            NativeTTS.voiceGender(VoicePreferences.Gender.FEMALE)
          } else if (selectedGender == "MALE") {
            NativeTTS.voiceGender(VoicePreferences.Gender.MALE)
          }
          log.debug("Gender set to: {}", selectedGender)
          LiveMic.maxWords = maxWords
          log.debug("Max Words set to: {}", maxWords)
        } catch (ex: IOException) {
          log.error("Couldn't set the voice language or country", ex)
        }
      }
      return btn
    }
  }
}

private val commandInfo = """
  "Hey Aries..."<br/>
  - "write special [text]": Writes special characters.<br/>
  - "write [text]": Writes the specified text.<br/>
  - "search [query]": Searches Google for the specified query.<br/>
  - "mouse [coordinates]": Moves the mouse to the specified coordinates.<br/>
  - "ask gemini [question]": Asks gemini a question and processes the response.<br/>
  Notepad-specific commands:<br/>
  - "open notepad": Opens the notepad.<br/>
  - "close notepad": Closes the notepad.<br/>
  - "open new": Opens a new file in notepad.<br/>
  - "delete everything": Deletes all text in notepad.<br/>
  - "save file [name]": Saves the file with the specified name.<br/>
  - "enter": Adds a new line in notepad.
""".trimIndent()