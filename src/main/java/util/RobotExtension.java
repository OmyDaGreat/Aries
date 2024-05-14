package util;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class RobotExtension {
    public static final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width, screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    private static final Map<Character, List<Integer>> special = new HashMap<>();

    static {
        special.put('?', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH));
        special.put('!', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1));
    }

    public static Robot sendKeys(Robot robot, String keys) {
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
}
