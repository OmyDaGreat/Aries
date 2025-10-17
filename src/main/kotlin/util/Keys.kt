package util

object Keys {
    operator fun get(key: String): String? =
        when (key) {
            "gemini" -> "AIzaSyBbmqe_-S2a1L1Ggk_LungNF24nen4SqY0"
            else -> null
        }
}
