package com.diatech.imageannotator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(
    colors: List<Color>,
    selectedColor: Color,
    onColorPicked: (Color) -> Unit
) {
    FlowRow (
        horizontalArrangement = Arrangement.Center
    ){
        colors.forEach { color ->
            val border =
                if (selectedColor == color) Color.Black else Color.Transparent
            IconButton(onClick = { onColorPicked(color) }) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color, CircleShape)
                        .border(2.dp, border, CircleShape)
                )
            }
        }
    }
}
