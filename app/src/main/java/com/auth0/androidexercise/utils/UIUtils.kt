package com.auth0.androidexercise

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF


fun Bitmap.toRoundedCorners(
    cornerRadius: Float = 25F
): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        width + 8, // width in pixels
        height, // height in pixels
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    val path = Path().apply {
        addRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            cornerRadius,
            cornerRadius,
            Path.Direction.CCW
        )
    }
    canvas.clipPath(path)

    // draw the rounded corners bitmap on canvas
    canvas.drawBitmap(this, 0f, 0f, null)
    return bitmap
}