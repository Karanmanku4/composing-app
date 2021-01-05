package com.example.composingapp.utils.interfaces

import android.graphics.Color
import android.graphics.RectF

interface Clickable {
    val touchAreaRectF: RectF
    var isClicked: Boolean
    fun updateColour(colour: Int)
}