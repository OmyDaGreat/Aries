package util.extension

import it.unimi.dsi.fastutil.ints.IntArrayList
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

  val special = hashMapOf(
    Pair("question", '?') to IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH),
    Pair("exclamation", '!') to IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1),
    Pair("colon", ':') to IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON),
    Pair("quote", '"') to IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE),
    Pair("tilde", '~') to IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE)
  )
  val directionActions = hashMapOf(
    "up" to TriConsumer { x: Int?, y: Int, robot: Robot -> robot.mouseMove(x!!, y - 50) },
    "down" to TriConsumer { x: Int?, y: Int, robot: Robot -> robot.mouseMove(x!!, y + 50) },
    "left" to TriConsumer { x: Int, y: Int?, robot: Robot -> robot.mouseMove(x - 50, y!!) },
    "right" to TriConsumer { x: Int, y: Int?, robot: Robot -> robot.mouseMove(x + 50, y!!) }
  )
}