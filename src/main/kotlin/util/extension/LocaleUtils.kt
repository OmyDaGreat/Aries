package util.extension

import java.util.Locale

object LocaleUtils {
    /**
     * Get a display name for a language code with fallback to the code itself
     */
    fun getLanguageDisplayName(languageCode: String): String {
        return try {
            val locale = Locale(languageCode)
            val displayName = locale.getDisplayLanguage(Locale.ENGLISH)
            if (displayName.isNotEmpty() && displayName != languageCode) {
                "$displayName ($languageCode)"
            } else {
                languageCode.uppercase()
            }
        } catch (e: Exception) {
            languageCode.uppercase()
        }
    }

    /**
     * Get a display name for a country code with fallback to the code itself
     */
    fun getCountryDisplayName(countryCode: String): String {
        return try {
            val locale = Locale("", countryCode)
            val displayName = locale.getDisplayCountry(Locale.ENGLISH)
            if (displayName.isNotEmpty() && displayName != countryCode) {
                "$displayName ($countryCode)"
            } else {
                countryCode.uppercase()
            }
        } catch (e: Exception) {
            countryCode.uppercase()
        }
    }

    /**
     * Get formatted list of languages with display names
     */
    fun getFormattedLanguages(): List<String> {
        return Locale.getISOLanguages().map { languageCode ->
            getLanguageDisplayName(languageCode)
        }.sorted()
    }

    /**
     * Get formatted list of countries with display names
     */
    fun getFormattedCountries(): List<String> {
        return Locale.getISOCountries().map { countryCode ->
            getCountryDisplayName(countryCode)
        }.sorted()
    }

    /**
     * Extract language code from formatted display name
     */
    fun extractLanguageCode(displayName: String): String {
        return if (displayName.contains("(") && displayName.contains(")")) {
            displayName.substringAfter("(").substringBefore(")")
        } else {
            displayName.lowercase()
        }
    }

    /**
     * Extract country code from formatted display name
     */
    fun extractCountryCode(displayName: String): String {
        return if (displayName.contains("(") && displayName.contains(")")) {
            displayName.substringAfter("(").substringBefore(")")
        } else {
            displayName.uppercase()
        }
    }
}