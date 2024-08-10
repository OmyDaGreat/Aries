package parse.audio

import ai.picovoice.leopard.*
import kotlinx.coroutines.*
import lombok.experimental.ExtensionMethod
import parse.visual.GUI.Companion.cbLanguage
import util.*
import util.Keys.get
import util.ResourcePath.getLocalResourcePath
import util.audio.*
import util.extension.*
import util.extension.RobotUtils.special
import util.extension.ScrollOption.Companion.showScrollableMessageDialog
import util.notepad.NotepadProcessor
import java.awt.*
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.IOException
import java.net.URI
import java.util.*
import javax.swing.JOptionPane
import kotlin.time.Duration

@ExtensionMethod(RobotUtils::class)
class LiveMic {
    companion object {
        private val n = NotepadProcessor()
        lateinit var leopardthing: Leopard.Builder

        @JvmField
        var maxWords = 40

        private suspend fun initializeLeopard() {
            NativeTTS.tts("Initializing Leopard.")
            leopardthing = Leopard.Builder().setAccessKey(get("pico"))
                .setModelPath(downloadFile(pv, getLocalResourcePath("Aries.pv")).absolutePath)
                .setLibraryPath(when (Platform.detectPlatform()) {
                    Platform.WINDOWS -> downloadFile(
                        leolibwin, getLocalResourcePath("libpv_leopard_jni.dll")
                    ).absolutePath

                    Platform.MAC -> downloadFile(
                        leolibmac, getLocalResourcePath("libpv_leopard_jni.dylib")
                    ).absolutePath

                    Platform.LINUX -> downloadFile(
                        leoliblin, getLocalResourcePath("libpv_leopard_jni.so")
                    ).absolutePath

                    else -> null.also {
                        NativeTTS.tts("Leopard is not supported on this platform.")
                    }
                })
        }

        private fun process(input: String) {
            println("input: $input")
            runBlocking {
                beep()
            }

            when {
                input.contains("Mouse") -> {
                    Robot().mouseMoveString(
                        input.replace("mouse", "", ignoreCase = true).replace("write", "right", ignoreCase = true)
                            .trim()
                    )
                }

                input.trueContainsAny("write special", "right special") -> {
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

                input.trueContainsAny("write", "right") -> {
                    Robot().type(
                        input.replace("write", "", ignoreCase = true).replace("right", "", ignoreCase = true).trim()
                    )
                }

                input.trueContains("ask gemini") -> {
                    ask("Answer the request while staying concise but without contractions: $input")
                }

                input.trueContains("search for") -> {
                    println(
                        open(
                            "https://www.google.com/search?q=" + input.replace("search for", "", ignoreCase = true)
                                .trim().replace(" ", "+")
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

                input.trueContainsAny("switch window") -> {
                    input.replace("switch window", "", ignoreCase = true).trim().split(" ").forEach {
                        it.replaceSpecial().toIntOrNull()?.let { n ->
                            Robot().alt { repeat(n) { Robot().tab() } }
                        }
                    }
                }

                input.trueContains("left press") -> {
                    Robot().mousePress(InputEvent.BUTTON1_DOWN_MASK)
                }

                input.trueContains("left release") -> {
                    Robot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
                }

                input.trueContains("right press") -> {
                    Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK)
                }

                input.trueContains("right release") -> {
                    Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
                }

                input.trueContains("middle click") -> {
                    Robot().apply {
                        mousePress(InputEvent.BUTTON2_DOWN_MASK)
                        mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
                    }
                }

                input.trueContains("left click") -> {
                    Robot().leftClick()
                }

                input.trueContains("right click") -> {
                    Robot().rightClick()
                }

                input.trueContains("alte f") -> {
                    Robot().alt {
                        it.f(
                            input.replace("alte f", "", ignoreCase = true).replaceSpecial().trim().toIntOrNull()
                        )
                    }
                }

                input.trueContains("windows shift") -> {
                    Robot().windows { r1 ->
                        r1.shift { r2 ->
                            r2.type(
                                input.replace("windows shift", "", ignoreCase = true).replaceSpecial().trim()
                                    .lowercase()
                            )
                        }
                    }
                }

                input.trueContains("windows") -> {
                    Robot().windows { r ->
                        r.type(
                            input.replace("windows", "", ignoreCase = true).replaceSpecial().trim().lowercase()
                        )
                    }
                }

                input.trueContains("command shift") -> {
                    Robot().command { r1 ->
                        r1.shift { r2 ->
                            r2.type(
                                input.replace("command shift", "", ignoreCase = true).replaceSpecial().trim()
                                    .lowercase()
                            )
                        }
                    }
                }

                input.trueContainsAny("control shift", "controlled shift") -> {
                    Robot().control { r1 ->
                        r1.shift { r2 ->
                            r2.type(
                                input.replace("control shift", "", ignoreCase = true)
                                    .replace("controlled shift", "", ignoreCase = true).replaceSpecial().trim()
                                    .lowercase()
                            )
                        }
                    }
                }

                input.trueContains("shift") -> {
                    Robot().shift { r ->
                        r.type(
                            input.replace("shift", "", ignoreCase = true).replaceSpecial().trim().lowercase()
                        )
                    }
                }

                input.trueContainsAny("control", "controlled") -> {
                    Robot().control { r ->
                        r.type(
                            input.replace("control", "", ignoreCase = true).replace("controlled", "", ignoreCase = true)
                                .replaceSpecial().trim().lowercase()
                        )
                    }
                }

                input.trueContains("f ") && input[0] == 'f' -> {
                    Robot().f(input.replace("f", "", ignoreCase = true).replaceSpecial().trim().toIntOrNull())
                }

                input.trueContainsAny("command") -> {
                    Robot().command { r ->
                        r.type(
                            input.replace("command", "", ignoreCase = true).replaceSpecial().trim().lowercase()
                        )
                    }
                }

                input.trueContainsAny("scroll", "scrolled") -> {
                    Robot().scroll(input.replace("scroll", "", ignoreCase = true)
                        .replace("scrolled", "", ignoreCase = true).trim { it <= ' ' })
                }

                input.trueContainsAny("open notepad", "opened notepad") -> {
                    n.openNotepad()
                }

                input.trueContainsAny("close notepad", "closed notepad", "clothes notepad") -> {
                    n.closeNotepad()
                }

                input.trueContainsAny("open new", "open knew") -> {
                    n.openNewFile()
                }

                input.trueContains("delete everything") -> {
                    n.deleteText()
                }

                input.trueContains("save file as") -> {
                    println("saving file as")
                    n.saveFileAs(input.replace("save file as", "", ignoreCase = true).trim().replace(" ", "_").also {
                        println("saving file as $it")
                    })
                }

                input.trueContains("save file") -> {
                    println("saving file")
                    n.saveFileAs(input.replace("save file", "", ignoreCase = true).trim().replace(" ", "_").also {
                        println("saving file as $it")
                    })
                }

                input.trueContains("enter") -> {
                    n.addNewLine()
                }

                input.trueContains("arrow") -> {
                    Robot().arrow(input.replace("arrow", "", ignoreCase = true).trim())
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

        fun startRecognition() {
            if (!::leopardthing.isInitialized) {
                runBlocking {
                    initializeLeopard()
                }
            }
            val leopard = leopardthing.build()
            var recorder: Recorder? = null
            println("Aries is ready.")
            NativeTTS.tts("Aries is ready.")
            try {
                processAudio({
                    NativeTTS.tts("Yes?")
                    recorder = Recorder(-1)
                    recorder!!.start()
                }, {
                    recorder!!.end()
                    recorder!!.join()
                    val pcm = recorder!!.pcm
                    recorder = null
                    val transcript = leopard.process(pcm)
                    process(transcript.transcriptString)
                }) {
                    recorder != null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                leopard.delete()
                startRecognition()
            }
        }
    }
}

/**
 * Opens a web page in the system's default browser using a URL string.
 *
 * @param page The URL of the web page to open as a String.
 * @throws IOException If the default browser is not found, or it fails to be launched.
 */
@Throws(IOException::class)
fun open(page: String): String {
    Desktop.getDesktop().browse(URI.create(page))
    return page
}

/**Plays a beep sound. BEEP!*/
suspend fun beep() {
    NativeTTS.tts("Request confirmed.")
    delay(Duration.parse("1s"))
}