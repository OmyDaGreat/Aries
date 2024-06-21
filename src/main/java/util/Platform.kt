package util

import lombok.Getter
import org.apache.commons.lang3.SystemUtils

@Getter
enum class Platform(val displayName: String) {
    WINDOWS("Windows"), LINUX("Linux"), MAC("Mac"), UNKNOWN("Unknown");

    val fullName: String = System.getProperty("os.name")

    companion object {
        fun detectPlatform(): Platform {
            return when {
                SystemUtils.IS_OS_WINDOWS -> WINDOWS
                SystemUtils.IS_OS_LINUX -> LINUX
                SystemUtils.IS_OS_MAC -> MAC
                else -> UNKNOWN
            }
        }
    }
}
