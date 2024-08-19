package parse.audio

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import parse.audio.LiveMic.Companion.maxWords
import parse.visual.GUI.Companion.cbLanguage
import util.audio.NativeTTS
import util.audio.generateContent
import util.extension.*
import util.extension.RobotUtils.special
import util.extension.ScrollOption.Companion.showScrollableMessageDialog
import util.notepad.NotepadProcessor
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.JOptionPane

private val n = NotepadProcessor()

fun process(input: String) {
    println("input: $input")
    runBlocking {
        beep()
    }

    when {
        input.trueContains("arrow") -> {
            Robot().arrow(input.remove("arrow").trim())
        }

        input.trueContains("mouse") -> {
            Robot().mouseMoveString(
                input.remove("mouse").replace("write", "right", ignoreCase = true).trim()
            )
        }

        input.trueContains(
            "right press", "write press", "right release", "write release", "right click", "write click"
        ) -> {
            when {
                input.trueContains("right press", "write press") -> Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
                input.trueContains(
                    "right release", "write release"
                ) -> Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)

                input.trueContains("right click", "write click") -> Robot().rightClick()
            }
        }

        input.trueContains("left press", "left release", "left click") -> {
            when {
                input.trueContains("left press") -> Robot().mousePress(InputEvent.BUTTON1_DOWN_MASK)
                input.trueContains("left release") -> Robot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
                input.trueContains("left click") -> Robot().leftClick()
            }
        }

        input.trueContains("write special", "right special") -> {
            input.split(" ").forEach { c ->
                if (special.containsKeyFirst(c)) {
                    special.getFromFirst(c).forEach { key ->
                        Robot().keyPress(key)
                    }
                    special.getFromFirst(c).reversed().forEach { key ->
                        Robot().keyRelease(key)
                    }
                }
            }
        }

        input.trueContains("write", "right") -> {
            Robot().type(
                input.remove("write", "right").trim()
            )
        }

        input.trueContains("ask gemini") -> {
            ask("Answer the request while staying concise but without contractions: $input")
        }

        input.trueContains("search for") -> {
            println(
                open(
                    "https://www.google.com/search?q=" + input.remove("search for").trim().replace(" ", "+")
                )
            )
        }

        input.trueContains("search") -> {
            println(
                open(
                    "https://www.google.com/search?q=" + input.replace("search ", "", ignoreCase = true).trim()
                        .replace(" ", "+")
                )
            )
        }

        input.trueContains("cap") -> {
            Robot().type(KeyEvent.VK_CAPS_LOCK)
        }

        input.trueContains("switch window") -> {
            input.remove("switch window").trim().split(" ").forEach {
                it.replaceSpecial().toIntOrNull()?.let { n ->
                    Robot().alt { repeat(n) { Robot().tab() } }
                }
            }
        }

        input.trueContains("tab") -> {
            Robot().tab()
        }

        input.trueContains("middle click") -> {
            Robot().apply {
                mousePress(InputEvent.BUTTON2_DOWN_MASK)
                mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
            }
        }

        input.trueContains("alte f", "alte of") -> {
            Robot().alt {
                it.f(
                    input.remove("alte f", "alte of").replaceSpecial().trim().toIntOrNull()
                )
            }
        }

        input.trueContains("windows shift") -> {
            Robot().windows { r1 ->
                r1.shift { r2 ->
                    r2.type(
                        input.remove("windows shift").replaceSpecial().trim().lowercase()
                    )
                }
            }
        }

        input.trueContains("windows") -> {
            Robot().windows { r ->
                r.type(
                    input.remove("windows").replaceSpecial().trim().lowercase()
                )
            }
        }

        input.trueContains("command shift") -> {
            Robot().command { r1 ->
                r1.shift { r2 ->
                    r2.type(
                        input.remove("command shift").replaceSpecial().trim().lowercase()
                    )
                }
            }
        }

        input.trueContains("control shift", "controlled shift") -> {
            Robot().control { r1 ->
                r1.shift { r2 ->
                    r2.type(
                        input.remove("control shift", "controlled shift").replaceSpecial().trim().lowercase()
                    )
                }
            }
        }

        input.trueContains("shift") -> {
            Robot().shift { r ->
                r.type(
                    input.remove("shift").replaceSpecial().trim().lowercase()
                )
            }
        }

        input.trueContains("control", "controlled") -> {
            Robot().control { r ->
                r.type(
                    input.remove("control", "controlled").replaceSpecial().trim().lowercase()
                )
            }
        }

        input.trueContains("f ") -> {
            Robot().f(input.remove("f").trim().replaceSpecial().toIntOrNull().also {
                println("f $it")
            })
        }

        input.trueContains("command") -> {
            Robot().command { r ->
                r.type(
                    input.remove("command").replaceSpecial().trim().lowercase()
                )
            }
        }

        input.trueContains("scroll", "scrolled") -> {
            Robot().scroll(input.remove("scroll", "scrolled").trim { it <= ' ' })
        }

        input.trueContains("open notepad", "opened notepad", "close notepad", "closed notepad", "clothes notepad") -> {
            when {
                input.trueContains("open notepad", "opened notepad") -> n.openNotepad()
                input.trueContains("close notepad", "closed notepad", "clothes notepad") -> n.closeNotepad()
            }
        }

        input.trueContains("open new", "open knew") -> {
            n.openNewFile()
        }

        input.trueContains("delete everything") -> {
            n.deleteText()
        }

        input.trueContains("save file as", "save file") -> {
            val fileName = input.remove("save file as", "save file").trim().replace(" ", "_")
            println("saving file as $fileName")
            n.saveFileAs(fileName)
        }

        input.trueContains("enter") -> {
            n.addNewLine()
        }

        else -> {
            ask("Answer the request while staying concise but without contractions: $input")
        }
    }
}

private fun ask(input: String) {
    runBlocking {
        var gemini = generateContent(input).replace("*", "")
        if (cbLanguage.selectedItem!! != "en") {
            gemini = generateContent("Translate $gemini to ${cbLanguage.selectedItem}")
        }
        launch {
            if (gemini.split(" ").size > maxWords) {
                NativeTTS.tts("The response is over $maxWords words.")
            } else {
                NativeTTS.tts(gemini)
            }
        }
        Thread {
            showScrollableMessageDialog(
                null, gemini, "Gemini is responding to ${
                    input.replace(
                        "Answer the request while staying concise but without contractions: ", ""
                    ).replaceFirst("Translate ", "").replaceFirst(" to ${cbLanguage.selectedItem}", "")
                }", JOptionPane.INFORMATION_MESSAGE
            )
        }.start()
    }
}