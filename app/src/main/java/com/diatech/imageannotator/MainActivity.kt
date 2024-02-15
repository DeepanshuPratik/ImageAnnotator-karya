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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorSpace
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.diatech.imageannotator.helper.getBitmapFromOffsets
import com.diatech.imageannotator.helper.saveBitmapToGallery
import com.diatech.imageannotator.ui.theme.ImageAnnotatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContent {
            ImageAnnotatorTheme {
                val bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sample5)
                val resultBitmap by remember{
                    mutableStateOf<Bitmap?>(null)
                }
                val context = LocalContext.current
                var drawing : ImageAnnotator? = null
                var resultBitmapWithoutImage by remember{
                    mutableStateOf<Bitmap?>(null)
                }
                val drawableResourceId = R.drawable.sample5
                val drawable: Drawable? = ContextCompat.getDrawable(LocalContext.current, drawableResourceId)
                Column (
                    modifier = Modifier
                        .fillMaxHeight(),
//                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if (drawable != null) {
                        ImageAnnotation(
                            image = bitmap,
                            enableCircle = false,
                            enablePolygon = false,
                            enableDisabledDrawing = true,
                            enableFreeHand = true,
                            polygonResourceId = R.drawable.ic_polygon,
                            freeHandResourceId = R.drawable.ic_free_hand,
                            disabledDrawingResourceId = R.drawable.ic_baseline_pinch,
                            polygonSides = 5,
                            onDone = {
                                resultBitmapWithoutImage = it.second
                                drawing = it.first
                                saveBitmapToGallery(context, resultBitmapWithoutImage!!,"SampleImage","image/png" )
//                                saveBitmapToGallery(context, resultBitmap!!,"SampleImageWithBackground","image/png" )
//                                val overlayImage = overlayBitmaps(bitmap, resultBitmapWithoutImage!!)
//                                saveBitmapToGallery(context, overlayImage,"newBitmap","image/png" )
                                /**
                                *  Creating Image through offsets
                                *
                                **/
                                val offsets = drawing!!.getFreeHandOffset()
                                val tempBitmap = getBitmapFromOffsets(
                                    orgH = offsets.second.first,
                                    orgW = offsets.second.second,
                                    drawingStrokes = offsets.first
                                )
                                val overlayImageOffset = overlayBitmaps(bitmap, tempBitmap)
                                saveBitmapToGallery(context, overlayImageOffset, "CreatedImageFromOffset","image/png")
                            },
                        )
                    }

                    Row(modifier = Modifier.horizontalScroll(ScrollState(0),true)) {
                        resultBitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                modifier = Modifier
                                    .border(2.dp, Color.Red)
                                    .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat()),
                                contentDescription = null
                            )
                        }
                        resultBitmapWithoutImage?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                modifier = Modifier
                                    .border(2.dp, Color.Red)
                                    .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat()),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}
