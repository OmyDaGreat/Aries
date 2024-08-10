package util.extension

import util.extension.RobotUtils.special
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.KeyEvent

/**
 * Moves the mouse cursor based on the specified directions.
 * @param direction A string containing one or more directions (e.g., "up left") to move the mouse cursor.
 * Each direction should be separated by a space.
 */
fun Robot.mouseMoveString(direction: String) {
  direction.split(" ").forEach {dir ->
    RobotUtils.directionActions[dir]?.accept(
      MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y, this
    )
  }
}

/**
 * Simulates typing a sequence of characters.
 * @param keys A string where each character represents a key to be typed by the robot.
 */
fun Robot.type(keys: String) {
  keys.forEach {c ->
    when {
      c == KeyEvent.CHAR_UNDEFINED -> {
        throw IllegalArgumentException("Key code not found for character '$c'")
      }

      special.containsKeySecond(c) -> {
        special.getFromSecond(c).forEach {key ->
          type(key)
        }
      }

      c.isUpperCase() -> {
        shift(KeyEvent.getExtendedKeyCodeForChar(c.code))
      }

      else -> {
        type(KeyEvent.getExtendedKeyCodeForChar(c.code))
      }
    }
  }
}

/**
 * Presses and releases a specified key.
 * @param keyCode The integer code of the key to be pressed and released.
 */
fun Robot.type(keyCode: Int) {
  keyPress(keyCode)
  keyRelease(keyCode)
}

/**
 * Simulates a left mouse click.
 */
fun Robot.leftClick() = click(KeyEvent.BUTTON1_DOWN_MASK)

/**
 * Simulates a right mouse click.
 */
fun Robot.rightClick() = click(KeyEvent.BUTTON2_DOWN_MASK)

/**
 * Simulates pressing the ENTER key.
 */
fun Robot.enter() = type(KeyEvent.VK_ENTER)

/**
 * Simulates pressing the TAB key.
 */
fun Robot.tab() = type(KeyEvent.VK_TAB)

/**
 * Simulates pressing the TAB key.
 */
fun Robot.esc() = type(KeyEvent.VK_ESCAPE)

/**
 * Simulates pressing CONTROL + another key.
 * @param keyCode The integer code of the key to be pressed in combination with the CONTROL key.
 */
fun Robot.control(keyCode: Int) {
  keyPress(KeyEvent.VK_CONTROL)
  type(keyCode)
  keyRelease(KeyEvent.VK_CONTROL)
}

/**
 * Simulates pressing CONTROL + doing something else.
 * @param action The function to be done in combination with the CONTROL key.
 */
fun Robot.control(action: (Robot) -> Unit) {
  keyPress(KeyEvent.VK_CONTROL)
  action(this)
  keyRelease(KeyEvent.VK_CONTROL)
}

fun Robot.f(i: Int?) {
  if (i == null) return
  when (i) {
    1 -> type(KeyEvent.VK_F1)
    2 -> type(KeyEvent.VK_F2)
    3 -> type(KeyEvent.VK_F3)
    4 -> type(KeyEvent.VK_F4)
    5 -> type(KeyEvent.VK_F5)
    6 -> type(KeyEvent.VK_F6)
    7 -> type(KeyEvent.VK_F7)
    8 -> type(KeyEvent.VK_F8)
    9 -> type(KeyEvent.VK_F9)
    10 -> type(KeyEvent.VK_F10)
    11 -> type(KeyEvent.VK_F11)
    12 -> type(KeyEvent.VK_F12)
  }
}

/**
 * Simulates pressing COMMAND + another key.
 * @param keyCode The integer code of the key to be pressed in combination with the COMMAND key.
 */
fun Robot.command(keyCode: Int) {
  keyPress(KeyEvent.VK_META)
  type(keyCode)
  keyRelease(KeyEvent.VK_META)
}

/**
 * Simulates pressing CMD + doing something else.
 * @param action The function to be done in combination with the CMD key.
 */
fun Robot.command(action: (Robot) -> Unit) {
  keyPress(KeyEvent.VK_META)
  action(this)
  keyRelease(KeyEvent.VK_META)
}

/**
 * Simulates pressing SHIFT + another key.
 * @param keyCode The integer code of the key to be pressed in combination with the SHIFT key.
 */
fun Robot.shift(keyCode: Int) {
  keyPress(KeyEvent.VK_SHIFT)
  type(keyCode)
  keyRelease(KeyEvent.VK_SHIFT)
}

/**
 * Simulates pressing SHIFT + doing something else.
 * @param action The function to be done in combination with the SHIFT key.
 */
fun Robot.shift(action: (Robot) -> Unit) {
  keyPress(KeyEvent.VK_SHIFT)
  action(this)
  keyRelease(KeyEvent.VK_SHIFT)
}

/**
 * Simulates pressing WINDOWS + doing something else.
 * @param action The function to be done in combination with the WINDOWS key.
 */
fun Robot.windows(action: (Robot) -> Unit) {
  keyPress(KeyEvent.VK_WINDOWS)
  action(this)
  keyRelease(KeyEvent.VK_WINDOWS)
}

/**
 * Simulates pressing ALT + doing something else.
 * @param action The function to be done in combination with the ALT key.
 */
fun Robot.alt(action: (Robot) -> Unit) {
  keyPress(KeyEvent.VK_ALT)
  action(this)
  keyRelease(KeyEvent.VK_ALT)
}

/**
 * Scrolls the mouse wheel up or down based on the specified direction.
 * @param direction A string specifying the scroll direction ("up" or "down").
 */
fun Robot.scroll(direction: String) {
  direction.split(" ").forEach { dir ->
    when (dir.lowercase()) {
      "up" -> mouseWheel(-1)
      "down" -> mouseWheel(1)
    }
  }
}

/**
 * Moves the mouse cursor based on a string of directions.
 * @param directions A string containing directions (e.g., "up down left right") to move the mouse cursor.
 * Each direction should be separated by a space.
 */
fun Robot.arrow(directions: String) {
  directions.split(" ").forEach { direction ->
    when (direction.lowercase()) {
      "up" -> type(KeyEvent.VK_UP)
      "down" -> type(KeyEvent.VK_DOWN)
      "left" -> type(KeyEvent.VK_LEFT)
      "right" -> type(KeyEvent.VK_RIGHT)
      "write" -> type(KeyEvent.VK_RIGHT)
    }
  }
}

/**
 * Simulates saving something by pressing CONTROL + S.
 */
fun Robot.save() = control(KeyEvent.VK_S)

/**
 * Helper function to simulate mouse clicks.
 * @param mask The mask for the mouse button to be clicked.
 */
private fun Robot.click(mask: Int) {
  mousePress(mask)
  mouseRelease(mask)
}