package parse

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import parse.gui.GUI
import parse.listen.LiveMic

fun startGUI() = runBlocking {
  launch {
    GUI.run()
  }
}

fun startTranscriber() = runBlocking {
  launch {
    LiveMic.startRecognition()
  }
}