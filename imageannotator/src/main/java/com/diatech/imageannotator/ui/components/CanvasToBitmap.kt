package com.diatech.imageannotator.ui.components

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import com.diatech.imageannotator.DrawingStroke
import com.diatech.imageannotator.drawQuadraticBezier

fun getDrawingBitmap(width: Int, height: Int, strokes: List<DrawingStroke>, orgH: Float, orgW: Float): Bitmap {
    val paint = Paint().apply {
        setARGB(255, 255, 0, 0)
        strokeWidth = 16f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    android.graphics.Canvas(bmp).apply {
        val scaleX = width.toFloat() / orgW
        val scaleY = height.toFloat() / orgH
        strokes.forEach { stroke ->
            when (stroke) {
                is DrawingStroke.Circle -> {
                    val path = Path().apply {
                        // Add circle to path based on the properties of the Circle stroke
                        addCircle(
                            stroke.center.x * scaleX,
                            stroke.center.y * scaleY,
                            stroke.radius * scaleX,
                            Path.Direction.CW
                        )
                    }
                    drawPath(path, paint)
                }
                is DrawingStroke.FreeHand -> {
                    val path = Path().apply {
                        drawQuadraticBezier(stroke.points,scaleX,scaleY)
                    }
                    drawPath(path, paint)
                }

                is DrawingStroke.NONE -> {}
                is DrawingStroke.Polygon -> {
                    val path = Path().apply {
                        drawQuadraticBezier(stroke.points,scaleX,scaleY)
                    }
                    drawPath(path, paint)
                }
            }
        }
    }
    return bmp
}