package com.diatech.imageannotator.ui.components

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import com.diatech.imageannotator.DrawingStroke
import com.diatech.imageannotator.drawQuadraticBezier

fun getDrawingBitmap(drawable: Drawable, width: Int, height: Int, strokes: List<DrawingStroke>, orgH: Float, orgW: Float): Pair<Bitmap,Bitmap> {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val bmpWithoutImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvasWithDrawable = android.graphics.Canvas(bmp)
    val canvasWithoutDrawable = android.graphics.Canvas(bmpWithoutImage)
    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvasWithDrawable)
    drawAnnotation(canvasWithDrawable,strokes, orgH, orgW)
    drawAnnotation(canvasWithoutDrawable, strokes, orgH, orgW)
    return Pair(bmp,bmpWithoutImage)
}

fun drawAnnotation(canvas: android.graphics.Canvas,strokes: List<DrawingStroke>, orgH: Float, orgW: Float){
    val paint = Paint().apply {
        setARGB(255, 255, 0, 0)
        strokeWidth = 16f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    canvas.apply {
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
}