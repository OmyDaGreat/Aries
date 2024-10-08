package util

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun generateContent(prompt: String): String {
  Logger.d("Generating content for prompt: $prompt")
  val body = mapOf("contents" to listOf(mapOf("parts" to listOf(mapOf("text" to prompt)))))

  val json = Json { ignoreUnknownKeys = true }
  val bodyString = json.encodeToString(body)

  val client = HttpClient(CIO)
  val response: HttpResponse =
    client.post(
      "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${Keys.get("gemini")}"
    ) {
      contentType(ContentType.Application.Json)
      setBody(bodyString)
    }

  val responseBody = response.bodyAsText()
  val jsonResponse = json.decodeFromString<Response>(responseBody)

  val firstCandidateText =
    jsonResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""

  return firstCandidateText
}

@Serializable data class Part(val text: String)

@Serializable data class Content(val parts: List<Part>)

@Serializable data class Candidate(val content: Content)

@Serializable data class Response(val candidates: List<Candidate>)
