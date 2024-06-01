package parse;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialOceanicTheme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

@UtilityClass
@Log4j2
public class GUI {
	public static void run() {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(new MaterialLookAndFeel());
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			MaterialLookAndFeel.changeTheme(new MaterialOceanicTheme());
			
			JFrame frame = new JFrame("Parse Pro");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setSize(300, 250);
			frame.setLocationRelativeTo(null);
			
			JPanel panel = new JPanel(new MigLayout("wrap"));
			frame.add(panel);
			
			JTextField userInput = new JTextField(20);
			JButton btn = new JButton("Click here");
			JTextArea messageLabel = new JTextArea("");
			messageLabel.setLineWrap(true); // Enable line wrapping
			messageLabel.setWrapStyleWord(true); // Wrap at word boundaries
			messageLabel.setEditable(false); // Make the text area read-only
			messageLabel.setOpaque(false); // Make the text area transparent
			messageLabel.setFocusable(false); // Disable focus to prevent the text area from being editable
			
			StringBuilder msg = new StringBuilder();
			btn.addActionListener(e -> {
				String input = userInput.getText();
				msg.append("You entered: ").append(input).append("\n");
				messageLabel.setText(msg.toString());
				userInput.setText("");
			});
			
			// Add components to the panel using MigLayout constraints
			panel.add(userInput, "span, growx"); // Span the text field across the panel and allow it to grow in width
			panel.add(btn, "span, align center"); // Span the button across the panel and center it
			panel.add(messageLabel, "span, growx"); // Span the message label across the panel and allow it to grow in width
			
			// Make the frame visible
			frame.setVisible(true);
			
			// Make button work on enter
			frame.getRootPane().setDefaultButton(btn);
			
			SystemTrayIcon.run();
		});
	}
}
