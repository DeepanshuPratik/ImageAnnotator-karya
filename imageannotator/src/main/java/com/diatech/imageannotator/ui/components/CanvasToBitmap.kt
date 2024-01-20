package com.diatech.imageannotator.ui.components

import android.graphics.Bitmap
import android.graphics.Paint
import com.diatech.imageannotator.DrawingStroke
import com.diatech.imageannotator.drawQuadraticBezier

fun getDrawingBitmap(width: Int, height: Int, strokes: List<DrawingStroke>): Bitmap {
    val paint = Paint().apply {
        setARGB(255, 255, 0, 0)
        strokeWidth = 4f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    android.graphics.Canvas(bmp).apply {
        val scaleX = width.toFloat() / 1280f
        val scaleY = height.toFloat() / 959f
        strokes.forEach { stroke ->
            when (stroke) {
                is DrawingStroke.Circle -> {}
                is DrawingStroke.FreeHand -> {
                    val path = android.graphics.Path().apply {
                        drawQuadraticBezier(stroke.points,scaleX,scaleY)
                    }
                    drawPath(path, paint)
                }

                is DrawingStroke.NONE -> {}
                is DrawingStroke.Polygon -> {}
            }
        }
    }
    return bmp
}