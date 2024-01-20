package com.diatech.imageannotator.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.diatech.imageannotator.DrawMode

@Composable
fun ShowBitmap(bitmap: Bitmap, modifier: Modifier) {
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()
    Image(
        bitmap = imageBitmap,
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun DrawModeSelector(
    modifier: Modifier,
    selected: DrawMode,
    polygonSides: Int = 5,
    onSelect: (DrawMode) -> Unit,
    wantFreeHand: Boolean = true,
    freeHandResourceId : Int,
    wantCircle: Boolean = false,
    wantPolygon: Boolean = false,
    polygonResourceId : Int = 0,
    wantDisabledDrawing : Boolean = true,
    disabledDrawingResourceId : Int,
    onSubmit: ()->Unit
) {
    Row(
        modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .horizontalScroll(rememberScrollState())
    ) {
        if(wantFreeHand){
            DrawModeButton(drawMode = DrawMode.FREE_HAND, selected = selected, onSelect = onSelect) {
                Icon(
                    painter = painterResource(id = freeHandResourceId),
                    contentDescription = "free hand drawing",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        if(wantCircle){
            DrawModeButton(drawMode = DrawMode.CIRCLE, selected = selected, onSelect = onSelect) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                )
            }
        }
        if(wantPolygon){
            DrawModeButton(
                drawMode = DrawMode.POLYGON(polygonSides),
                selected = selected,
                onSelect = onSelect
            ) {
                Icon(
                    painter = painterResource(id = polygonResourceId),
                    contentDescription = "free hand drawing",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        if(wantDisabledDrawing){
            DrawModeButton(drawMode = DrawMode.NONE, selected = selected, onSelect = onSelect) {
                Image(
                    painter = painterResource(id = disabledDrawingResourceId),
                    contentDescription = "Non editing Mode",
                )
            }
        }
        IconButton(onClick = onSubmit) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Submit"
            )
        }
    }
}
