package com.diatech.imageannotator.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.diatech.imageannotator.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThicknessPicker(
    thicknesses: List<Float>,
    selectedThickness: Float,
    onThicknessPicked: (Float) -> Unit
) {
    FlowRow (
        horizontalArrangement = Arrangement.Center
    ){
        thicknesses.forEach { thickness ->
            val border = if (selectedThickness == thickness) Color.Black else Color.Transparent
            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = { onThicknessPicked(thickness) }
            ) {
                Image(
                    modifier = Modifier
                        .border(2.dp, border, CircleShape),
                    painter = when (thickness) {
                        4f -> painterResource(id = R.drawable.scribble_low)
                        8f -> painterResource(id = R.drawable.scribble_mid)
                        16f -> painterResource(id = R.drawable.scribble_high)
                        else -> painterResource(id = R.drawable.scribble_low) // Default image resource
                    },
                    contentDescription = null,
                )
            }
        }
    }
}
