package util.notepad;

import static util.extension.RobotExtension.screenHeight;
import static util.extension.RobotExtension.screenWidth;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import lombok.experimental.ExtensionMethod;
import lombok.extern.log4j.Log4j2;
import util.extension.RobotExtension;

@Log4j2
@ExtensionMethod({RobotExtension.class})
public class LinuxNotepad implements Notepad {
	public static final String home = System.getProperty("user.home") + File.separator + "ParseButPro";
	private Robot r;
	Process process;

	LinuxNotepad() {
		try {
			r = new Robot();
		} catch (AWTException e) {
			log.error("Failed to initialize Robot", e);
		}
	}

	public void openNotepad() throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder("notepadqq");
		process = processBuilder.start();
		r.delay(1000);
		r.mouseMove(screenWidth / 2, screenHeight / 2);
		r.leftClick();
	}

	public void writeText(String text) {
		r.type(text);
	}

	public void deleteText() {
		r.control(KeyEvent.VK_A);
		r.type(KeyEvent.VK_DELETE);
	}

	public void addNewLine() {
		r.enter();
	}

	public void saveFileAs(String name) {
		r.type(name);
		r.control(KeyEvent.VK_S);
		for (int i = 0; i < 7; i++) {
			r.type(KeyEvent.VK_TAB);
		}
		r.enter();
		r.type(home);
		r.enter();
	}

	public void openNewFile() {
		r.control(KeyEvent.VK_N);
	}

	public void closeFile() {
		r.control(KeyEvent.VK_W);
	}

	public void closeNotepad() throws InterruptedException {
		r.control(KeyEvent.VK_F4);
		log.info("Exited with code: {}", process.waitFor());
	}
}