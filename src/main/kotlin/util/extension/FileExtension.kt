package util.extension

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import java.io.File

// URL of the icon image
const val ICON =
    "https://www.dropbox.com/scl/fi/4o3busy75hk1mga2i665g/aries.png?rlkey=fp7yrxl5lz5ldfcjndsiikus2&st=m9ydm8op&dl=1"

// URL of the Aries.pv file
const val PV =
    "https://www.dropbox.com/scl/fi/f4370qnb7d05e90odjebo/Aries.pv?rlkey=a1ceeoipvdah4nosr00w3blv6&st=uoc4kwn3&dl=1"

// URL of the openNotepad.scpt file
const val SCPT =
    "https://www.dropbox.com/scl/fi/lppie7xcxbzloo860t7qg/openNotepad.scpt?rlkey=xv413n4dclvsjj6hdj580u0s6&st=1uk85psv&dl=1"

/**
 * Downloads a file from the specified URL and saves it to the given destination path.
 *
 * @param fileURL The URL of the file to download.
 * @param destinationPath The path where the downloaded file will be saved.
 * @return The downloaded file.
 */
suspend fun downloadFile(
    fileURL: String,
    destinationPath: String,
): File {
    val file = File(destinationPath)
    file.parentFile?.mkdirs()
    if (file.exists()) {
        Logger.d("File already exists.")
        return file
    }

    Logger.d("Downloading file ${file.name}.")
    HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 120000 // 2 minutes
        }
    }.use { client ->
        val fileBytes: ByteArray = client.get(fileURL).readRawBytes()
        file.writeBytes(fileBytes)
        Logger.d("File downloaded successfully.")
    }
    return file
}
