/**
 * Copyright (c) 2024 DAIA Pvt Ltd
 * Author : Deepanshu Pratik <deepanshu.pratik@gmail.com>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.diatech.imageannotator

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.diatech.imageannotator.color.colors
import com.diatech.imageannotator.drawutils.DrawMode
import com.diatech.imageannotator.ui.components.FreeHandCanvas
import com.diatech.imageannotator.ui.screens.DisplayCanvas
import com.diatech.imageannotator.helper.getDrawingBitmap
import com.diatech.imageannotator.ui.components.ActionsBar
import com.diatech.imageannotator.ui.components.ColorPicker
import com.diatech.imageannotator.ui.components.ResponsePairButton
import com.diatech.imageannotator.ui.components.rememberDrawing

@Composable
fun ImageAnnotation(
    image: Bitmap,
    enableFreeHand: Boolean,
    freeHandResourceId: Int,
    enableCircle: Boolean,
    enablePolygon: Boolean,
    polygonResourceId: Int,
    enableDisabledDrawing: Boolean,
    disabledDrawingResourceId: Int,
    polygonSides: Int,
    onDone: (Pair<ImageAnnotator, Bitmap>) -> Unit
) {
    val drawing = rememberDrawing()
    var boxHeightPx by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableFloatStateOf(boxHeightPx) }
    var scale by remember { mutableFloatStateOf(1f) }
    var zoomOffset by remember { mutableStateOf(Offset.Zero) }
    val viewHeight by drawing.originalHeight.collectAsState()
    val viewWidth by drawing.originalWidth.collectAsState()
    val toggleColorPicker: () -> Unit = {
        offset = if (offset == 0f) {
            boxHeightPx
        } else {
            0f
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(image.width.toFloat() / image.height.toFloat())
                .graphicsLayer { clip = true }
        ) {
            val state = rememberTransformableState { zoomChange, panChange, _ ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)

                val extraWidth = (scale - 1) * constraints.maxWidth
                val extraHeight = (scale - 1) * constraints.maxHeight

                val maxX = extraWidth / 2
                val maxY = extraHeight / 2

                zoomOffset = Offset(
                    x = (zoomOffset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                    y = (zoomOffset.y + scale * panChange.y).coerceIn(-maxY, maxY)
                )
            }
            Image(
                bitmap = image.asImageBitmap(),
                modifier = Modifier
                    .onGloballyPositioned {
                        drawing.updateOriginalDimensions(
                            height = it.size.height.toFloat(),
                            width = it.size.width.toFloat()
                        )
                    }
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = zoomOffset.x
                        translationY = zoomOffset.y
                    }
                    .matchParentSize()
                    .transformable(state),
                contentDescription = null
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
                DisplayCanvas(
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Transparent,
                    RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .align(Alignment.CenterHorizontally)
        ) {
            ActionsBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                drawing = drawing,
                enableFreeHand = enableFreeHand,
                enablePolygon = enablePolygon,
                enableDisabledDrawing = enableDisabledDrawing,
                enableCircle = enableCircle,
                polygonResourceId = polygonResourceId,
                freeHandResourceId = freeHandResourceId,
                disabledDrawingResourceId = disabledDrawingResourceId,
                polygonSides = polygonSides,
                toggleColorPicker = toggleColorPicker
            )
            if(offset == boxHeightPx){
                Box(
                    Modifier.onGloballyPositioned { boxHeightPx = it.size.height.toFloat() }
                        .align(Alignment.CenterHorizontally)
                ) {
                    ColorPicker(
                        colors = colors,
                        selectedColor = drawing.color,
                        onColorPicked = {
                            drawing.setColor(it)
                            toggleColorPicker()
                        }
                    )
                }
            }
            ResponsePairButton(
                positiveEnabled = true,
                positiveResponse = "Submit",
                negativeEnabled = true,
                negativeResponse = "Clear All",
                onNegative = { drawing.clear() },
                onPositive = {
                    val bmp = getDrawingBitmap(
                        image.width,
                        image.height,
                        drawing.strokes,
                        viewHeight,
                        viewWidth
                    )
                    onDone(Pair(drawing, bmp))
                }
            )
        }
    }
}

// TODO() : helper functions
fun overlayBitmaps(baseBitmap: Bitmap, overlayBitmap: Bitmap): Bitmap {
    val resultBitmap = Bitmap.createBitmap(baseBitmap.width, baseBitmap.height, baseBitmap.config)
    val canvas = Canvas(resultBitmap)

    // Draw the base bitmap
    canvas.drawBitmap(baseBitmap, 0f, 0f, null)

    val scaledOverlayBitmap = Bitmap.createScaledBitmap(overlayBitmap, baseBitmap.width, baseBitmap.height, true)
    // Draw the overlay bitmap on top
    canvas.drawBitmap(scaledOverlayBitmap, 0f, 0f, null)

    return resultBitmap
}