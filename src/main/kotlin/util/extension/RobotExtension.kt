package util.extension

import co.touchlab.kermit.Logger
import util.emu.Direction.Companion.fromString
import util.extension.RobotUtils.special
import java.awt.MouseInfo.getPointerInfo
import java.awt.Robot
import java.awt.event.InputEvent.BUTTON1_DOWN_MASK
import java.awt.event.InputEvent.BUTTON3_DOWN_MASK
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.CHAR_UNDEFINED
import java.awt.event.KeyEvent.VK_CONTROL
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyEvent.VK_F1
import java.awt.event.KeyEvent.VK_F10
import java.awt.event.KeyEvent.VK_F11
import java.awt.event.KeyEvent.VK_F12
import java.awt.event.KeyEvent.VK_F2
import java.awt.event.KeyEvent.VK_F3
import java.awt.event.KeyEvent.VK_F4
import java.awt.event.KeyEvent.VK_F5
import java.awt.event.KeyEvent.VK_F6
import java.awt.event.KeyEvent.VK_F7
import java.awt.event.KeyEvent.VK_F8
import java.awt.event.KeyEvent.VK_F9
import java.awt.event.KeyEvent.VK_S
import java.awt.event.KeyEvent.VK_TAB

/**
 * Moves the mouse cursor based on the specified directions.
 *
 * @param direction A string containing one or more directions (e.g., "up left") to move the mouse
 *   cursor. Each direction should be separated by a space.
 */
fun Robot.mouseMoveString(direction: String) {
    direction.split(" ").mapNotNull { fromString(it) }.forEach { it.tri(getPointerInfo().location.x, getPointerInfo().location.y, this) }
}

/**
 * Simulates typing a sequence of characters.
 *
 * @param keys A string where each character represents a key to be typed by the robot.
 */
fun Robot.type(keys: String) {
    keys.forEach { c ->
        when {
            c == CHAR_UNDEFINED -> Logger.e("Warning: Key code not found for character '\$c'")
            special.containsKeySecond(c) -> special.getFromSecond(c).forEach { key -> type(key) }
            c.isUpperCase() -> shift(KeyEvent.getExtendedKeyCodeForChar(c.code))
            else -> type(KeyEvent.getExtendedKeyCodeForChar(c.code))
        }
    }
}

/**
 * Presses and releases a specified key.
 *
 * @param keyCode The integer code of the key to be pressed and released.
 */
fun Robot.type(keyCode: Int) {
    keyPress(keyCode)
    keyRelease(keyCode)
}

/** Simulates a left mouse click. */
fun Robot.leftClick() = click(BUTTON1_DOWN_MASK)

/** Simulates a right mouse click. */
fun Robot.rightClick() = click(BUTTON3_DOWN_MASK)

/** Simulates pressing the ENTER key. */
fun Robot.enter() = type(VK_ENTER)

/** Simulates pressing the TAB key. */
fun Robot.tab() = type(VK_TAB)

/**
 * Simulates pressing CONTROL + another key.
 *
 * @param keyCode The integer code of the key to be pressed in combination with the CONTROL key.
 */
fun Robot.control(keyCode: Int) {
    keyPress(VK_CONTROL)
    type(keyCode)
    keyRelease(VK_CONTROL)
}

/**
 * Simulates pressing CONTROL + doing something else.
 *
 * @param action The function to be done in combination with the CONTROL key.
 */
fun Robot.control(action: (Robot) -> Unit) {
    keyPress(VK_CONTROL)
    action(this)
    keyRelease(VK_CONTROL)
}

fun Robot.f(i: Int?) {
    if (i == null) return
    when (i) {
        1 -> type(VK_F1)
        2 -> type(VK_F2)
        3 -> type(VK_F3)
        4 -> type(VK_F4)
        5 -> type(VK_F5)
        6 -> type(VK_F6)
        7 -> type(VK_F7)
        8 -> type(VK_F8)
        9 -> type(VK_F9)
        10 -> type(VK_F10)
        11 -> type(VK_F11)
        12 -> type(VK_F12)
    }
}

/**
 * Simulates pressing COMMAND + another key.
 *
 * @param keyCode The integer code of the key to be pressed in combination with the COMMAND key.
 */
fun Robot.command(keyCode: Int) {
    keyPress(KeyEvent.VK_META)
    type(keyCode)
    keyRelease(KeyEvent.VK_META)
}

/**
 * Simulates pressing CMD + doing something else.
 *
 * @param action The function to be done in combination with the CMD key.
 */
fun Robot.command(action: (Robot) -> Unit) {
    keyPress(KeyEvent.VK_META)
    action(this)
    keyRelease(KeyEvent.VK_META)
}

/**
 * Simulates pressing SHIFT + another key.
 *
 * @param keyCode The integer code of the key to be pressed in combination with the SHIFT key.
 */
fun Robot.shift(keyCode: Int) {
    keyPress(KeyEvent.VK_SHIFT)
    type(keyCode)
    keyRelease(KeyEvent.VK_SHIFT)
}

/**
 * Simulates pressing SHIFT + doing something else.
 *
 * @param action The function to be done in combination with the SHIFT key.
 */
fun Robot.shift(action: (Robot) -> Unit) {
    keyPress(KeyEvent.VK_SHIFT)
    action(this)
    keyRelease(KeyEvent.VK_SHIFT)
}

/**
 * Simulates pressing WINDOWS + doing something else.
 *
 * @param action The function to be done in combination with the WINDOWS key.
 */
fun Robot.windows(action: (Robot) -> Unit) {
    keyPress(KeyEvent.VK_WINDOWS)
    action(this)
    keyRelease(KeyEvent.VK_WINDOWS)
}

/**
 * Simulates pressing ALT + doing something else.
 *
 * @param action The function to be done in combination with the ALT key.
 */
fun Robot.alt(action: (Robot) -> Unit) {
    keyPress(KeyEvent.VK_ALT)
    action(this)
    keyRelease(KeyEvent.VK_ALT)
}

/**
 * Scrolls the mouse wheel up or down based on the specified direction.
 *
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
 *
 * @param directions A string containing directions (e.g., "up down left right") to move the mouse
 *   cursor. Each direction should be separated by a space.
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

/** Simulates saving something by pressing CONTROL + S. */
fun Robot.save() = control(VK_S)

/**
 * Helper function to simulate mouse clicks.
 *
 * @param mask The mask for the mouse button to be clicked.
 */
private fun Robot.click(mask: Int) {
    mousePress(mask)
    mouseRelease(mask)
}
