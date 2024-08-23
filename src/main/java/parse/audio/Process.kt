package parse.audio

import kotlinx.coroutines.*
import util.*
import util.extension.*
import util.extension.RobotUtils.special
import util.notepad.NotepadProcessor
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

private val n = NotepadProcessor()
private val scope = CoroutineScope(Dispatchers.Default)

fun process(input: String) {
  println("input: $input")
  beep()

  when {
    input.trueContains("arrow") -> handleArrow(input)
    input.trueContains("mouse") -> handleMouse(input)
    input.trueContains("right press", "write press", "right release", "write release", "right click", "write click") -> handleRightPress(input)
    input.trueContains("left press", "left release", "left click") -> handleLeftPress(input)
    input.trueContains("write special", "right special") -> handleSpecial(input)
    input.trueContains("write", "right") -> handleWrite(input)
    input.trueContains("ask gemini") -> handleAskGemini(input)
    input.trueContains("search for") -> handleSearchFor(input)
    input.trueContains("search") -> handleSearch(input)
    input.trueContains("cap") -> handleCap()
    input.trueContains("switch window") -> handleSwitchWindow(input)
    input.trueContains("tab") -> handleTab()
    input.trueContains("middle click") -> handleMiddleClick()
    input.trueContains("alte f", "alte of") -> handleAlteF(input)
    input.trueContains("windows shift") -> handleWindowsShift(input)
    input.trueContains("windows") -> handleWindows(input)
    input.trueContains("command shift") -> handleCommandShift(input)
    input.trueContains("control shift", "controlled shift") -> handleControlShift(input)
    input.trueContains("shift") -> handleShift(input)
    input.trueContains("control", "controlled") -> handleControl(input)
    input.trueContains("f ") -> handleF(input)
    input.trueContains("command") -> handleCommand(input)
    input.trueContains("scroll", "scrolled") -> handleScroll(input)
    input.trueContains("open notepad", "opened notepad", "close notepad", "closed notepad", "clothes notepad") -> handleNotepad(input)
    input.trueContains("open new", "open knew") -> handleOpenNew()
    input.trueContains("delete everything") -> handleDeleteEverything()
    input.trueContains("save file as", "save file") -> handleSaveFile(input)
    input.trueContains("enter") -> handleEnter()
    input.trueContains("set alarm", "set alarm for ") -> handleSetAlarm(input)
    else -> handleDefault(input)
  }
}

private fun handleArrow(input: String) {
  Robot().arrow(input.remove("arrow").trim())
}

private fun handleMouse(input: String) {
  Robot().mouseMoveString(input.remove("mouse").replace("write", "right", ignoreCase = true).trim())
}

private fun handleRightPress(input: String) {
  when {
    input.trueContains("right press", "write press") -> Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
    input.trueContains("right release", "write release") -> Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
    input.trueContains("right click", "write click") -> Robot().rightClick()
  }
}

private fun handleLeftPress(input: String) {
  when {
    input.trueContains("left press") -> Robot().mousePress(InputEvent.BUTTON1_DOWN_MASK)
    input.trueContains("left release") -> Robot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    input.trueContains("left click") -> Robot().leftClick()
  }
}

private fun handleSpecial(input: String) {
  input.split(" ").forEach { c ->
    if (special.containsKeyFirst(c)) {
      special.getFromFirst(c).forEach { key -> Robot().keyPress(key) }
      special.getFromFirst(c).reversed().forEach { key -> Robot().keyRelease(key) }
    }
  }
}

private fun handleWrite(input: String) {
  Robot().type(input.remove("write", "right").trim())
}

private fun handleAskGemini(input: String) {
  ask("Answer the request while staying concise but without contractions: $input")
}

private fun handleSearchFor(input: String) {
  println(open("https://www.google.com/search?q=${input.remove("search for").trim().replace(" ", "+")}"))
}

private fun handleSearch(input: String) {
  println(open("https://www.google.com/search?q=${input.remove("search ").trim().replace(" ", "+")}"))
}

private fun handleCap() {
  Robot().type(KeyEvent.VK_CAPS_LOCK)
}

private fun handleSwitchWindow(input: String) {
  input.remove("switch window").trim().split(" ").forEach {
    it.replaceSpecial().toIntOrNull()?.let { n -> Robot().alt { repeat(n) { Robot().tab() } } }
  }
}

private fun handleTab() {
  Robot().tab()
}

private fun handleMiddleClick() {
  Robot().apply {
    mousePress(InputEvent.BUTTON2_DOWN_MASK)
    mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
  }
}

private fun handleAlteF(input: String) {
  Robot().alt { it.f(input.remove("alte f", "alte of").replaceSpecial().trim().toIntOrNull()) }
}

private fun handleWindowsShift(input: String) {
  Robot().windows { r1 ->
    r1.shift { r2 ->
      r2.type(input.remove("windows shift").replaceSpecial().trim().lowercase())
    }
  }
}

private fun handleWindows(input: String) {
  Robot().windows { r -> r.type(input.remove("windows").replaceSpecial().trim().lowercase()) }
}

private fun handleCommandShift(input: String) {
  Robot().command { r1 ->
    r1.shift { r2 ->
      r2.type(input.remove("command shift").replaceSpecial().trim().lowercase())
    }
  }
}

private fun handleControlShift(input: String) {
  Robot().control { r1 ->
    r1.shift { r2 ->
      r2.type(input.remove("control shift", "controlled shift").replaceSpecial().trim().lowercase())
    }
  }
}

private fun handleShift(input: String) {
  Robot().shift { r -> r.type(input.remove("shift").replaceSpecial().trim().lowercase()) }
}

private fun handleControl(input: String) {
  Robot().control { r ->
    r.type(input.remove("control", "controlled").replaceSpecial().trim().lowercase())
  }
}

private fun handleF(input: String) {
  Robot().f(input.remove("f").trim().replaceSpecial().toIntOrNull().also { println("f $it") })
}

private fun handleCommand(input: String) {
  Robot().command { r -> r.type(input.remove("command").replaceSpecial().trim().lowercase()) }
}

private fun handleScroll(input: String) {
  Robot().scroll(input.remove("scroll", "scrolled").trim { it <= ' ' })
}

private fun handleNotepad(input: String) {
  when {
    input.trueContains("open notepad", "opened notepad") -> n.openNotepad()
    input.trueContains("close notepad", "closed notepad", "clothes notepad") -> n.closeNotepad()
  }
}

private fun handleOpenNew() {
  n.openNewFile()
}

private fun handleDeleteEverything() {
  n.deleteText()
}

private fun handleSaveFile(input: String) {
  val fileName = input.remove("save file as", "save file").trim().replace(" ", "_")
  println("saving file as $fileName")
  n.saveFileAs(fileName)
}

private fun handleEnter() {
  n.addNewLine()
}

private fun handleSetAlarm(input: String) {
  val time = input.remove("set alarm ", "set alarm for ").trim()
  println("Setting alarm for $time")
  scope.launch { setAlarm(time) }
}

private fun handleDefault(input: String) {
  ask("Answer the request while staying concise but without contractions: $input")
}
