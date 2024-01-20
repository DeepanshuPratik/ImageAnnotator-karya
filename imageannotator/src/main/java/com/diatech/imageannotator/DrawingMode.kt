package com.diatech.imageannotator

sealed class DrawMode {
    object FREE_HAND : DrawMode()
    object CIRCLE : DrawMode()
    class POLYGON(val sides: Int) : DrawMode()
    object NONE : DrawMode()
}