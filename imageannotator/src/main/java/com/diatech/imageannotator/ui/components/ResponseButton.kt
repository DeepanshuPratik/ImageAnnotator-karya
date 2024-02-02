package com.diatech.imageannotator.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.diatech.imageannotator.R
import com.diatech.imageannotator.helper.KButton
import com.diatech.imageannotator.helper.errorButtonColors
import com.diatech.imageannotator.helper.primaryButtonColors

@Composable
fun ResponsePairButton(
    positiveResponse: String,
    onPositive: () -> Unit,
    positiveEnabled: Boolean,
    negativeResponse: String,
    onNegative: () -> Unit,
    negativeEnabled: Boolean,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KButton(
                colors = errorButtonColors,
                modifier = Modifier.weight(1f),
                onClick = {
                    onNegative()
                },
                enabled = negativeEnabled
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete_all),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Text(text = negativeResponse)
            }
            KButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    onPositive()
                },
                enabled = positiveEnabled,
                colors = primaryButtonColors
            ) {
                Text(text = positiveResponse)
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
