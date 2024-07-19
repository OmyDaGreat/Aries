package parse.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import util.listen.NativeTTS;
import util.extension.SwingExtension;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

@Log4j2
@UtilityClass
@ExtensionMethod(SwingExtension.class)
public class GUI {
  public static void run() {
    SwingUtilities.invokeLater(GUI::getGUI);
  }

  @SneakyThrows
  private static void getGUI() {
    FlatDarkLaf.setup();

    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("ParseButPro");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.setLocation(430, 100);

    JPanel panel = new JPanel(new MigLayout("", "[][grow]", "[]10[]"));
    frame.add(panel);

    String[] languages = {"en", "es", "zh", "hi", "ar", "fr"};
    final JComboBox<String> cbLanguage = new JComboBox<>(languages);

    String[] countries = {"US", "GB", "CN", "IN", "MX", "CA"};
    final JComboBox<String> cbCountry = new JComboBox<>(countries);

    JButton btn = getBtn(cbLanguage, cbCountry);

    panel.add(cbLanguage, "cell 0 0, growx");
    panel.add(cbCountry, "cell 1 0, growx");
    panel.add(btn, "cell 0 1 2 1, growx");

    frame.setVisible(true);
    frame.getRootPane().setDefaultButton(btn);

    SystemTrayIcon.run();
  }

  private static @NotNull JButton getBtn(JComboBox<String> cbLanguage, JComboBox<String> cbCountry) {
    JButton btn = new JButton("Parse");
    btn.addActionListener(e -> {
      String selectedLanguage = Objects.requireNonNull(cbLanguage.getSelectedItem()).toString();
      String selectedCountry = Objects.requireNonNull(cbCountry.getSelectedItem()).toString();
      try {
        NativeTTS.voiceLanguage(selectedLanguage);
        log.debug("Language set to: {}", selectedLanguage);
        NativeTTS.voiceCountry(selectedCountry);
        log.debug("Country set to: {}", selectedCountry);
      } catch (IOException ex) {
        log.error("Couldn't set the voice language or country", ex);
      }
    });
    return btn;
  }
}