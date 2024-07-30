package parse

import util.audio.processAudio

fun main() {
  processAudio({
    println("Keyword detected")
  }, {
    println("Silence detected")
  }, {
    false
  })
}
