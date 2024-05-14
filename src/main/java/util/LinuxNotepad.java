package util;

import java.io.IOException;

import static util.RobotExtension.screenHeight;
import static util.RobotExtension.screenWidth;

import lombok.experimental.ExtensionMethod;
import lombok.extern.log4j.Log4j2;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@Log4j2
@ExtensionMethod({RobotExtension.class})
public class LinuxNotepad {
    public static void main(String[] args) throws AWTException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("notepadqq");
            Process process = processBuilder.start();
            Robot r = new Robot();
            r.delay(1000);
            r.mouseMove(screenWidth/2, screenHeight/2);
            r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            r.sendKeys("help i'm stuck in the matrix!");
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);
            r.sendKeys("wait, is this just a temporary simulation?");
            log.info("Exited with code: " + process.waitFor());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
