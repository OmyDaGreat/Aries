package util

object Keys {
    operator fun get(key: String): String? =
        when (key) {
            "pico" -> "e+ZNFkJrKHVLxHRNcKPDsOhxpHt+Q5h4AYjFodeWyFxfhHK2Kpf+/A=="
            "gemini" -> "AIzaSyB-lFixLFCKlXt__DPlEYkjH-Z8AwS4EEw"
            else -> null
        }
}
