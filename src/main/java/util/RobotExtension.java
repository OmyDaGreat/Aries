package util;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RobotExtension {
    public static final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width, screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    private static final Map<Character, List<Integer>> special = new HashMap<>();

    static {
        special.put('?', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH));
        special.put('!', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1));
        special.put(':', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON));
        special.put('"', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE));
        special.put('~', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE));
    }

    public static Robot type(Robot robot, String keys) {
        for (char c : keys.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException(
                    "Key code not found for character '" + c + "'");
            } else if(special.containsKey(c)) {
                for (int i = 0; i < special.get(c).size(); i++) {
                    robot.keyPress(special.get(c).get(i));
                }
                for (int i = special.get(c).size() - 1; i >= 0; i--) {
                    robot.keyRelease(special.get(c).get(i));
                }
            } else if(Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                type(robot, keyCode);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                type(robot, keyCode);
            }
        }
        return robot;
    }
    public static Robot type(Robot robot, int i) {
        robot.keyPress(i);
        robot.keyRelease(i);
        return robot;
    }
    public static Robot leftClick(Robot robot) {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.keyRelease(InputEvent.BUTTON1_DOWN_MASK);
        return robot;
    }
    public static Robot rightClick(Robot robot) {
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        robot.keyRelease(InputEvent.BUTTON2_DOWN_MASK);
        return robot;
    }
    public static Robot enter(Robot robot) {
        type(robot, KeyEvent.VK_ENTER);
        return robot;
    }
    public static Robot tab(Robot robot) {
        type(robot, KeyEvent.VK_TAB);
        return robot;
    }
    public static Robot control(Robot robot, int i) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        type(robot, i);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        return robot;
    }
    public static Robot shift(Robot robot, int i) {
        robot.keyPress(KeyEvent.VK_SHIFT);
        type(robot, i);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        return robot;
    }
}
