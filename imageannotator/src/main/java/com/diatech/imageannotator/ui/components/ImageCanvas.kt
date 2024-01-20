package com.diatech.imageannotator.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.diatech.imageannotator.DrawMode
import com.diatech.imageannotator.ImageAnnotator

@Composable
fun ImageAnnotation(
    image: Bitmap,
    wantFreeHand: Boolean,
    freeHandResourceId: Int,
    wantCircle: Boolean,
    wantPolygon: Boolean,
    polygonResourceId: Int,
    wantDisabledDrawing: Boolean,
    disabledDrawingResourceId: Int,
    polygonSides: Int,
    onDone : (Bitmap) -> Unit
) {
    val drawing = rememberDrawing()
    val boxHeightPx by remember { mutableStateOf(0f) }
    val offset by remember { mutableStateOf(boxHeightPx) }
    val offsetAnim = animateFloatAsState(targetValue = offset)
    var scale by remember { mutableStateOf(1f) }
    var zoomOffset by remember { mutableStateOf(Offset.Zero) }
    var bmp by remember { mutableStateOf<Bitmap?>(null) }
    var height = 0f
    var width = 0f

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(image.width.toFloat() / image.height.toFloat())
                .graphicsLayer { clip = true }
        ) {
            val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)

                val extraWidth = (scale - 1) * constraints.maxWidth
                val extraHeight = (scale - 1) * constraints.maxHeight

                val maxX = extraWidth / 2
                val maxY = extraHeight / 2

                zoomOffset = Offset(
                    x = (zoomOffset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                    y = (zoomOffset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                )
            }
            ShowBitmap(
                bitmap = image,
                modifier = Modifier
                    .onGloballyPositioned {
                        height = it.size.height.toFloat()
                        width = it.size.width.toFloat()
                    }
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = zoomOffset.x
                        translationY = zoomOffset.y
                    }
                    .matchParentSize()
                    .transformable(state)
            )
            if (drawing.drawMode.value != DrawMode.NONE) {
                FreeHandCanvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(image.width.toFloat() / image.height.toFloat())
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = zoomOffset.x
                            translationY = zoomOffset.y
                        }
                        .transformable(state)
                        .matchParentSize(),
                    drawing = drawing
                )
            } else {
                displayCanvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(image.width.toFloat() / image.height.toFloat())
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = zoomOffset.x
                            translationY = zoomOffset.y
                        }
                        .transformable(state),
                    drawing = drawing
                )
            }
        }
        ActionsBar(
            modifier = Modifier
                .align(Alignment.End)
                .graphicsLayer {
                    translationY = offsetAnim.value
                }
                .fillMaxWidth()
                .border(2.dp , Color.Cyan)
                .background(
                    //MaterialTheme.colorScheme.primaryContainer,
                    Color.Transparent,
                    RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                ),
            drawing = drawing,
            wantFreeHand = wantFreeHand,
            wantPolygon = wantPolygon,
            wantDisabledDrawing = wantDisabledDrawing,
            wantCircle = wantCircle,
            polygonResourceId = polygonResourceId,
            freeHandResourceId = freeHandResourceId,
            disabledDrawingResourceId = disabledDrawingResourceId,
            polygonSides = polygonSides,
            onSubmit = {
                bmp = getDrawingBitmap(image.width, image.height, drawing.strokes, height, width)
                bmp?.let { onDone(it) }
            }
        )
    }
}

@Composable
fun ActionsBar(
    modifier: Modifier,
    drawing: ImageAnnotator,
    wantFreeHand: Boolean,
    freeHandResourceId: Int,
    wantCircle: Boolean,
    wantPolygon: Boolean,
    polygonResourceId: Int,
    wantDisabledDrawing: Boolean,
    disabledDrawingResourceId: Int,
    polygonSides: Int,
    onSubmit : () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(Modifier.padding(8.dp)) {
            IconButton(onClick = { drawing.clear() }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear"
                )
            }
            IconButton(onClick = { drawing.undo() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Undo"
                )
            }
            IconButton(onClick = { drawing.redo() }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Redo"
                )
            }
            DrawModeSelector(
                modifier = Modifier
                    .weight(1f),
                selected = drawing.drawMode.value,
                onSelect = { drawing.setDrawMode(it) },
                polygonSides = polygonSides,
                wantCircle = wantCircle,
                wantDisabledDrawing = wantDisabledDrawing,
                wantFreeHand = wantFreeHand,
                wantPolygon = wantPolygon,
                freeHandResourceId = freeHandResourceId,
                polygonResourceId = polygonResourceId,
                disabledDrawingResourceId = disabledDrawingResourceId,
                onSubmit = onSubmit
            )
        }
    }
}