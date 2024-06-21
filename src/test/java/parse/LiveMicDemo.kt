package parse

import ai.picovoice.leopard.LeopardException
import java.awt.AWTException
import java.io.IOException

object LiveMicDemo {
    @Throws(IOException::class, InterruptedException::class, AWTException::class, LeopardException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        LiveMic.startRecognition()
    }
}
