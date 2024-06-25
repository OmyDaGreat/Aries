package parse;

import javax.swing.*;

import com.formdev.flatlaf.*;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.miginfocom.swing.MigLayout;
import util.Extension.SwingExtension;
import util.Extension.SwingExtension.JComponentConstraints;

@ExtensionMethod(SwingExtension.class)
@UtilityClass
@Log4j2
public class GUI {
  public static void run() {
    SwingUtilities.invokeLater(GUI::getGUI);
  }

  @SneakyThrows
  private static void getGUI() {
    FlatDarkLaf.setup();

    JFrame frame = new JFrame("Parse Pro");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(300, 250);
    frame.setLocationRelativeTo(null);

    JPanel panel = new JPanel(new MigLayout("wrap"));
    frame.add(panel);

    JTextField userInput = new JTextField(20);
    JButton btn = new JButton("Click here");
    JTextArea messageLabel = new JTextArea("");
    messageLabel.transformLabel();

    StringBuilder msg = new StringBuilder();
    btn.addActionListener(
        e -> {
          String input = userInput.getText();
          msg.append("You entered: ").append(input).append("\n");
          messageLabel.setText(msg.toString());
          userInput.setText("");
        });

    panel.addAll(
        new JComponentConstraints(userInput, "span, growx"),
        new JComponentConstraints(btn, "span, align center"),
        new JComponentConstraints(messageLabel, "span, growx"));

    frame.setVisible(true);
    frame.getRootPane().setDefaultButton(btn);

    SystemTrayIcon.run();
  }
}
