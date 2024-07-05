package util.extension;

import javax.swing.*;
import lombok.experimental.UtilityClass;

/**
 * Utility class providing enhancements and utility methods for Swing components.
 */
@UtilityClass
public class SwingExtension {

  /**
   * Transforms a {@link JTextArea} to display text in a more user-friendly way.
   * This includes enabling line wrapping, making the text area read-only, and setting its background to be transparent.
   *
   * @param messageLabel The {@link JTextArea} to be transformed.
   */
  public static void transformLabel(JTextArea messageLabel) {
    messageLabel.setLineWrap(true); // Enable line wrapping
    messageLabel.setWrapStyleWord(true); // Wrap at word boundaries
    messageLabel.setEditable(false); // Make the text area read-only
    messageLabel.setOpaque(false); // Make the text area transparent
    messageLabel.setFocusable(false); // Disable focus to prevent the text area from being editable
  }

  /**
   * Adds multiple components to a {@link JPanel} with specified constraints for each.
   * This method simplifies the process of adding components to a panel by allowing the specification of constraints for each component.
   *
   * @param panel The {@link JPanel} to which the components will be added.
   * @param components An array of {@link JComponentConstraints}, pairing each component with its layout constraints.
   */
  public static void addAll(JPanel panel, JComponentConstraints... components) {
    for (JComponentConstraints component : components) {
      panel.add(component.component(), component.constraints());
    }
  }

  /**
   * Record to pair a {@link JComponent} with a {@link String} representing layout constraints.
   * This facilitates specifying layout constraints when adding components to a container.
   */
  public record JComponentConstraints(JComponent component, String constraints) {}
}