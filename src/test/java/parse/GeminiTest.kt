package parse

import util.Secrets

fun main() {
    val generativeModel = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with both text-only and multimodal prompts
        modelName = "gemini-1.5-flash",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = Secrets.get("gemini")
    )
    val prompt = "Write a story about a magic backpack."
    val response = generativeModel.generateContent(prompt)
    print(response.text)
}