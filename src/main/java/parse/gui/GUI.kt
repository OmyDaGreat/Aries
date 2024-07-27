package parse.gui

import com.formdev.flatlaf.FlatDarkLaf
import io.github.jonelo.tts.engines.VoicePreferences
import net.miginfocom.swing.MigLayout
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import util.extension.addAll
import util.listen.NativeTTS
import java.awt.event.ActionEvent
import java.io.IOException
import javax.swing.*

class GUI {

  companion object {
    lateinit var cbLanguage: JComboBox<String>
    lateinit var cbCountry: JComboBox<String>
    lateinit var cbGender: JComboBox<String>
    private val log: Logger = LogManager.getLogger()

    fun run() {
      SwingUtilities.invokeLater {GUI}
    }

    private val GUI: Unit
      get() {
        FlatDarkLaf.setup()

        JFrame.setDefaultLookAndFeelDecorated(true)
        val frame = JFrame("ParseButPro")
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(500, 500)
        frame.setLocationRelativeTo(null) // Center the frame on the screen

        val panel = JPanel(MigLayout("fill, insets 0", "[center]", "[center]10[center]"))
        frame.add(panel)

        val languages = arrayOf("en", "es", "zh", "hi", "ar", "fr")
        cbLanguage = JComboBox<String>(languages)

        val countries = arrayOf("US", "GB", "CN", "IN", "MX", "CA")
        cbCountry = JComboBox<String>(countries)

        val gender = arrayOf("MALE", "FEMALE")
        cbGender = JComboBox<String>(gender)

        val btn = getBtn(cbLanguage, cbCountry, cbGender)

        panel.addAll(
          cbLanguage to "cell 0 0, growx",
          cbCountry to "cell 0 1, growx",
          cbGender to "cell 0 2, growx",
          btn to "cell 0 3, growx"
        )

        frame.isVisible = true
        frame.rootPane.defaultButton = btn
      }

    private fun getBtn(cbLanguage: JComboBox<String>, cbCountry: JComboBox<String>, cbGender: JComboBox<String>): JButton {
      val btn = JButton("Parse")
      btn.addActionListener {_: ActionEvent? ->
        val selectedLanguage = cbLanguage.selectedItem?.toString()
        val selectedCountry = cbCountry.selectedItem?.toString()
        val selectedGender = cbGender.selectedItem?.toString()
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
        } catch (ex: IOException) {
          log.error("Couldn't set the voice language or country", ex)
        }
      }
      return btn
    }
  }
}