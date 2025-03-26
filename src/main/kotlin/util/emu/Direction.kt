package util.emu

import java.awt.Robot

/**
 * Represents the four cardinal directions.
 * Provides utility methods for converting string inputs to Direction values.
 */
enum class Direction(
    val tri: (Int, Int, Robot) -> Unit,
) {
    UP(tri = { x, y, robot -> robot.mouseMove(x, y - 50) }),
    DOWN(tri = { x, y, robot -> robot.mouseMove(x, y + 50) }),
    LEFT(tri = { x, y, robot -> robot.mouseMove(x - 50, y) }),
    RIGHT(tri = { x, y, robot -> robot.mouseMove(x + 50, y) }),
    ;

    companion object {
        /**
         * Converts a string to a Direction enum value.
         *
         * @param input The string representation of a direction (case-insensitive).
         * @return The corresponding Direction enum value, or null if no match is found.
         */
        fun fromString(input: String) =
            when (input.lowercase()) {
                "up" -> UP
                "down" -> DOWN
                "left" -> LEFT
                "right", "write" -> RIGHT
                else -> null
            }
    }
}
