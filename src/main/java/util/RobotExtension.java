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
    }

    public static Robot type(Robot robot, String keys) {
        for (char c : keys.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException(
                    "Key code not found for character '" + c + "'");
            } else if(special.containsKey(c)) {
                robot.keyPress(special.get(c).get(0));
                robot.keyPress(special.get(c).get(1));
                robot.keyRelease(special.get(c).get(1));
                robot.keyRelease(special.get(c).get(0));
            } else {
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
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
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        return robot;
    }
}
