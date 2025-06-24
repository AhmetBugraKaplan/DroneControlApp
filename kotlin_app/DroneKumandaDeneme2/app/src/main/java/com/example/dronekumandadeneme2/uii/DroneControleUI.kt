package com.example.dronekumandadeneme2.uii

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sendCommandToPython
import sendXYZtoPython
import kotlin.math.roundToInt

@Composable
fun DroneControllerUI() {
    var throttleValue by remember { mutableStateOf(0f) }
    var pitchValue by remember { mutableStateOf(0f) }
    var rollValue by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ÜSTTE SLIDERLAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Throttle (SOLA YAPIŞIK, DİKEY)
            VerticalSliderControl(
                label = "Throttle",
                value = throttleValue,
                onValueChange = { newThrottle ->
                    throttleValue = newThrottle
                    sendXYZtoPython(rollValue, throttleValue, pitchValue)
                },
                onValueChangeFinished = {},
                valueRange = 0f..1f,
                modifier = Modifier.height(250.dp)
            )
            // Pitch (ORTADA, DİKEY)
            VerticalSliderControl(
                label = "Pitch",
                value = pitchValue,
                onValueChange = { newPitch ->
                    pitchValue = newPitch
                    sendXYZtoPython(rollValue, throttleValue, pitchValue)
                },
                onValueChangeFinished = { pitchValue = 0f; sendXYZtoPython(rollValue, throttleValue, 0f) },
                valueRange = -1f..1f
            )
            // Roll (SAĞDA, YATAY)
            HorizontalSliderControl(
                label = "Roll",
                value = rollValue,
                onValueChange = { newRoll ->
                    rollValue = newRoll
                    sendXYZtoPython(rollValue, throttleValue, pitchValue)
                },
                onValueChangeFinished = { rollValue = 0f; sendXYZtoPython(0f, throttleValue, pitchValue) }
            )
        }

        // ALTA YAPIŞIK BUTONLAR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. Satır: Arm/Disarm
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { sendCommandToPython("arm") }) {
                    Text("Arm")
                }
                Button(onClick = { sendCommandToPython("disarm") }) {
                    Text("Disarm")
                }
            }
            // 2. Satır: Mod butonları
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { sendCommandToPython("loiter") }) {
                    Text("Loiter")
                }
                Button(onClick = { sendCommandToPython("stabilize") }) {
                    Text("Stabilize")
                }
                Button(onClick = { sendCommandToPython("land") }) {
                    Text("Land")
                }
            }
        }
    }
}

// Yatay ROLL Slider
@Composable
fun HorizontalSliderControl(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$label: ${(value * 100).roundToInt()}%",
            color = Color.White,
            fontSize = 18.sp
        )
        Slider(
            value = value,
            onValueChange = { onValueChange(it) },
            onValueChangeFinished = onValueChangeFinished,
            valueRange = -1f..1f,
            modifier = Modifier
                .width(280.dp)
                .padding(8.dp)
        )
    }
}

// Dikey PITCH veya THROTTLE Slider
@Composable
fun VerticalSliderControl(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$label: ${(value * 100).roundToInt()}%",
            color = Color.White,
            fontSize = 18.sp
        )
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(36.dp)
                .rotate(-90f)
        ) {
            Slider(
                value = value,
                onValueChange = { onValueChange(it) },
                onValueChangeFinished = onValueChangeFinished,
                valueRange = valueRange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
