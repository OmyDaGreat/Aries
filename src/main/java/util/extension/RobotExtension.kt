package util.extension

import util.extension.RobotUtils.directionActions
import util.extension.RobotUtils.special
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

/**
 * Moves the mouse cursor according to the specified directions using a [Robot].
 *
 * @param robot The [Robot] instance to use for simulation.
 * @param direction A space-separated string specifying the directions ("up", "down", "left", "right").
 * @return The modified [Robot] instance.
 */
fun Robot.mouseMoveString(direction: String): Robot {
  val split = direction.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  for (dir in split) {
    val x = MouseInfo.getPointerInfo().location.x
    val y = MouseInfo.getPointerInfo().location.y
    val action = directionActions[dir]
    if (action != null) {
      action.accept(x, y, this)
    } else {
      RobotUtils.log.debug("Invalid direction: \"{}\"", dir)
    }
  }
  return this
}

/**
 * Simulates typing a sequence of characters using a [Robot].
 *
 * @param keys  The sequence of characters to type.
 * @return The modified [Robot] instance.
 */
fun Robot.type(keys: String): Robot {
  for (c in keys.toCharArray()) {
    val keyCode = KeyEvent.getExtendedKeyCodeForChar(c.code)
    require(KeyEvent.CHAR_UNDEFINED.code != keyCode) { "Key code not found for character '$c'" }
    if (special.containsKeySecond(c)) {
      for (i in special.getSecond(c).indices) {
        keyPress(special.getSecond(c).getInt(i))
      }
      for (i in special.getSecond(c).indices.reversed()) {
        keyRelease(special.getSecond(c).getInt(i))
      }
    } else if (Character.isUpperCase(c)) {
      this.shift(keyCode)
    } else {
      this.type(keyCode)
    }
  }
  return this
}

/**
 * Simulates pressing and releasing a single key using a [Robot].
 *
 * @param i     The virtual key code representing the key to press and release.
 * @return The modified [Robot] instance.
 */
fun Robot.type(i: Int): Robot {
  keyPress(i)
  keyRelease(i)
  return this
}

/**
 * Simulates a left mouse click using a [Robot].
 *
 * @return The modified [Robot] instance.
 */
fun Robot.leftClick(): Robot {
  mousePress(InputEvent.BUTTON1_DOWN_MASK)
  mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
  return this
}

/**
 * Simulates a right mouse click using a [Robot].
 *
 * @return The modified [Robot] instance.
 */
fun Robot.rightClick(): Robot {
  mousePress(InputEvent.BUTTON2_DOWN_MASK)
  mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
  return this
}

/**
 * Simulates pressing the ENTER key using a [Robot].
 *
 * @return The modified [Robot] instance.
 */
fun Robot.enter(): Robot {
  type(KeyEvent.VK_ENTER)
  return this
}

/**
 * Simulates pressing the TAB key using a [Robot].
 *
 * @return The modified [Robot] instance.
 */
fun Robot.tab(): Robot {
  type(KeyEvent.VK_TAB)
  return robot
}

/**
 * Simulates pressing the CONTROL key followed by another key using a [Robot].
 *
 * @param i     The virtual key code representing the key to press after CONTROL.
 * @return The modified [Robot] instance.
 */
fun Robot.control(i: Int): Robot {
  keyPress(KeyEvent.VK_CONTROL)
  type(i)
  keyRelease(KeyEvent.VK_CONTROL)
  return this
}

/**
 * Simulates pressing the SHIFT key followed by another key using a [Robot].
 *
 * @param robot The [Robot] instance to use for simulation.
 * @param i     The virtual key code representing the key to press after SHIFT.
 * @return The modified [Robot] instance.
 */
fun Robot.shift(i: Int): Robot {
  keyPress(KeyEvent.VK_SHIFT)
  type(i)
  keyRelease(KeyEvent.VK_SHIFT)
  return this
}

/**
 * Simulates saving something using a [Robot].
 *
 * @param robot The [Robot] instance to use for simulation.
 * @return The modified [Robot] instance.
 */
fun Robot.save(): Robot {
  control(this, KeyEvent.VK_S)
  return this
}