package util

object Keys {
    operator fun get(key: String): String? =
        when (key) {
            "pico" -> "e+ZNFkJrKHVLxHRNcKPDsOhxpHt+Q5h4AYjFodeWyFxfhHK2Kpf+/A=="
            "gemini" -> "AIzaSyBbmqe_-S2a1L1Ggk_LungNF24nen4SqY0"
            else -> null
        }
}
