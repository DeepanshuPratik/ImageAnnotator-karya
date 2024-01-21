package com.diatech.imageannotator

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class ImageAnnotator
constructor(
    private var _color: Color,
    private var _width: Float,
    private var _alpha: Float,
    drawMode: DrawMode
){
    private val _undoList = mutableStateListOf<DrawingStroke>()
    val strokes: SnapshotStateList<DrawingStroke> get() = _undoList
    private val _redoList = mutableStateListOf<DrawingStroke>()
    private val _drawMode = mutableStateOf(drawMode)
    val color get() = _color
    val width get() = _width
    val alpha get() = _alpha
    val drawMode: State<DrawMode> get() = _drawMode

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
        if(stroke.width != null && stroke.color!=null && stroke.alpha!=null)
            _undoList.add(stroke)
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