package com.diatech.imageannotator

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diatech.imageannotator.ui.components.ImageAnnotation
import com.diatech.imageannotator.ui.components.ShowBitmap
import com.diatech.imageannotator.ui.theme.ImageAnnotatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageAnnotatorTheme {
                val bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.images)
                var resultBitmap by remember{
                    mutableStateOf<Bitmap?>(null)
                }//: Bitmap? = Bitmap.createBitmap(bitmap.width,bitmap.height, Bitmap.Config.ARGB_8888)
                Column (
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ){
                    ImageAnnotation(
                        image = bitmap,
                        wantCircle = true,
                        wantPolygon = true,
                        wantDisabledDrawing = true,
                        wantFreeHand = true,
                        polygonResourceId = R.drawable.ic_polygon,
                        freeHandResourceId = R.drawable.ic_free_hand,
                        disabledDrawingResourceId = R.drawable.no_writing,
                        polygonSides = 5,
                        onDone = {
                            resultBitmap = it
                        }
                    )
                    resultBitmap?.let {
                        ShowBitmap(
                            bitmap = it,
                            modifier = Modifier
                                //.fillMaxWidth()
                                .border(2.dp , Color.Red)
                                .aspectRatio(bitmap.width.toFloat()/bitmap.height.toFloat())
                        )
                    }
                }
            }
        }
    }
}
