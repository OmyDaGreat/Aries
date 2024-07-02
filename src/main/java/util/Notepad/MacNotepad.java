package util.Notepad;

import lombok.experimental.ExtensionMethod;
import lombok.extern.log4j.Log4j2;
import util.Extension.RobotExtension;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

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

    private void runAppleScript(String scriptPath) {
        String[] args = { "osascript", scriptPath };
        try {
            Process process = Runtime.getRuntime().exec(args);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("Failed to run AppleScript", e);
        }
    }

    @Override
    public void openNotepad() {
        runAppleScript("/Applications/openNotepad.scpt");
        r.delay(10000); // Wait for TextEdit to open
    }

    @Override
    public void writeText(String text) {
        for (char c : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                log.error("Key code not found for character '" + c + "'");
                continue;
            }
            r.keyPress(keyCode);
            r.keyRelease(keyCode);
            r.delay(50);
        }
    }

    @Override
    public void deleteText() {
        r.keyPress(KeyEvent.VK_META); // Command key
        r.keyPress(KeyEvent.VK_A); // Select All
        r.keyRelease(KeyEvent.VK_A);
        r.keyRelease(KeyEvent.VK_META);
        r.delay(200);

        r.keyPress(KeyEvent.VK_BACK_SPACE); // Delete selected text
        r.keyRelease(KeyEvent.VK_BACK_SPACE);
        r.delay(200);
    }

    @Override
    public void addNewLine() {
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyRelease(KeyEvent.VK_ENTER);
    }

    @Override
    public void saveFileAs(String name) {
        r.keyPress(KeyEvent.VK_META); // Command key
        r.keyPress(KeyEvent.VK_SHIFT);
        r.keyPress(KeyEvent.VK_S); // Save As
        r.keyRelease(KeyEvent.VK_S);
        r.keyRelease(KeyEvent.VK_SHIFT);
        r.keyRelease(KeyEvent.VK_META);
        r.delay(1000);

        writeText(home + File.separator + name); // Write file path
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyRelease(KeyEvent.VK_ENTER);
    }

    @Override
    public void openNewFile() {
        r.keyPress(KeyEvent.VK_META); // Command key
        r.keyPress(KeyEvent.VK_N); // New file
        r.keyRelease(KeyEvent.VK_N);
        r.keyRelease(KeyEvent.VK_META);
        r.delay(500);
    }

    @Override
    public void closeFile() {
        r.keyPress(KeyEvent.VK_META); // Command key
        r.keyPress(KeyEvent.VK_W); // Close window
        r.keyRelease(KeyEvent.VK_W);
        r.keyRelease(KeyEvent.VK_META);
        r.delay(500);
    }

    @Override
    public void closeNotepad() {
        r.keyPress(KeyEvent.VK_META); // Command key
        r.keyPress(KeyEvent.VK_Q); // Quit application
        r.keyRelease(KeyEvent.VK_Q);
        r.keyRelease(KeyEvent.VK_META);
    }
}
