package parse

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import parse.audio.LiveMic
import parse.visual.GUI

fun startGUI() = runBlocking { launch(Dispatchers.Swing) { GUI.run() } }

fun startTranscriber() = runBlocking { launch(Dispatchers.IO) { LiveMic.startRecognition() } }
