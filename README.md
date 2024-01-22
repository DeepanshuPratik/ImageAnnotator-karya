<div align="center">
<img src='' width='256px' />
</div>

<h1 align="center">ImageAnnotator</h1>

</br>

<p align="center">
  <img alt="API" src="https://img.shields.io/badge/Api%2021+-50f270?logo=android&logoColor=black&style=for-the-badge"/></a>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-a503fc?logo=kotlin&logoColor=white&style=for-the-badge"/></a>
<p/>

<p align="center">A lightweight image annotator library that allows to get Bitmap after annotation</p>

# Gradle

Kotlin: build.gradle.kts
```kotlin
dependencies {
  implementation("com.github.karya-inc:imageannotator:<latest_release>")
}
```

Groovy: build.gradle
```kotlin
dependencies {
  implementation 'com.github.karya-inc:imageannotator:<latest_release>'
}
```

# Usage

Create an instance of ImageAnnotator
```kotlin
var resultBitmap by remember{ mutableStateOf<Bitmap?>(null) }
var resultBitmapWithoutImage by remember{ mutableStateOf<Bitmap?>(null) }
val drawableResourceId = R.drawable.dark_sample  // pass the image you want to get image annotation on
val drawable: Drawable? = ContextCompat.getDrawable(LocalContext.current, drawableResourceId) 
ImageAnnotation(
    image = bitmap,
    wantCircle = false,
    wantPolygon = false,
    wantDisabledDrawing = true,
    wantFreeHand = true,
    polygonResourceId = R.drawable.ic_polygon,
    freeHandResourceId = R.drawable.ic_free_hand,
    disabledDrawingResourceId = R.drawable.no_writing,
    polygonSides = 5,
    onDone = {
        resultBitmap = it.first
        resultBitmapWithoutImage = it.second
    },
    drawable = drawable
)
```

# Sample App
Checkout the sample [App](https://github.com/karya-inc/ImageAnnotator/tree/main/app) for reference

<img src='https://github.com/DeepanshuPratik/ImageAnnotator-karya/blob/main/assets/imageAnnotationWithCanvas.jpeg' alt='sample2' width='256'/>
<img src='https://github.com/DeepanshuPratik/ImageAnnotator-karya/blob/main/assets/imageAnnotationWithoutBackground.jpeg' alt='sample1' width='256'/>
<img src='https://github.com/DeepanshuPratik/ImageAnnotator-karya/blob/main/assets/imageAnnotationWithBackground.jpeg' alt='sample1' width='256'/>

