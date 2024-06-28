package util.Notepad;

import lombok.experimental.ExtensionMethod;
import lombok.extern.log4j.Log4j2;
import util.Extension.RobotExtension;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

@Log4j2
@ExtensionMethod({RobotExtension.class})
public class MacNotepad implements Notepad {
    public static final String home = System.getProperty("user.home") + File.separator + "ParseButPro";
    private Robot r;

    MacNotepad() {
        try {
            r = new Robot();
        } catch (AWTException e) {
            log.error("Failed to initialize Robot", e);
        }
    }

    public void openNotepad() {
        // TODO: Implement this method
    }

    public void writeText(String text) {
        // TODO: Implement this method
    }

    public void deleteText() {
        // TODO: Implement this method
    }

    public void addNewLine() {
        // TODO: Implement this method
    }

    public void saveFileAs(String name) {
        // TODO: Implement this method
    }

    public void openNewFile() {
        // TODO: Implement this method
    }

    public void closeFile() {
        // TODO: Implement this method
    }

    public void closeNotepad() {
        // TODO: Implement this method
    }
}