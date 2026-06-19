package com.mattg.wahuntingregs.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.mattg.wahuntingregs.data.HuntingSchema
import com.mattg.wahuntingregs.components.DropDownField
import com.mattg.wahuntingregs.utils.loadHuntingSchema

// Helper functions
fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

// Actual Homescreen
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFindRegs: (tagType: String, species: String, gmu: String) -> Unit,
    onPointDiagrams: () -> Unit,
    onIdentification: () -> Unit
    ) {
    // **Updated for JSON
    val context = LocalContext.current
    /* val schema = remember { loadHuntingSchema(context) } */   // Temporarily removed due to JSON error.
    val schema = remember {
        try {
            loadHuntingSchema(context)
        } catch (e: Exception) {
            e.printStackTrace()
            HuntingSchema(
                tagTypes = emptyList(),
                speciesTypes = emptyList(),
                gmus = emptyList()
            )
        }
    }
    val tagTypes = schema.tagTypes
    val speciesTypes = schema.speciesTypes
    val gmus = schema.gmus.map { "${it.number} - ${it.name}" }
    var selectedTagType by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("") }
    var selectedGmu by remember { mutableStateOf("") }

    val lintTest = "Lint Test"

    // Build UI
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "Title"
        Text (
            text = "Washington Hunting Regulations",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Instruction
        Text (text = "Select criteria from below")

        // Create dropdown menus
        DropDownField(label = "Tag Type", options = tagTypes, selected = selectedTagType, onSelected = {selectedTagType = it}, searchable = false)
        DropDownField(label = "Species Type", options = speciesTypes, selected = selectedSpecies, onSelected = {selectedSpecies = it}, searchable = false)
        DropDownField(label = "GMU", options = gmus, selected = selectedGmu, onSelected = {selectedGmu = it}, searchable = true)

        Spacer(modifier = Modifier.height(8.dp))

        // Create button to go back to Screen.RESULTS
        Button( onClick = {
            onFindRegs(selectedTagType, selectedSpecies, selectedGmu)
        },
            modifier = Modifier .fillMaxWidth().height(56.dp),
            enabled = selectedTagType.isNotBlank() && selectedSpecies.isNotBlank() && selectedGmu.isNotBlank()
        ) {
            Text("Find Regs!")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Create button for identification page
        Button(
            onClick = onIdentification,
            modifier = Modifier .fillMaxWidth().height(56.dp)
        ) {
            Text("Identification")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Create button for Point Diagram page
        Button(
            onClick = onPointDiagrams,
            modifier = Modifier .fillMaxWidth().height(56.dp)
        ) {
            Text("Points Diagrams")
        }

        Text (text = "***Regulations updated for 2026 Season***", fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(1f))

        // Updater Button
        Button(
            onClick = {
                // Check JSON from github, compare metadata, if newer, update json, else do nothing
            }
        ) {
            Text("Check for regulations update")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Link buttons
        Button(
            onClick = {
                openLink(context, "https://wdfw.wa.gov/hunting/regulations")
            }
        ) {
            Text("WDFW Hunting Regulations")
        }
    }
}