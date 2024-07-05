package util

import lombok.Getter
import org.apache.commons.lang3.SystemUtils

/**
 * Enum representing different platforms on which the application can run.
 * Provides utility methods to detect the current operating system.
 */
@Getter
enum class Platform(val displayName: String) {
    WINDOWS("Windows"), // Represents the Windows operating system
    LINUX("Linux"), // Represents the Linux operating system
    MAC("Mac"), // Represents the macOS operating system
    UNKNOWN("Unknown"); // Represents an unknown operating system

    val fullName: String = System.getProperty("os.name") // The full name of the operating system

    companion object {
        /**
         * Detects the current platform based on system properties.
         *
         * @return The detected platform as a [Platform] enum.
         */
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