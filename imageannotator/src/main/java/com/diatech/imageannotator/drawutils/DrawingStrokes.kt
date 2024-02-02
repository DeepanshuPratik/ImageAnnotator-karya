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
package com.diatech.imageannotator.drawutils

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.sqrt

sealed class DrawingStroke(
    val color: Color?,
    val width: Float?,
    val alpha: Float?
) {
    class FreeHand(
        val points: SnapshotStateList<Offset>,
        color: Color,
        width: Float,
        alpha: Float
    ) : DrawingStroke(color, width, alpha)

    open class Polygon(
        val points: MutableList<Offset>,
        color: Color = Color.Green,
        width: Float,
        alpha: Float
    ) : DrawingStroke(color, width, alpha) {
        init {
            points.add(points[0]) // add the first point at last
        }
    }
    class NONE : DrawingStroke(null,null,null)

    class Circle(
        val poc1: Offset, // point on circumference 1
        val poc2: Offset, // point on circumference 2
        color: Color = Color.Green,
        width: Float,
        alpha: Float
    ) : DrawingStroke(color, width, alpha) {
        val radius get() = calculateDistance(poc1, poc2) / 2 // diameter/2
        val center
            get() = calculateMidPoint(
                poc1,
                poc2
            ) // center is the mid of line joining two opposite points on circumference
    }
}

// d = sqrt((y2-y2)^2 + (x2-x2)^2)
fun calculateDistance(offset1: Offset, offset2: Offset) = sqrt(
    (offset1.y - offset2.y).toDouble().pow(2.0) +
            (offset2.x - offset1.x).toDouble().pow(2.0)
).toFloat()

//
fun calculateMidPoint(offset1: Offset, offset2: Offset) = Offset(
    x = (offset1.x + offset2.x) / 2,
    y = (offset1.y + offset2.y) / 2
)

// get top left coordinates of a square/rect from two end-points of diagonal
fun getTopLeft(d1: Offset, d2: Offset): Offset {
    val x1 = d1.x
    val x2 = d2.x
    val y1 = d1.y
    val y2 = d2.y
    return if (y2 > y1) {
        if (x2 > x1) {
            Offset(x1, y1)

        } else {
            Offset(x2, y1)
        }
    } else {
        if (x2 > x1) {
            Offset(x1, y2)
        } else {
            Offset(x2, y2)
        }
    }
}

fun getTopLeftAndBottomRight(d1: Offset, d2: Offset): Pair<Offset, Offset> {
    val x1 = d1.x
    val x2 = d2.x
    val y1 = d1.y
    val y2 = d2.y
    return if (y2 > y1) {
        if (x2 > x1) {
            Pair(Offset(x1, y1), Offset(x2, y2))
        } else {
            Pair(Offset(x2, y1), Offset(x1, y2))
        }
    } else {
        if (x2 > x1) {
            Pair(Offset(x1, y2), Offset(x2, y1))
        } else {
            Pair(Offset(x2, y2), Offset(x1, y1))
        }
    }
}

fun getVertices(radius: Float, center: Offset, sides: Int): MutableList<Offset> {
    val vertices = mutableListOf<Offset>()
    val x = center.x
    val y = center.y
    for (i in 0 until sides) {
        val x1 = (x + radius * Math.cos(2 * Math.PI * i / sides)).toFloat()
        val y1 = (y + radius * Math.sin(2 * Math.PI * i / sides)).toFloat()
        vertices.add(Offset(x1, y1))
    }
    return vertices
}