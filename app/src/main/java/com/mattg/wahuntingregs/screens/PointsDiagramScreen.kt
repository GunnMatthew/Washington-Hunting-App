package com.mattg.wahuntingregs.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

import com.mattg.wahuntingregs.components.DropDownField
import com.mattg.wahuntingregs.components.AssetImage

@Composable
fun PointsDiagramScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val options = listOf("Deer", "Elk")
    var selected by remember { mutableStateOf("")}

    Column(
        modifier = modifier .fillMaxSize() .padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Point Diagrams", style = MaterialTheme.typography.headlineMedium)

        DropDownField(
            label = "Select Animal Diagram",
            options = options,
            selected = selected,
            onSelected = { selected = it }
        )

        // Animal -> Image
        if(selected.isNotBlank()) {
            val imageName = when (selected) {
                "Deer" -> "deerPoints.png"
                "Elk" -> "elkPoints.png"
                else -> ""
            }

            AssetImage(imageName)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBack,
            modifier = Modifier .fillMaxWidth() .height(56.dp)
        ) {
            Text("Home")
        }
    }
}