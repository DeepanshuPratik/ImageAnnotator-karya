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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.diatech.imageannotator.di.DrawMode
import com.diatech.imageannotator.di.DrawingStroke
import com.diatech.imageannotator.di.calculateDistance
import com.diatech.imageannotator.di.calculateMidPoint
import com.diatech.imageannotator.di.getVertices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImageAnnotator
constructor(
    private var _color: Color,
    private var _width: Float,
    private var _alpha: Float,
    drawMode: DrawMode
) {
    private val _undoList = mutableStateListOf<DrawingStroke>()
    val strokes: SnapshotStateList<DrawingStroke> get() = _undoList
    private val _redoList = mutableStateListOf<DrawingStroke>()
    private val _drawMode = mutableStateOf(drawMode)
    val color get() = _color
    val width get() = _width
    val alpha get() = _alpha
    val drawMode: State<DrawMode> get() = _drawMode

    private val _originalHeight = MutableStateFlow(0f)
    private val _originalWidth = MutableStateFlow(0f)
    val originalHeight = _originalHeight.asStateFlow()
    val originalWidth = _originalWidth.asStateFlow()

    fun updateOriginalDimensions(height: Float, width: Float) {
        _originalWidth.update { width }
        _originalHeight.update { height }
    }
    fun startDrawing(offset: Offset) {
        val stroke = when (_drawMode.value) {
            DrawMode.CIRCLE -> DrawingStroke.Circle(offset, offset, color, width, alpha)
            is DrawMode.POLYGON -> {
                val edges = getVertices(0f, offset, (_drawMode.value as DrawMode.POLYGON).sides)
                DrawingStroke.Polygon(
                    edges,
                    _color,
                    _width,
                    _alpha
                )
            }
            is DrawMode.FREE_HAND -> DrawingStroke.FreeHand(
                mutableStateListOf(offset),
                _color,
                _width,
                _alpha
            )
            is DrawMode.NONE -> DrawingStroke.NONE()
        }
        if (stroke.width != null && stroke.color != null && stroke.alpha != null) {
            _undoList.add(stroke)
        }
    }

    fun updateDrawing(offset: Offset) {
        if (_undoList.isEmpty()) return
        when (val lastStroke = _undoList.last()) {
            is DrawingStroke.Circle -> {
                val newCircle = DrawingStroke.Circle(lastStroke.poc1, offset, color, width, alpha)
                _undoList.removeLast()
                _undoList.add(newCircle)
            }
            is DrawingStroke.FreeHand -> {
                lastStroke.points.add(offset)
            }
            is DrawingStroke.NONE -> {
            }
            is DrawingStroke.Polygon -> {
                val p1 = lastStroke.points[0]
                val p2 = offset
                val radius = calculateDistance(p1, p2) / 2
                val center = calculateMidPoint(p1, p2)
                val sides = 5
                val edges = getVertices(radius, center, sides)
                val newPolygon = DrawingStroke.Polygon(edges, color, width, alpha)
                _undoList.removeLast()
                _undoList.add(newPolygon)
            }
        }
    }

    fun undo() {
        if (_undoList.isEmpty()) return
        val removed = _undoList.removeLast()
        _redoList.add(removed)
    }

    fun redo() {
        if (_redoList.isEmpty()) return
        val removed = _redoList.removeLast()
        _undoList.add(removed)
    }

    fun clear() {
        _undoList.clear()
        _redoList.clear()
    }

    fun setColor(color: Color) = run { this._color = color }
    fun setWidth(width: Float) = run { this._width = width }
    fun setAlpha(alpha: Float) = run { this._alpha = alpha }
    fun setDrawMode(drawMode: DrawMode) = run { this._drawMode.value = drawMode }
}
