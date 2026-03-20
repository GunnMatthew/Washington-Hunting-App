package com.mattg.wahuntingregs.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.json.JSONObject

// Data classes using JSON objects
data class SeasonDate(
    val dateRange: String,
    val restriction: String = "",
    val seasonLabel: String = ""
)

data class ResultEntry(
    val generalSeasonDates: List<SeasonDate>,
    val lateGeneralDates: List<SeasonDate>,
    val youthGeneralDates: List<SeasonDate>,
    val specialSeasonDates: List<SeasonDate>,
    val restrictions: List<String>,
    val specialInformation: List<String>
)

// Helper Functions
// GMU By number
fun extractGmuNumber(gmu: String): String {
    return gmu.substringBefore("-").trim()
}

// JSON Array (Strings) to Kotlin List
fun jsonArrayToStringList(array: org.json.JSONArray?): List<String> {
    if (array == null) return emptyList()

    val list = mutableListOf<String>()

    for (i in 0 until array.length()) {
        list.add(array.getString(i))
    }

    return list
}

// JSON Array (Objects) to Kotlin List
fun jsonArrayToSeasonDateList(array: org.json.JSONArray?): List<SeasonDate> {
    if (array == null) return emptyList()

    val list = mutableListOf<SeasonDate>()

    for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        list.add(
            SeasonDate(
                dateRange = obj.optString("dateRange"),
                restriction = obj.optString("restriction"),
                seasonLabel = obj.optString("seasonLabel")
            )
        )
    }

    return list
}

// Data loading
fun loadResultEntry(
    context: Context,
    tagType: String,
    species: String,
    gmu: String
): ResultEntry? {
    val jsonString = context.assets.open("HuntingSchema26.json")
        .bufferedReader()
        .use { it.readText() }
    val jsonObject = JSONObject(jsonString)
    val seasonData = jsonObject.getJSONArray("seasonData")
    val selectedGmuNumber = extractGmuNumber(gmu)

    for (i in 0 until seasonData.length()) {
        val entry = seasonData.getJSONObject(i)

        val entryTagType = entry.getString("tagType")
        val entrySpecies = entry.getString("species")
        val entryGmu = entry.getJSONObject("gmu")
        val entryGmuNumber = entryGmu.get("number").toString()

        if (entryTagType == tagType && entrySpecies == species && entryGmuNumber == selectedGmuNumber) {
            return ResultEntry(
                generalSeasonDates = jsonArrayToSeasonDateList(entry.optJSONArray("generalSeasonDates")),
                lateGeneralDates = jsonArrayToSeasonDateList(entry.optJSONArray("lateGeneralDates")),
                youthGeneralDates = jsonArrayToSeasonDateList(entry.optJSONArray("youthGeneralDates")),
                specialSeasonDates = jsonArrayToSeasonDateList(entry.optJSONArray("specialSeasonDates")),
                restrictions = jsonArrayToStringList(entry.optJSONArray("restrictions")),
                specialInformation = jsonArrayToStringList(entry.optJSONArray("specialInformation"))
            )
        }
    }

    return null
}

// Differentiate between restrictions.  Using to prevent displaying harvest restrictions vs GMU restrictions in incorrect places
fun isHarvestRestriction(text: String): Boolean {
    val value = text.lowercase()

    return value.contains("any deer") ||
            value.contains("any buck") ||
            value.contains("antlerless") ||
            value.contains("3 pt") ||
            value.contains("3-point") ||
            value.contains("4 pt") ||
            value.contains("4-point") ||
            value.contains("spike")
}

// Actual Result Screen
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    tagType: String,
    species: String,
    gmu: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val result = remember(tagType, species, gmu) {
        loadResultEntry(context, tagType, species, gmu)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Results",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth()
        )

        // Data summation
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tag Type: $tagType")
                Text("Species: $species")
                Text("GMU: $gmu")
            }
        }

        // Information card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {

                // General Season Text
                Text("General Season Dates:")
                if (result?.generalSeasonDates?.isNotEmpty() == true) {
                    result.generalSeasonDates.forEach { date ->
                        Text("• ${date.dateRange}")
                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                } else {
                    Text("No data.", fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Late Season Text
                Text("Late General Dates:")
                if (result?.lateGeneralDates?.isNotEmpty() == true) {
                    result.lateGeneralDates.forEach { date ->
                        Text("• ${date.dateRange}")
                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                } else {
                    Text("No data.", fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Youth Season Text
                Text("Youth General Dates:")
                if (result?.youthGeneralDates?.isNotEmpty() == true) {
                    result.youthGeneralDates.forEach { date ->
                        Text("• ${date.dateRange}")
                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                } else {
                    Text("No data.", fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Special Season Text
                Text("Special Season Dates:")
                if (result?.specialSeasonDates?.isNotEmpty() == true) {
                    result.specialSeasonDates.forEach { date ->
                        if (date.seasonLabel.isNotBlank()) {
                            Text("• ${date.seasonLabel}: ${date.dateRange}")
                        } else {
                            Text("• ${date.dateRange}")
                        }

                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                } else {
                    Text("No data.", fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Restrictions Text
                Text("Restrictions:")
                val amplifyingRestrictions = result?.restrictions
                    ?.filterNot { isHarvestRestriction(it) }
                    ?.distinct()
                    .orEmpty()

                if (amplifyingRestrictions.isNotEmpty()) {
                    amplifyingRestrictions.forEach { Text("• $it") }
                } else {
                    Text("No data.", fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Special information text
                Text("Special Information:")
                if (result?.specialInformation?.isNotEmpty() == true) {
                    result.specialInformation.forEach { Text("• $it") }
                } else {
                    Text("No data.", fontStyle = FontStyle.Italic)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Home")
        }
    }
}