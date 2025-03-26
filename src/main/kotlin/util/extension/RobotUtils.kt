package util.extension

import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_0
import java.awt.event.KeyEvent.VK_1
import java.awt.event.KeyEvent.VK_2
import java.awt.event.KeyEvent.VK_3
import java.awt.event.KeyEvent.VK_4
import java.awt.event.KeyEvent.VK_5
import java.awt.event.KeyEvent.VK_6
import java.awt.event.KeyEvent.VK_7
import java.awt.event.KeyEvent.VK_8
import java.awt.event.KeyEvent.VK_9
import java.awt.event.KeyEvent.VK_BACK_QUOTE
import java.awt.event.KeyEvent.VK_COMMA
import java.awt.event.KeyEvent.VK_PERIOD
import java.awt.event.KeyEvent.VK_QUOTE
import java.awt.event.KeyEvent.VK_SEMICOLON
import java.awt.event.KeyEvent.VK_SHIFT
import java.awt.event.KeyEvent.VK_SLASH

object RobotUtils {
    val screenWidth = Toolkit.getDefaultToolkit().screenSize.width
    val screenHeight = Toolkit.getDefaultToolkit().screenSize.height

    val special =
        linkedMapOf(
            Pair("question", '?') to listOf(VK_SHIFT, VK_SLASH),
            Pair("colon", ':') to listOf(VK_SHIFT, VK_SEMICOLON),
            Pair("quote", '"') to listOf(VK_SHIFT, VK_QUOTE),
            Pair("tilde", '~') to listOf(VK_SHIFT, VK_BACK_QUOTE),
            Pair("apostrophe", '\'') to listOf(VK_QUOTE),
            Pair("tab", '\t') to listOf(KeyEvent.VK_TAB),
            Pair("exclamation", '!') to listOf(VK_SHIFT, VK_1),
            Pair("at", '@') to listOf(VK_SHIFT, VK_2),
            Pair("hashtag", '#') to listOf(VK_SHIFT, VK_3),
            Pair("octothorpe", '#') to listOf(VK_SHIFT, VK_3),
            Pair("dollar", '$') to listOf(VK_SHIFT, VK_4),
            Pair("percent", '%') to listOf(VK_SHIFT, VK_5),
            Pair("up", '^') to listOf(VK_SHIFT, VK_6),
            Pair("and", '&') to listOf(VK_SHIFT, VK_7),
            Pair("asterisk", '*') to listOf(VK_SHIFT, VK_8),
            Pair("open", '(') to listOf(VK_SHIFT, VK_9),
            Pair("close", ')') to listOf(VK_SHIFT, VK_0),
            Pair("left", '<') to listOf(VK_SHIFT, VK_COMMA),
            Pair("right", '>') to listOf(VK_SHIFT, VK_PERIOD),
            Pair("semicolon", ';') to listOf(VK_SEMICOLON),
            Pair("comma", ',') to listOf(VK_COMMA),
            Pair("period", '.') to listOf(VK_PERIOD),
            Pair("tilde", '.') to listOf(VK_SHIFT, VK_BACK_QUOTE),
            Pair("back quote", '.') to listOf(VK_BACK_QUOTE),
            Pair("zero", '0') to listOf(VK_0),
            Pair("one", '1') to listOf(VK_1),
            Pair("two", '2') to listOf(VK_2),
            Pair("three", '3') to listOf(VK_3),
            Pair("four", '4') to listOf(VK_4),
            Pair("for", '4') to listOf(VK_4),
            Pair("five", '5') to listOf(VK_5),
            Pair("six", '6') to listOf(VK_6),
            Pair("seven", '7') to listOf(VK_7),
            Pair("eight", '8') to listOf(VK_8),
            Pair("nine", '9') to listOf(VK_9),
            Pair("space", ' ') to listOf(KeyEvent.VK_SPACE),
            Pair("slash", '/') to listOf(VK_SLASH),
            Pair("backslash", '\\') to listOf(KeyEvent.VK_BACK_SLASH),
            Pair("single quote", '\'') to listOf(VK_QUOTE),
        )
}
