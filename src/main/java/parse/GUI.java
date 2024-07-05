package parse;

import javax.swing.*;

import com.formdev.flatlaf.*;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import util.extension.SwingExtension;
import util.extension.SwingExtension.JComponentConstraints;
import util.NativeTTS;

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
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.setLocation(430, 100);


    JPanel panel = new JPanel();
    panel.setLayout(null);

    frame.add(panel);

    String[] languages = { "en","es", "CHOICE 3","CHOICE 4","CHOICE 5","CHOICE 6"};

    final JComboBox<String> cbLanguage = new JComboBox<>(languages);
    cbLanguage.setBounds(50, 50, 80, 20);
    cbLanguage.addActionListener(e -> {
        try {
            NativeTTS.voiceLanguage(Objects.requireNonNull(cbLanguage.getSelectedItem()).toString());
          log.info(cbLanguage.getSelectedItem());
        } catch (IOException ex) {
          log.error("Couldn't set the voice language", ex);
        }
    });

    String[] Countrys = { "US","es", "CHOICE 3","CHOICE 4","CHOICE 5","CHOICE 6"};
    final JComboBox<String> cbCountry = new JComboBox<>(Countrys);
    cbCountry.setBounds(150, 50, 80, 20);
    cbCountry.addActionListener(e -> {
      try {
        NativeTTS.voiceLanguage(Objects.requireNonNull(cbCountry.getSelectedItem()).toString());
        log.info(cbCountry.getSelectedItem());
      } catch (IOException ex) {
        log.error("Couldn't set the voice country", ex);
      }
    });

    JButton btn = new JButton("Parse");
    panel.add(btn);

    panel.addAll(
        new JComponentConstraints(cbLanguage, "span, growx"),
        new JComponentConstraints(cbCountry, "span, growx"));

    frame.setVisible(true);
    frame.getRootPane().setDefaultButton(btn);

    SystemTrayIcon.run();
  }
}
