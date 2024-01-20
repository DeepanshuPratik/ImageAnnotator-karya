package com.diatech.imageannotator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.diatech.imageannotator.DrawMode
import com.diatech.imageannotator.DrawingStroke
import com.diatech.imageannotator.ImageAnnotator
import com.diatech.imageannotator.drawQuadraticBezier

@Composable
fun FreeHandCanvas(
    modifier: Modifier,
    drawing: ImageAnnotator
) {
    Canvas(
        modifier = modifier
            .pointerInput(null) {
                detectDragGestures(
                    onDragStart = {
                        drawing.startDrawing(it)
                    },
                    onDrag = { change, _ ->
                        drawing.updateDrawing(change.position)
                    }
                )
            }
    ) {
        drawing.strokes.forEach { stroke ->
            when (stroke) {
                is DrawingStroke.FreeHand -> {
                    drawPath(
                        Path().apply { drawQuadraticBezier(stroke.points) },
                        color = stroke.color!!,
                        alpha = stroke.alpha!!,
                        style = Stroke(
                            width = stroke.width!!,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
                is DrawingStroke.NONE -> {

                }
                is DrawingStroke.Polygon -> {
                    drawPath(
                        Path().apply { drawQuadraticBezier(stroke.points) },
                        color = stroke.color!!,
                        alpha = stroke.alpha!!,
                        style = Stroke(
                            width = stroke.width!!,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
                is DrawingStroke.Circle -> {
                    drawCircle(
                        color = stroke.color!!,
                        radius = stroke.radius,
                        center = stroke.center,
                        style = Stroke(
                            width = stroke.width!!,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun rememberDrawing(
    color: Color = Color.Red,
    width: Float = 4f,
    alpha: Float = 1f,
    drawMode: DrawMode = DrawMode.FREE_HAND
): ImageAnnotator {
    return remember { ImageAnnotator(color, width, alpha, drawMode) }
}
