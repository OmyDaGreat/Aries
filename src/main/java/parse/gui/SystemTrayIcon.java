package parse.gui;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Log4j2
@UtilityClass
public class SystemTrayIcon {
    public static void run() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException |
                 ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    static void createAndShowGUI() throws IOException, URISyntaxException {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            log.error("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final URL trayIconUrl = new URI("https://docs.oracle.com/javase%2Ftutorial%2F/uiswing/examples/misc/TrayIconDemoProject/src/misc/images/bulb.gif").toURL();
        Image image = ImageIO.read(trayIconUrl); // TODO: New icon ^
        final TrayIcon trayIcon = new TrayIcon(image, "tray icon");
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warn");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem debugItem = new MenuItem("Debug");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(debugItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            log.error("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "This dialog box is run from System Tray"));

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "This dialog box is run from the About menu item"));

        cb1.addItemListener(e -> {
            int cb1Id = e.getStateChange();
	        trayIcon.setImageAutoSize(cb1Id == ItemEvent.SELECTED);
        });

        cb2.addItemListener(e -> {
            int cb2Id = e.getStateChange();
            trayIcon.setToolTip(cb2Id == ItemEvent.SELECTED ? "Sun TrayIcon" : null);
        });

        ActionListener listener = e -> {
            MenuItem item = (MenuItem)e.getSource();
            log.log(Level.getLevel(item.getLabel()), item.getLabel());
            switch (item.getLabel()) {
                case "Error":
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", java.awt.TrayIcon.MessageType.ERROR);
                    break;
                case "Warning":
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", java.awt.TrayIcon.MessageType.WARNING);
                    break;
                case "Info":
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", java.awt.TrayIcon.MessageType.INFO);
                    break;
                default:
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", java.awt.TrayIcon.MessageType.NONE);
                    break;
            }
        };

        errorItem.addActionListener(listener);
        warningItem.addActionListener(listener);
        infoItem.addActionListener(listener);
        debugItem.addActionListener(listener);

        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
    }

    //Obtain the image URL
    public static Image createImage(String path, String description) {
        URL imageURL = SystemTrayIcon.class.getResource(path);

        if (imageURL == null) {
	        log.error("Resource not found: {}", path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
