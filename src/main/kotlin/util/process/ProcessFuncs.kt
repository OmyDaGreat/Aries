package util.process

import aries.audio.open
import co.touchlab.kermit.Logger
import kotlinx.coroutines.DelicateCoroutinesApi
import util.ai.ask
import util.extension.RobotUtils.special
import util.extension.alt
import util.extension.arrow
import util.extension.command
import util.extension.containsKeyFirst
import util.extension.control
import util.extension.f
import util.extension.getFromFirst
import util.extension.leftClick
import util.extension.mouseMoveString
import util.extension.remove
import util.extension.replaceSpecial
import util.extension.rightClick
import util.extension.scroll
import util.extension.shift
import util.extension.tab
import util.extension.trueContains
import util.extension.type
import util.extension.windows
import util.notepad.NotepadProcessor
import util.visual.logAction
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent.VK_CAPS_LOCK

private val n = NotepadProcessor()

fun handleArrow(input: String) {
    Robot().arrow(input.remove("arrow").trim())
}

fun handleMouse(input: String) {
    Robot().mouseMoveString(input.remove("mouse").replace("write", "right", ignoreCase = true).trim())
}

fun handleRightPress(input: String) {
    when {
        input.trueContains("right press", "write press") -> {
            logAction("Right Mouse Press")
            Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
        }

        input.trueContains("right release", "write release") -> {
            logAction("Right Mouse Release")
            Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
        }

        input.trueContains("right click", "write click") -> {
            Robot().rightClick()
        }
    }
}

fun handleLeftPress(input: String) {
    when {
        input.trueContains("left press") -> {
            logAction("Left Mouse Press")
            Robot().mousePress(InputEvent.BUTTON1_DOWN_MASK)
        }

        input.trueContains("left release") -> {
            logAction("Left Mouse Release")
            Robot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        }

        input.trueContains("left click") -> {
            Robot().leftClick()
        }
    }
}

fun handleSpecial(input: String) {
    input.split(" ").forEach { c ->
        if (special.containsKeyFirst(c)) {
            special.getFromFirst(c).forEach { key -> Robot().keyPress(key) }
            special.getFromFirst(c).reversed().forEach { key -> Robot().keyRelease(key) }
        }
    }
}

fun handleWrite(input: String) {
    Robot().type(input.remove("write", "right").trim())
}

fun handleAskGemini(input: String) {
    Logger.i("Asking Gemini: $input")
    ask("Answer the request while staying concise but without contractions: $input")
}

fun handleSearchFor(input: String) {
    Logger.d(open("https://www.google.com/search?q=${input.remove("search for").trim().replace(" ", "+")}"))
}

fun handleSearch(input: String) {
    Logger.d(open("https://www.google.com/search?q=${input.remove("search ").trim().replace(" ", "+")}"))
}

fun handleCap() {
    logAction("Caps Lock")
    Robot().type(VK_CAPS_LOCK)
}

fun handleSwitchWindow(input: String) {
    input.remove("switch window").trim().split(" ").forEach {
        it.replaceSpecial().toIntOrNull()?.let { n -> Robot().alt { repeat(n) { Robot().tab() } } }
    }
}

fun handleTab() {
    Robot().tab()
}

fun handleMiddleClick() {
    logAction("Middle Click")
    Robot().apply {
        mousePress(InputEvent.BUTTON2_DOWN_MASK)
        mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
    }
}

fun handleAlteF(input: String) {
    Robot().alt {
        it.f(
            input
                .remove("alte f", "alte of")
                .replaceSpecial()
                .trim()
                .toIntOrNull(),
        )
    }
}

fun handleWindowsShift(input: String) {
    Robot().windows { r1 ->
        r1.shift { r2 ->
            r2.type(
                input
                    .remove("windows shift")
                    .replaceSpecial()
                    .trim()
                    .lowercase(),
            )
        }
    }
}

fun handleWindows(input: String) {
    Robot().windows { r ->
        r.type(
            input
                .remove("windows")
                .replaceSpecial()
                .trim()
                .lowercase(),
        )
    }
}

fun handleCommandShift(input: String) {
    Robot().command { r1 ->
        r1.shift { r2 ->
            r2.type(
                input
                    .remove("command shift")
                    .replaceSpecial()
                    .trim()
                    .lowercase(),
            )
        }
    }
}

fun handleControlShift(input: String) {
    Robot().control { r1 ->
        r1.shift { r2 ->
            r2.type(
                input
                    .remove("control shift", "controlled shift")
                    .replaceSpecial()
                    .trim()
                    .lowercase(),
            )
        }
    }
}

fun handleShift(input: String) {
    Robot().shift { r ->
        r.type(
            input
                .remove("shift")
                .replaceSpecial()
                .trim()
                .lowercase(),
        )
    }
}

fun handleControl(input: String) {
    Robot().control { r ->
        r.type(
            input
                .remove("control", "controlled")
                .replaceSpecial()
                .trim()
                .lowercase(),
        )
    }
}

fun handleF(input: String) {
    Robot().f(
        input
            .remove("f")
            .trim()
            .replaceSpecial()
            .toIntOrNull()
            .also { Logger.d("f $it") },
    )
}

fun handleCommand(input: String) {
    Robot().command { r ->
        r.type(
            input
                .remove("command")
                .replaceSpecial()
                .trim()
                .lowercase(),
        )
    }
}

fun handleScroll(input: String) {
    Robot().scroll(input.remove("scroll", "scrolled").trim { it <= ' ' })
}

fun handleNotepad(input: String) {
    when {
        input.trueContains("open notepad", "opened notepad") -> n.openNotepad()
        input.trueContains("close notepad", "closed notepad", "clothes notepad") -> n.closeNotepad()
    }
}

fun handleOpenNew() {
    n.openNewFile()
}

fun handleDeleteEverything() {
    n.deleteText()
}

fun handleSaveFile(input: String) {
    val fileName = input.remove("save file as", "save file").trim().replace(" ", "_")
    println("saving file as $fileName")
    n.saveFileAs(fileName)
}

fun handleEnter() {
    n.addNewLine()
}

@OptIn(DelicateCoroutinesApi::class)
fun handleSetAlarm(input: String) {
    val time = input.remove("set alarm ", "set alarm for ").trim()
    println("Setting alarm for $time")
    setAlarm(time)
}
