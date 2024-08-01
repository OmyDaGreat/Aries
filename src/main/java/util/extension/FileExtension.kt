package util.extension

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.io.File

// URL of the icon image
const val icon =
  "https://www.dropbox.com/scl/fi/woan0cno18kodgghthbn9/java.png?rlkey=rnxt8unmblb476lfgsena00lb&st=2ktqz7ef&dl=1"

// URL of the Aries.pv file
const val pv =
  "https://www.dropbox.com/scl/fi/f4370qnb7d05e90odjebo/Aries.pv?rlkey=a1ceeoipvdah4nosr00w3blv6&st=uoc4kwn3&dl=1"

// URL of the db.properties file
const val props =
  "https://www.dropbox.com/scl/fi/uhod5m8oqvhoq89ank0tf/db.properties?rlkey=iwvuaq617j5099zgjqgzm9bjm&st=ge9s493z&dl=1"

// URL of the openNotepad.scpt file
const val openNotepad =
  "https://www.dropbox.com/scl/fi/lppie7xcxbzloo860t7qg/openNotepad.scpt?rlkey=xv413n4dclvsjj6hdj580u0s6&st=1uk85psv&dl=1"

/**
 * Downloads a file from the specified URL to the given destination path.
 * If the file already exists at the destination, it will not be downloaded again.
 *
 * @param fileURL The URL of the file to download.
 * @param destinationPath The path where the downloaded file should be saved.
 * @return The downloaded file.
 * @throws Exception if the file download fails.
 */
suspend fun downloadFile(fileURL: String, destinationPath: String): File {
  return File(destinationPath).apply {
    if (!exists()) {
      HttpClient(CIO){
        install(HttpTimeout) {
          requestTimeoutMillis = 120000 // 2 minutes
        }
      }.use {
        val fileBytes: ByteArray = it.get(fileURL).readBytes()
        writeBytes(fileBytes)
        println("File downloaded successfully.")
      }
    }
  }
}