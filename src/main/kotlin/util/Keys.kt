package util

object Keys {
    operator fun get(key: String): String? =
        when (key) {
            "pico" -> "emS3S3/V9mmsEP4gljgeB2NrS9ramDVzdqYg49Tn9dpwtFcvfZ4hRQ=="
            "gemini" -> "AIzaSyBbmqe_-S2a1L1Ggk_LungNF24nen4SqY0"
            else -> null
        }
}
