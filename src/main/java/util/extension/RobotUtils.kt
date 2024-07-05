package util.extension

import it.unimi.dsi.fastutil.ints.IntArrayList
import org.apache.commons.lang3.function.TriConsumer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Robot
import java.awt.Toolkit
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

object RobotUtils {
  val log: Logger = LogManager.getLogger()
  /**
   * Width of the screen in pixels.
   */
  @JvmField
  val screenWidth: Int = Toolkit.getDefaultToolkit().screenSize.width

  /**
   * Height of the screen in pixels.
   */
  @JvmField
  val screenHeight: Int = Toolkit.getDefaultToolkit().screenSize.height

  /**
   * Maps special characters to sequences of key presses and releases.
   */
  val special: MutableMap<Pair<String, Char>, IntArrayList> = HashMap()

  /**
   * Maps direction names to lambda expressions defining mouse movement actions.
   */
  val directionActions: MutableMap<String, TriConsumer<Int, Int, Robot>> = HashMap()

  init {
    initializeDirections()
    initializeSpecialChars()
  }

  private fun initializeDirections() {
    directionActions["up"] = TriConsumer { x: Int?, y: Int, robot: Robot ->
      robot.mouseMove(
        x!!, y - 50
      )
    }
    directionActions["down"] = TriConsumer { x: Int?, y: Int, robot: Robot ->
      robot.mouseMove(
        x!!, y + 50
      )
    }
    directionActions["left"] = TriConsumer { x: Int, y: Int?, robot: Robot -> robot.mouseMove(x - 50, y!!) }
    directionActions["right"] = TriConsumer { x: Int, y: Int?, robot: Robot -> robot.mouseMove(x + 50, y!!) }
  }

  private fun initializeSpecialChars() {
    special[Pair("question", '?')] = IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH)
    special[Pair("exclamation", '!')] =
      IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1)
    special[Pair("colon", ':')] =
      IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON)
    special[Pair("quote", '"')] = IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE)
    special[Pair("tilde", '~')] = IntArrayList.of(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE)
  }
}
