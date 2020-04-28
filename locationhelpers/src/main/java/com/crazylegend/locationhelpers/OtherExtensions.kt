package com.crazylegend.locationhelpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


/**
 * Created by crazy on 4/28/20 to long live and prosper !
 */


fun Context.getBitmapFromResource(drawableRes: Int): Bitmap? {
    var bitmap: Bitmap? = null
    val drawable = getCompatDrawable(drawableRes)
    val canvas = Canvas()
    drawable?.apply {
        bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        draw(canvas)
    }

    return bitmap
}

fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? =
        ContextCompat.getDrawable(this, drawableRes)
