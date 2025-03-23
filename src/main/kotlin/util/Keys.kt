package util

object Keys {
    @JvmStatic
    fun get(key: String): String? {
        when (key) {
            "pico" -> return "e+ZNFkJrKHVLxHRNcKPDsOhxpHt+Q5h4AYjFodeWyFxfhHK2Kpf+/A=="
            "gemini" -> return "AIzaSyB-lFixLFCKlXt__DPlEYkjH-Z8AwS4EEw"
        }
        return null
    }
}
