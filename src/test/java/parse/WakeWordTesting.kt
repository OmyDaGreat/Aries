package parse

import util.listen.processAudio

fun main() {
  processAudio({
    println("Keyword detected")
  }, {
    println("Silence detected")
  }, {
    false
  })
}
