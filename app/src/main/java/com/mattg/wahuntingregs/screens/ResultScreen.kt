package com.mattg.wahuntingregs.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

import com.mattg.wahuntingregs.utils.isHarvestRestriction
import com.mattg.wahuntingregs.utils.loadResultEntry

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
                if (result?.generalSeasonDates?.isNotEmpty() == true) {
                    Text("General Season Dates:")

                    result.generalSeasonDates.forEach { date ->
                        Text("• ${date.dateRange}")
                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Late Season Text
                if (result?.lateGeneralDates?.isNotEmpty() == true) {
                    Text("Late General Dates:")

                    result.lateGeneralDates.forEach { date ->
                        Text("• ${date.dateRange}")
                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Youth Season Text
                if (result?.youthGeneralDates?.isNotEmpty() == true) {
                    Text("Youth General Dates:")

                    result.youthGeneralDates.forEach { date ->
                        Text("• ${date.dateRange}")
                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Special Season Text
                if (result?.specialSeasonDates?.isNotEmpty() == true) {
                    Text("Special Season Dates:")

                    result.specialSeasonDates.forEach { date ->
                        Text("• ${date.dateRange}")
                        if (date.restriction.isNotBlank() && isHarvestRestriction(date.restriction)) {
                            Text("  Type: ${date.restriction}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Restrictions Text
                Text("Restrictions:")
                val amplifyingRestrictions = result?.restrictions
                    ?.filterNot { isHarvestRestriction(it) }
                    ?.distinct()
                    .orEmpty()

                if (amplifyingRestrictions.isNotEmpty()) {
                    Text("Restrictions")
                    amplifyingRestrictions.forEach { Text("• $it") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Special information text
                if (result?.specialInformation?.isNotEmpty() == true) {
                    Text("Special Information:")
                    result.specialInformation.forEach { Text("• $it") }
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