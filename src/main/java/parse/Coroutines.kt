package parse

import kotlinx.coroutines.*
import parse.listen.LiveMic

fun startGUI() = runBlocking {
  launch {
    GUI.run()
  }
}

fun startTranscriber() = runBlocking {
  launch(Dispatchers.IO) {
    LiveMic.startRecognition()
  }
}