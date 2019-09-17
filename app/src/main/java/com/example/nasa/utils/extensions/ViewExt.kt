package com.example.nasa.utils.extensions

import android.content.res.Resources

fun Int.dpToPx(): Int {
    return (Resources.getSystem().displayMetrics.density * this).toInt()
}

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}
