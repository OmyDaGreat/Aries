package parse.gui

import com.formdev.flatlaf.FlatDarkLaf
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
        frame.setLocation(430, 100)

        val panel = JPanel(MigLayout("", "[][grow]", "[]10[]"))
        frame.add(panel)

        val languages = arrayOf("en", "es", "zh", "hi", "ar", "fr")
        cbLanguage = JComboBox<String>(languages)

        val countries = arrayOf("US", "GB", "CN", "IN", "MX", "CA")
        cbCountry = JComboBox<String>(countries)

        val btn = getBtn(cbLanguage, cbCountry)

        panel.addAll(
          cbLanguage to "cell 0 0, growx",
          cbCountry to "cell 1 0, growx",
          btn to "cell 0 1 2 1, growx"
        )

        frame.isVisible = true
        frame.rootPane.defaultButton = btn

        SystemTrayIcon.run()
      }

    private fun getBtn(cbLanguage: JComboBox<String>, cbCountry: JComboBox<String>): JButton {
      val btn = JButton("Parse")
      btn.addActionListener {_: ActionEvent? ->
        val selectedLanguage = cbLanguage.selectedItem?.toString()
        val selectedCountry = cbCountry.selectedItem?.toString()
        try {
          NativeTTS.voiceLanguage(selectedLanguage)
          log.debug("Language set to: {}", selectedLanguage)
          NativeTTS.voiceCountry(selectedCountry)
          log.debug("Country set to: {}", selectedCountry)
        } catch (ex: IOException) {
          log.error("Couldn't set the voice language or country", ex)
        }
      }
      return btn
    }
  }
}