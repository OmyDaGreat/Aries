package parse;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RobotTesting {
	public static void main(String[] args) {
		try {
			Robot robot = new Robot();
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			robot.keyPress(KeyEvent.VK_0);
			robot.keyRelease(KeyEvent.VK_0);
			log.info("Like getting your buttons pushed?");
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
