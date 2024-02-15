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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

fun Path.drawQuadraticBezier(points: List<Offset>) {
    if (points.size <= 1) return // need atLeast two points to draw path
    moveTo(points[0].x, points[0].y) // move the cursor from (0,0) to x0, y0
    var prevPoint = points[1]
    points.forEachIndexed { idx, point ->
        if (idx == 0) return@forEachIndexed
        // set middle as control point
        val controlPoint = Offset((prevPoint.x + point.x) / 2, (prevPoint.y + point.y) / 2)
        // draw a bezier curve from `prevPoint` to `point` through `controlPoint`
        quadraticBezierTo(controlPoint.x, controlPoint.y, point.x, point.y)
        prevPoint = point
    }
}

fun android.graphics.Path.drawQuadraticBezier(points: List<Offset>, scaleX: Float, scaleY: Float) {
    if (points.size <= 1) return // need atLeast two points to draw path
    moveTo(points[0].x * scaleX, points[0].y * scaleY) // move the cursor from (0,0) to x0, y0
    var prevPoint = points[1]
    points.forEachIndexed { idx, point ->
        if (idx == 0) return@forEachIndexed
        // set middle as control point
        val controlPoint = Offset((prevPoint.x + point.x) / 2, (prevPoint.y + point.y) / 2)
        // draw a bezier curve from `prevPoint` to `point` through `controlPoint`
        quadTo(controlPoint.x * scaleX, controlPoint.y * scaleY, point.x * scaleX, point.y * scaleY)
        prevPoint = point
    }
}