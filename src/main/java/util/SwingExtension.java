package util;

import javax.swing.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SwingExtension {

  public static void transformLabel(JTextArea messageLabel) {
    messageLabel.setLineWrap(true); // Enable line wrapping
    messageLabel.setWrapStyleWord(true); // Wrap at word boundaries
    messageLabel.setEditable(false); // Make the text area read-only
    messageLabel.setOpaque(false); // Make the text area transparent
    messageLabel.setFocusable(false); // Disable focus to prevent the text area from being editable
  }

  public static void addAll(JPanel panel, JComponentConstraints... components) {
    for (JComponentConstraints component : components) {
      panel.add(component.component(), component.constraints());
    }
  }

  public record JComponentConstraints(JComponent component, String constraints) {}
}
