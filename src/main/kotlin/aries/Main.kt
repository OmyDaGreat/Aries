package aries

import aries.audio.LiveMic
import aries.visual.openWindow
import aries.visual.tray
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  println("Opening window")
  openWindow()
  println("Opening tray")
  tray() // doesn't actually open unless window isn't open and dies while window is open
  LiveMic.startRecognition() // immediately dies after yes?
}
