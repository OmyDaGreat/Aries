package util

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val client = HttpClient(CIO)
private val json = Json { ignoreUnknownKeys = true }

/**
 * Generates content using Google's Gemini model.
 *
 * @param prompt The text prompt to send to Gemini
 * @return The generated text response
 */
suspend fun generateContent(prompt: String): String {
    Logger.d("Generating content for prompt: $prompt")

    val request =
        GeminiRequest(
            contents =
                listOf(
                    Content(parts = listOf(Part(text = prompt))),
                ),
        )

    val response =
        client.post(
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${Keys["gemini"]}",
        ) {
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(GeminiRequest.serializer(), request))
        }

    val responseData = json.decodeFromString<GeminiResponse>(response.bodyAsText())
    return responseData.candidates
        .firstOrNull()
        ?.content
        ?.parts
        ?.firstOrNull()
        ?.text
        .orEmpty()
}

@Serializable
private data class GeminiRequest(
    val contents: List<Content>,
)

@Serializable
data class Content(
    val parts: List<Part>,
)

@Serializable
data class Part(
    val text: String,
)

@Serializable
private data class GeminiResponse(
    val candidates: List<Candidate>,
)

@Serializable
data class Candidate(
    val content: Content,
)
