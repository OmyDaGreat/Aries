package parse

import lombok.experimental.UtilityClass

@UtilityClass
internal object ShowProperties {
    @JvmStatic
    fun main(args: Array<String>) {
        System.getProperties().list(System.out)
    }
}