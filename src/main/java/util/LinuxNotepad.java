package util;

import lombok.experimental.ExtensionMethod;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.IOException;

import static util.RobotExtension.screenHeight;
import static util.RobotExtension.screenWidth;

@Log4j2
@ExtensionMethod({RobotExtension.class})
public class LinuxNotepad {
	public static void open() throws AWTException, IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder("notepadqq");
		Process process = processBuilder.start();
		Robot r = new Robot();
		r.delay(1000);
		r.mouseMove(screenWidth / 2, screenHeight / 2);
		r.leftClick();
		r.type("Help, i'm stuck in the matrix~");
		r.enter();
		r.type("Wait, is this only a temporary simulation?");
		r.save();
		// TODO: Actually save the file
		log.info("Exited with code: {}", process.waitFor());
	}
}
