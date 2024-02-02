package com.diatech.imageannotator.helper

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import com.diatech.imageannotator.color.errorColor
import com.diatech.imageannotator.color.onErrorColor
import com.diatech.imageannotator.color.onPrimary
import com.diatech.imageannotator.color.primary

val errorButtonColors
    @Composable get() = ButtonDefaults.buttonColors(
        containerColor = errorColor,
        disabledContainerColor = errorColor.copy(0.4f),
        contentColor = onErrorColor,
        disabledContentColor = onErrorColor.copy(0.4f)
    )

val primaryButtonColors
    @Composable get() = ButtonDefaults.buttonColors(
        containerColor = primary,
        contentColor = onPrimary,
        disabledContainerColor = primary.copy(0.4f),
        disabledContentColor = onPrimary.copy(0.4f)
    )