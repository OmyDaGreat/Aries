package util

object Keys {
  @JvmStatic
  fun get(key: String): String? {
    when (key) {
      "gemini" -> return "AIzaSyB-lFixLFCKlXt__DPlEYkjH-Z8AwS4EEw"
      "pico" -> return "G4H0dLn45eKqXR/LN+f+2QklChcsCaJGkIqsiNyD/5jU/jxnNVv8sQ=="
    }
    return null
  }
}
