package util

import lombok.experimental.UtilityClass

@UtilityClass
object Keys {
    @JvmStatic
    fun get(key: String): String? {
        if (key == "gemini") {
            return "AIzaSyAylywSkfIpglOjxkRKsVd_VZS_h5J-rao"
        } else if (key == "pico") {
            return "u2IdJ5z7zacX5h5uWJLL01HWeeIjvgFDFBdW+JOiFedkL2MiPpwB1w=="
        }
        return null
    }
}
