/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.milleron.harmonyfinder.ui.harmonyfinderhome

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import fr.milleron.harmonyfinder.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.Manifest
import fr.milleron.harmonyfinder.core.RecordingManager

@Composable
fun HarmonyFinderHomeScreen(modifier: Modifier = Modifier) {
        HarmonyFinderHomeScreen()
}

@Composable
internal fun HarmonyFinderHomeScreen(
) {
    val context = LocalContext.current
    val recordingManager = remember { RecordingManager(context) }
    var isRecording by remember { mutableStateOf(false) }

    // Request permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            recordingManager.startRecording()
            isRecording = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(), // Make column take full screen size
        verticalArrangement = Arrangement.Center, // Center elements vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center elements horizontally
    ) {
        // First button
        Button(
            onClick = {
                if (isRecording) {
                    recordingManager.stopRecording()
                    isRecording = false
                } else {
                    if (!recordingManager.hasAudioPermission()) {
                        // Request permission if not granted
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        // Start recording if permission is already granted
                        recordingManager.startRecording()
                        isRecording = true
                    }
                }
            },
            modifier = Modifier.padding(8.dp) // Add some padding around buttons
        ) {
            Text(if (isRecording) "Stop Recording" else "Start Recording")
        }

        // Second button
        Button(
            onClick = { /* Handle button click here */ },
            modifier = Modifier.padding(8.dp) // Add some padding around buttons
        ) {
            Text("Button 2")
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        HarmonyFinderHomeScreen()
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        HarmonyFinderHomeScreen()
    }
}
