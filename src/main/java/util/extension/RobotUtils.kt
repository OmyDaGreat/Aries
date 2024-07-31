package util.extension

import org.apache.commons.lang3.function.TriConsumer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Robot
import java.awt.Toolkit
import java.awt.event.KeyEvent

object RobotUtils {
  val log: Logger = LogManager.getLogger()
  val screenWidth = Toolkit.getDefaultToolkit().screenSize.width
  val screenHeight = Toolkit.getDefaultToolkit().screenSize.height

  val special = linkedMapOf(
    Pair("question", '?') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH),
    Pair("colon", ':') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON),
    Pair("quote", '"') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE),
    Pair("tilde", '~') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE),
    Pair("apostrophe", '\'') to listOf(KeyEvent.VK_QUOTE),
    Pair("tab", '\t') to listOf(KeyEvent.VK_TAB),
    Pair("exclamation", '!') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_1),
    Pair("at", '@') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_2),
    Pair("hashtag", '#') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_3),
    Pair("octothorpe", '#') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_3),
    Pair("dollar", '$') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_4),
    Pair("percent", '%') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_5),
    Pair("up", '^') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_6),
    Pair("and", '&') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_7),
    Pair("asterisk", '*') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_8),
    Pair("open", '(') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_9),
    Pair("close", ')') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_0),
    Pair("left", '<') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA),
    Pair("right", '>') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD),
    Pair("semicolon", ';') to listOf(KeyEvent.VK_SEMICOLON),
    Pair("comma", ',') to listOf(KeyEvent.VK_COMMA),
    Pair("period", '.') to listOf(KeyEvent.VK_PERIOD),
    Pair("tilde", '.') to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE),
    Pair("back quote", '.') to listOf(KeyEvent.VK_BACK_QUOTE),
    Pair("zero", '0') to listOf(KeyEvent.VK_0),
    Pair("one", '1') to listOf(KeyEvent.VK_1),
    Pair("two", '2') to listOf(KeyEvent.VK_2),
    Pair("to", '2') to listOf(KeyEvent.VK_2),
    Pair("three", '3') to listOf(KeyEvent.VK_3),
    Pair("four", '4') to listOf(KeyEvent.VK_4),
    Pair("for", '4') to listOf(KeyEvent.VK_4),
    Pair("five", '5') to listOf(KeyEvent.VK_5),
    Pair("six", '6') to listOf(KeyEvent.VK_6),
    Pair("seven", '7') to listOf(KeyEvent.VK_7),
    Pair("eight", '8') to listOf(KeyEvent.VK_8),
    Pair("nine", '9') to listOf(KeyEvent.VK_9),
    Pair("space", ' ') to listOf(KeyEvent.VK_SPACE),
    Pair("slash", '/') to listOf(KeyEvent.VK_SLASH),
    Pair("backslash", '\\') to listOf(KeyEvent.VK_BACK_SLASH),
    Pair("single quote", '\'') to listOf(KeyEvent.VK_QUOTE),
    Pair("back quote", '`') to listOf(KeyEvent.VK_BACK_QUOTE),
  )
  val directionActions = hashMapOf(
    "up" to TriConsumer {x: Int?, y: Int, robot: Robot -> robot.mouseMove(x!!, y - 50)},
    "down" to TriConsumer {x: Int?, y: Int, robot: Robot -> robot.mouseMove(x!!, y + 50)},
    "left" to TriConsumer {x: Int, y: Int?, robot: Robot -> robot.mouseMove(x - 50, y!!)},
    "right" to TriConsumer {x: Int, y: Int?, robot: Robot -> robot.mouseMove(x + 50, y!!)},
  )
}