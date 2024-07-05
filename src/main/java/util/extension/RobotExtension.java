package util.extension;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import kotlin.Pair;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.function.TriConsumer;

@Log4j2
@UtilityClass
@ExtensionMethod(MapExtension.class)
public class RobotExtension {

	/**
	 * Width of the screen in pixels.
	 */
	public static final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

	/**
	 * Height of the screen in pixels.
	 */
	public static final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

	/**
	 * Maps special characters to sequences of key presses and releases.
	 */
	private static final Map<Pair<String, Character>, IntArrayList> special = new HashMap<>();

	/**
	 * Maps direction names to lambda expressions defining mouse movement actions.
	 */
	private static final Map<String, TriConsumer<Integer, Integer, Robot>> directionActions = new HashMap<>();
	
	static {
		initializeDirections();
		initializeSpecialChars();
	}

	private static void initializeDirections() {
		directionActions.put("up", (x, y, robot) -> robot.mouseMove(x, y - 50));
		directionActions.put("down", (x, y, robot) -> robot.mouseMove(x, y + 50));
		directionActions.put("left", (x, y, robot) -> robot.mouseMove(x - 50, y));
		directionActions.put("right", (x, y, robot) -> robot.mouseMove(x + 50, y));
	}

	private static void initializeSpecialChars() {
		special.put(new Pair<>("question", '?'), IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH));
		special.put(new Pair<>("exclamation", '!'), IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1));
		special.put(new Pair<>("colon", ':'), IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON));
		special.put(new Pair<>("quote", '"'), IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE));
		special.put(new Pair<>("tilde", '~'), IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE));
	}

	/**
	 * Simulates typing a sequence of characters using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @param keys  The sequence of characters to type.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot type(Robot robot, String keys) {
		for (char c : keys.toCharArray()) {
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
			if (KeyEvent.CHAR_UNDEFINED == keyCode) {
				throw new IllegalArgumentException(
						"Key code not found for character '" + c + "'");
      		} else if (special.containsKeySecond(c)) {
				for (int i = 0; i < special.getSecond(c).size(); i++) {
					robot.keyPress(special.getSecond(c).getInt(i));
				}
				for (int i = special.getSecond(c).size() - 1; i >= 0; i--) {
					robot.keyRelease(special.getSecond(c).getInt(i));
				}
			} else if (Character.isUpperCase(c)) {
				shift(robot, keyCode);
			} else {
				type(robot, keyCode);
			}
		}
		return robot;
	}

	/**
	 * Simulates pressing and releasing a single key using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @param i     The virtual key code representing the key to press and release.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot type(Robot robot, int i) {
		robot.keyPress(i);
		robot.keyRelease(i);
		return robot;
	}

	/**
	 * Simulates a left mouse click using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot leftClick(Robot robot) {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		return robot;
	}

	/**
	 * Simulates a right mouse click using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot rightClick(Robot robot) {
		robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
		return robot;
	}

	/**
	 * Simulates pressing the ENTER key using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot enter(Robot robot) {
		type(robot, KeyEvent.VK_ENTER);
		return robot;
	}

	/**
	 * Simulates pressing the TAB key using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot tab(Robot robot) {
		type(robot, KeyEvent.VK_TAB);
		return robot;
	}

	/**
	 * Simulates pressing the CONTROL key followed by another key using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @param i     The virtual key code representing the key to press after CONTROL.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot control(Robot robot, int i) {
		robot.keyPress(KeyEvent.VK_CONTROL);
		type(robot, i);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		return robot;
	}

	/**
	 * Simulates pressing the SHIFT key followed by another key using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @param i     The virtual key code representing the key to press after SHIFT.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot shift(Robot robot, int i) {
		robot.keyPress(KeyEvent.VK_SHIFT);
		type(robot, i);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		return robot;
	}

	/**
	 * Simulates saving something using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot save(Robot robot) {
		control(robot, KeyEvent.VK_S);
		return robot;
	}

	/**
	 * Moves the mouse cursor according to the specified directions using a {@link Robot}.
	 *
	 * @param robot The {@link Robot} instance to use for simulation.
	 * @param direction A space-separated string specifying the directions ("up", "down", "left", "right").
	 * @return The modified {@link Robot} instance.
	 */
	public static Robot mouseMoveString(Robot robot, String direction) {
		String[] split = direction.split(" ");
		for (String dir : split) {
			int x = MouseInfo.getPointerInfo().getLocation().x;
			int y = MouseInfo.getPointerInfo().getLocation().y;
			TriConsumer<Integer, Integer, Robot> action = directionActions.get(dir);
			if (action != null) {
				action.accept(x, y, robot);
			} else {
              log.debug("Invalid direction: \"{}\"", dir);
			}
		}
		return robot;
	}
}
