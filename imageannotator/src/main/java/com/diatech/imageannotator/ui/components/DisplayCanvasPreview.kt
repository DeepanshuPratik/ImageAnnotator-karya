package com.diatech.imageannotator.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.diatech.imageannotator.DrawingStroke
import com.diatech.imageannotator.ImageAnnotator
import com.diatech.imageannotator.drawQuadraticBezier

@Composable
fun displayCanvas(modifier: Modifier, drawing: ImageAnnotator){

    Canvas(modifier = modifier
        .pointerInput(null) {}
    ){
        drawing.strokes.forEach {
                stroke->
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
                else -> {}
            }
        }
    }
}