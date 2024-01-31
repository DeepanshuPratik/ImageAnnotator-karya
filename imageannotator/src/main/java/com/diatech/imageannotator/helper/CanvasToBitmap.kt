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
package com.diatech.imageannotator.helper

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import androidx.compose.ui.geometry.Offset
import com.diatech.imageannotator.di.DrawingStroke

fun getDrawingBitmap(width: Int, height: Int, strokes: List<DrawingStroke>, orgH: Float, orgW: Float): Bitmap {
    val bmpWithoutImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvasWithoutDrawable = android.graphics.Canvas(bmpWithoutImage)
    drawAnnotation(canvasWithoutDrawable, strokes, orgH, orgW)
    return bmpWithoutImage
}

fun drawAnnotation(canvas: android.graphics.Canvas, strokes: List<DrawingStroke>, orgH: Float, orgW: Float) {
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
                        drawQuadraticBezier(stroke.points, scaleX, scaleY)
                    }
                    drawPath(path, paint)
                }

                is DrawingStroke.NONE -> {}
                is DrawingStroke.Polygon -> {
                    val path = Path().apply {
                        drawQuadraticBezier(stroke.points, scaleX, scaleY)
                    }
                    drawPath(path, paint)
                }
            }
        }
    }
}