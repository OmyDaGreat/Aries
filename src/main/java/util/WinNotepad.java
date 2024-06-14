package util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
@ExtensionMethod({RobotExtension.class})
public class WinNotepad {
	public static final String home = System.getProperty("user.home") + File.separator + "ParseButPro";
	
	public static void open() throws AWTException, IOException, InterruptedException {
		Files.createDirectories(Paths.get(home));
		ProcessBuilder processBuilder = new ProcessBuilder("C:\\Program Files\\Notepad++\\notepad++.exe");
		Process process = processBuilder.start();
		Robot r = new Robot();
		r.delay(1000);
		r.control(KeyEvent.VK_N);
		r.type("Help, I'm stuck in the matrix!");
		r.enter();
		r.type("Wait, is this only a temporary simulation?");
		r.control(KeyEvent.VK_S);
		r.delay(1000);
		r.type("test.txt");
		for(int i = 0; i < 7; i++) {
			r.tab();
		}
		r.enter();
		r.type(home);
		r.enter();
		for(int i = 0; i < 10; i++) {
			r.tab();
		}
		r.enter();
		log.info("Exited with code: {}", process.waitFor());
	}
}
