package com.vtplib.imageloader.transform

import android.graphics.*
import androidx.core.graphics.toRectF

class RoundedCornerTransformation(
    private val cornerRadius: Float = 20f
) : Transformation {
    override suspend fun transform(input: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(input.width, input.height, input.config)
        val canvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
        }

        val rect = Rect(0, 0, input.width, input.height)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawRoundRect(rect.toRectF(), cornerRadius, cornerRadius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(input, rect, rect, paint)
        return output
    }
}
