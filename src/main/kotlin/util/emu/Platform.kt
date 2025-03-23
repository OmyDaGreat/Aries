package util.emu

import org.apache.commons.lang3.SystemUtils

/**
 * Enum representing different platforms on which the application can run. Provides utility methods
 * to detect the current operating system.
 */
enum class Platform {
    WINDOWS, // Represents the Windows operating system
    LINUX, // Represents the Linux operating system
    MAC, // Represents the macOS operating system
    UNKNOWN, // Represents an unknown operating system
    ;

    companion object {
        /**
         * Detects the current platform based on system properties.
         *
         * @return The detected platform as a [Platform] enum.
         */
        val currentPlatform: Platform by lazy {
            when {
                SystemUtils.IS_OS_WINDOWS -> WINDOWS
                SystemUtils.IS_OS_LINUX -> LINUX
                SystemUtils.IS_OS_MAC -> MAC
                else -> UNKNOWN
            }
        }
    }
}
