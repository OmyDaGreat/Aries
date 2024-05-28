package parse;

import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

@Log4j2
public class GUI {
	public static void main(String[] args) throws IOException {
		//Check the SystemTray is supported
		if (!SystemTray.isSupported()) {
			log.info("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		Image image = null;
		final URL trayIconUrl = new URL("https://docs.oracle.com/javase%2Ftutorial%2F/uiswing/examples/misc/TrayIconDemoProject/src/misc/images/bulb.gif");
		image = ImageIO.read(trayIconUrl);
		final TrayIcon trayIcon =
				new TrayIcon(image, "tray icon");
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		MenuItem aboutItem = new MenuItem("About");
		CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
		CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
		Menu displayMenu = new Menu("Display");
		MenuItem errorItem = new MenuItem("Error");
		MenuItem warningItem = new MenuItem("Warning");
		MenuItem infoItem = new MenuItem("Info");
		MenuItem noneItem = new MenuItem("None");
		MenuItem exitItem = new MenuItem("Exit");

		//Add components to pop-up menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(cb1);
		popup.add(cb2);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(errorItem);
		displayMenu.add(warningItem);
		displayMenu.add(infoItem);
		displayMenu.add(noneItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			log.info("TrayIcon could not be added.");
		}

//		SwingUtilities.invokeLater(() -> {
//			try {
//				UIManager.setLookAndFeel(new MaterialLookAndFeel());
//			} catch (UnsupportedLookAndFeelException e) {
//				throw new util.UnsupportedLookAndFeelException(e);
//			}
//			MaterialLookAndFeel.changeTheme(new MaterialOceanicTheme());
//			// Create the main frame
//			JFrame frame = new JFrame("Parse Pro");
//			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//			frame.setSize(300, 250);
//			frame.setLocationRelativeTo(null); // Center the frame on the screen
//
//			// Create and add the main panel
//			JPanel panel = new JPanel(new MigLayout("wrap")); // Use MigLayout with wrap 2
//			frame.add(panel);
//
//			// Create components: text field, button, and message label
//			JTextField userInput = new JTextField(20);
//			JButton btn = new JButton("Click here");
//			JTextArea messageLabel = new JTextArea("");
//			messageLabel.setLineWrap(true); // Enable line wrapping
//			messageLabel.setWrapStyleWord(true); // Wrap at word boundaries
//			messageLabel.setEditable(false); // Make the text area read-only
//			messageLabel.setOpaque(false); // Make the text area transparent
//			messageLabel.setFocusable(false); // Disable focus to prevent the text area from being editable
//
//			// Add action listener to the button
//			AtomicReference<String> msg = new AtomicReference<>("");
//			btn.addActionListener(e -> {
//				String input = userInput.getText();
//				msg.set(msg.get() + "You entered: " + input + "\n");
//				messageLabel.setText(msg.get());
//				userInput.setText("");
//			});
//
//			// Add components to the panel using MigLayout constraints
//			panel.add(userInput, "span, growx"); // Span the text field across the panel and allow it to grow in width
//			panel.add(btn, "span, align center"); // Span the button across the panel and center it
//			panel.add(messageLabel, "span, growx"); // Span the message label across the panel and allow it to grow in width
//
//			// Make the frame visible
//			frame.setVisible(true);
//
//			// Make button work on enter
//			frame.getRootPane().setDefaultButton(btn);
//
//			// Log
//			log.info("Check it out.");
//		});
	}
}
