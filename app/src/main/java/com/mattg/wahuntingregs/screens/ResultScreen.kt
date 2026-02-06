package com.mattg.wahuntingregs.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
        modifier: Modifier = Modifier,
        tagType: String,
        species: String,
        gmu: String,
        onBack: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // "Title"
        Text("Results", textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.fillMaxWidth())

        // Card for summary of selected information
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tag Type: $tagType")
                Text("Species: $species")
                Text("GMU: $gmu")
            }
        }

        // Card for actual data report
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Hunt Dates:")
                Text("No data yet.", fontStyle = FontStyle.Italic) // Replace this with data display for hunt dates
                Spacer(modifier = Modifier.height(8.dp))
                Text("Restrictions:")
                Text("No data yet.", fontStyle = FontStyle.Italic) // Replace this with data display for restrictions (3 pt min, any deer, cougar closed, etc)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Special Information:")
                Text("No data yet.", fontStyle = FontStyle.Italic) // Replace this with extra info, cougar hotline, etc as applicable
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Button to go back home
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Home")
        }
    }

}