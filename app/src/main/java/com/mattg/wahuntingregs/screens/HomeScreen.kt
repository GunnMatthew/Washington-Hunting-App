package com.mattg.wahuntingregs.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFindRegs: (tagType: String, species: String, gmu: String) -> Unit
    ) {
    // Temporary lists, replace with proper data later.  This is just to play with/render data
    val tagTypes = listOf("Archery", "Modern Firearm", "Muzzleloader")
    val speciesTypes = listOf("Bear", "Cougar", "Deer", "Elk", "Grouse", "Turkey", "Migratory Bird")
    val gmus = listOf("GMU 1", "GMU 2", "GMU 3")

    var selectedTagType by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("") }
    var selectedGmu by remember { mutableStateOf("") }

    Column (
        modifier = modifier .fillMaxSize() .padding(20.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
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

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
        OutlinedTextField(value = selected, onValueChange = {}, readOnly = true, label = { Text(label) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)}, modifier = Modifier .fillMaxWidth() .menuAnchor())

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
            options.forEach { option ->
                DropdownMenuItem( text = { Text(option) }, onClick = {
                    onSelected(option)
                    expanded = false
                })
            }
        }
    }
}