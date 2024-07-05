package parse

import kotlinx.coroutines.*

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