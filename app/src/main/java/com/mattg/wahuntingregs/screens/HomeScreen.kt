package com.mattg.wahuntingregs.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import org.json.JSONObject

// Data classes for JSON
data class Gmu(
    val number: Int,
    val name: String
)

data class HuntingSchema(
    val tagTypes: List<String>,
    val speciesTypes: List<String>,
    val gmus: List<Gmu>
)

// JSON Loader
fun loadHuntingSchema(context: Context): HuntingSchema {
    val jsonString = context.assets.open("HuntingSchema26.json")
        .bufferedReader()
        .use { it.readText() }
    val jsonObject = JSONObject(jsonString)

    // JSON Arrays -> Kotlin lists
    val tagTypesJson = jsonObject.getJSONArray("tagTypes")
    val tagTypes = mutableListOf<String>()

    for (i in 0 until tagTypesJson.length()) {
        tagTypes.add(tagTypesJson.getString(i))
    }

    val speciesJson = jsonObject.getJSONArray("speciesTypes")
    val speciesTypes = mutableListOf<String>()
    for (i in 0 until speciesJson.length()) {
        speciesTypes.add(speciesJson.getString(i))
    }

    val gmusJson = jsonObject.getJSONArray("gmus")
    val gmus = mutableListOf<Gmu>()
    for (i in 0 until gmusJson.length()) {
        val gmuObject = gmusJson.getJSONObject(i)
        gmus.add(
            Gmu(
                number = gmuObject.getInt("number"),
                name = gmuObject.getString("name")
            )
        )
    }

    return HuntingSchema(
        tagTypes = tagTypes,
        speciesTypes = speciesTypes,
        gmus = gmus
    )
}

// Actual Homescreen
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFindRegs: (tagType: String, species: String, gmu: String) -> Unit
    ) {
    // **Updated for JSON
    val context = LocalContext.current
    val schema = remember { loadHuntingSchema(context) }
    val tagTypes = schema.tagTypes
    val speciesTypes = schema.speciesTypes
    val gmus = schema.gmus.map { "${it.number} - ${it.name}" }
    var selectedTagType by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("") }
    var selectedGmu by remember { mutableStateOf("") }

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
        DropDownField(label = "Tag Type", options = tagTypes, selected = selectedTagType, onSelected = {selectedTagType = it})
        DropDownField(label = "Species Type", options = speciesTypes, selected = selectedSpecies, onSelected = {selectedSpecies = it})
        DropDownField(label = "GMU", options = gmus, selected = selectedGmu, onSelected = {selectedGmu = it})

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

        Text (text = "***Regulations updated for 2026 Season***", fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
    }
}

// Build drop downs for easy use later on
@OptIn(ExperimentalMaterial3Api::class) // For ExposedDropdownMenuBox
@Composable
fun DropDownField(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
        OutlinedTextField(value = selected, onValueChange = {}, readOnly = true, label = { Text(label) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)}, modifier = Modifier .fillMaxWidth() .menuAnchor())

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .verticalScroll(scrollState)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}