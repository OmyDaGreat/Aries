package parse

import org.apache.logging.log4j.LogManager

fun main() = System.getProperties().forEach {(prop, value) -> LogManager.getLogger().info("$prop=$value")}