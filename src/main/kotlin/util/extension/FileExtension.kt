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
    "https://www.dropbox.com/scl/fi/x4zl6mzfpm2g8j9d461z3/icon.png?rlkey=l63vlj7hywe1p97p9mcgdwc2k&st=83ot1zd3&dl=1"

// URL of the Aries.pv file
const val PV =
    "https://www.dropbox.com/scl/fi/f4370qnb7d05e90odjebo/Aries.pv?rlkey=a1ceeoipvdah4nosr00w3blv6&st=uoc4kwn3&dl=1"

// URL of the openNotepad.scpt file
const val SCPT =
    "https://www.dropbox.com/scl/fi/lppie7xcxbzloo860t7qg/openNotepad.scpt?rlkey=xv413n4dclvsjj6hdj580u0s6&st=1uk85psv&dl=1"

// URL of leopard library for windows
const val LEOLIBWIN =
    "https://www.dropbox.com/scl/fi/mztcz5q5p8qiv91vfchi8/libpv_leopard_jni.dll?rlkey=9aumenejyt39iewm2d8kwze6b&st=qaw0fewr&dl=1"

// URL of leopard library for mac (arm64 rn)
const val LEOLIBMAC =
    "https://www.dropbox.com/scl/fi/x0qk73f1i1roal45j8m5y/arm64.dylib?rlkey=g776duwkn61n0zsi0j9bocob1&st=nwq78i3n&dl=1"

// URL of leopard library for linux
const val LEOLIBLIN =
    "https://www.dropbox.com/scl/fi/v12jnksld7bhepd40tp0s/libpv_leopard_jni.so?rlkey=w6votdishp3bre7t4d9dmhstf&st=oz8rmcti&dl=1"

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
