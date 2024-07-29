package parse

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import parse.listen.LiveMic

fun startGUI() = runBlocking {
  launch(Dispatchers.Swing) {
    GUI.run()
  }
}

fun startTranscriber() = runBlocking {
  launch(Dispatchers.IO) {
    LiveMic.startRecognition()
  }
}