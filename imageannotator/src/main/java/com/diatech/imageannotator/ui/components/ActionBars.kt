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
package com.diatech.imageannotator.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.diatech.imageannotator.ImageAnnotator
import com.diatech.imageannotator.R
import com.diatech.imageannotator.drawutils.DrawMode

@Composable
fun DrawModeSelector(
    modifier: Modifier,
    selected: DrawMode,
    polygonSides: Int = 5,
    onSelect: (DrawMode) -> Unit,
    enableFreeHand: Boolean = true,
    freeHandResourceId: Int,
    enableCircle: Boolean = false,
    enablePolygon: Boolean = false,
    polygonResourceId: Int = 0,
    enableDisabledDrawing: Boolean = true,
    disabledDrawingResourceId: Int,
    toggleColorPicker: ()->Unit,
    drawing: ImageAnnotator
) {
    Row(
        modifier
            .clip(CircleShape)
            .background(Color(0XFFD4EAE5), CircleShape)
            .horizontalScroll(rememberScrollState())
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (enableFreeHand) {
            DrawModeButton(drawMode = DrawMode.FREE_HAND, selected = selected, onSelect = onSelect) {
                Icon(
                    painter = painterResource(id = freeHandResourceId),
                    contentDescription = "free hand drawing",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        if (enableCircle) {
            DrawModeButton(drawMode = DrawMode.CIRCLE, selected = selected, onSelect = onSelect) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                )
            }
        }
        if (enablePolygon) {
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
        if (enableDisabledDrawing) {
            DrawModeButton(drawMode = DrawMode.NONE, selected = selected, onSelect = onSelect) {
                Icon(
                    painter = painterResource(id = disabledDrawingResourceId),
                    contentDescription = "no-writing zoom mode",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        IconButton(onClick = toggleColorPicker) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(drawing.color, CircleShape)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun ActionsBar(
    modifier: Modifier,
    drawing: ImageAnnotator,
    enableFreeHand: Boolean,
    freeHandResourceId: Int,
    enableCircle: Boolean,
    enablePolygon: Boolean,
    polygonResourceId: Int,
    enableDisabledDrawing: Boolean,
    disabledDrawingResourceId: Int,
    polygonSides: Int,
    toggleColorPicker: ()->Unit
) {
    Row(
        modifier
            .background(Color(0XFFF6FEFF)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DrawModeSelector(
            modifier = Modifier,
//                .weight(1f),
            selected = drawing.drawMode.value,
            onSelect = { drawing.setDrawMode(it) },
            polygonSides = polygonSides,
            enableCircle = enableCircle,
            enableDisabledDrawing = enableDisabledDrawing,
            enableFreeHand = enableFreeHand,
            enablePolygon = enablePolygon,
            freeHandResourceId = freeHandResourceId,
            polygonResourceId = polygonResourceId,
            disabledDrawingResourceId = disabledDrawingResourceId,
            toggleColorPicker = toggleColorPicker,
            drawing = drawing
        )
        UtilityBar(drawing)
    }
}

@Composable
fun UtilityBar(
    drawing: ImageAnnotator
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { drawing.undo() }) {
            Image(
                painter = painterResource(id = R.drawable.undo),
                contentDescription = "Redo"
            )
        }
        IconButton(onClick = { drawing.redo() }) {
            Image(
                painter = painterResource(id = R.drawable.redo),
                contentDescription = "Redo"
            )
        }
//        IconButton(onClick = { drawing.clear() }) {
//            Image(
//                modifier = Modifier.size(36.dp),
//                painter = painterResource(id = R.drawable.ic_delete_all),
//                contentDescription = "Clear"
//            )
//        }
    }
}