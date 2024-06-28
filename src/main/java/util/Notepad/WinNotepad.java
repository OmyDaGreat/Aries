package util.Notepad;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.experimental.ExtensionMethod;
import lombok.extern.log4j.Log4j2;
import util.Extension.RobotExtension;

@Log4j2
@ExtensionMethod({RobotExtension.class})
public class WinNotepad implements Notepad {
	public static final String home = System.getProperty("user.home") + File.separator + "ParseButPro";
	private Robot r;
	private Process process;

	WinNotepad() {
		try {
			r = new Robot();
		} catch (AWTException e) {
			log.error("Failed to initialize Robot", e);
		}
	}

	public void openNotepad() throws IOException {
		Files.createDirectories(Paths.get(home));
		ProcessBuilder processBuilder = new ProcessBuilder("C:\\Program Files\\Notepad++\\notepad++.exe");
		process = processBuilder.start();
		r.delay(1000);
	}

	public void writeText(String text) {
		r.type(text);
	}

	public void deleteText() {
		r.control(KeyEvent.VK_A);
		r.type(KeyEvent.VK_BACK_SPACE);
	}

	public void addNewLine() {
		r.enter();
	}

	public void saveFileAs(String name) {
		r.control(KeyEvent.VK_S);
		r.delay(1000);
		r.type(name);
		for (int i = 0; i < 7; i++) {
			r.type(KeyEvent.VK_TAB);
		}
		r.enter();
		r.type(home);
		r.enter();
		r.delay(500);
		r.enter();
		r.delay(500);
		r.enter();
		r.delay(500);
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
		log.info("Exited Notepad++ with code: {}", process.waitFor());
	}
}