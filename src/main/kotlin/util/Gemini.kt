package util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun generateContent(prompt: String): String {
  val client = HttpClient(CIO) { install(ContentNegotiation) { json() } }

  val requestBody = mapOf("prompt" to prompt)
  val json = Json { ignoreUnknownKeys = true }
  val requestBodyString = json.encodeToString(requestBody)

  val response: HttpResponse =
    client.post("https://jaguar-gentle-seriously.ngrok-free.app/gemini") {
      contentType(ContentType.Application.Json)
      setBody(requestBodyString)
    }

  val responseBody = response.bodyAsText()
  val responseMap: Map<String, String> = json.decodeFromString(responseBody)
  return responseMap["response"] ?: ""
}
