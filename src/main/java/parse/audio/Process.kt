package parse.audio

import util.process.beep
import util.extension.trueContains
import util.process.*

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
    else -> handleAskGemini(input)
  }
}
