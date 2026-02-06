package com.mattg.wahuntingregs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mattg.wahuntingregs.ui.theme.WAHuntingRegsTheme
import com.mattg.wahuntingregs.screens.HomeScreen
import com.mattg.wahuntingregs.screens.ResultScreen

private enum class Screen {HOME, RESULTS}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WAHuntingRegsTheme {
                var screen by remember { mutableStateOf(Screen.HOME) }

                var selectedTagType by remember { mutableStateOf("") }
                var selectedSpecies by remember { mutableStateOf("") }
                var selectedGmu by remember { mutableStateOf("") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (screen) {
                        Screen.HOME -> {
                            HomeScreen(
                                modifier = Modifier.padding(innerPadding),
                                onFindRegs = { tagType, species, gmu ->
                                    selectedTagType = tagType
                                    selectedSpecies = species
                                    selectedGmu = gmu
                                    screen = Screen.RESULTS
                                }
                            )
                        }

                        Screen.RESULTS -> {
                            ResultScreen(
                                modifier = Modifier.padding(innerPadding),
                                tagType = selectedTagType,
                                species = selectedSpecies,
                                gmu = selectedGmu,
                                onBack = { screen = Screen.HOME }
                            )
                        }
                    }
                }

            }

        }
    }
}