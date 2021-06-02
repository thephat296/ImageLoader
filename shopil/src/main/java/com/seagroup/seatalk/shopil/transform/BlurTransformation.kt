package com.seagroup.seatalk.shopil.transform

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class BlurTransformation(
    private val context: Context,
    private val blurRadius: Float = DEFAULT_BLUR_RADIUS
) : Transformation {
    override suspend fun transform(input: Bitmap): Bitmap {
        val output: Bitmap = Bitmap.createBitmap(input)
        val renderScript = RenderScript.create(context)
        val tmpIn = Allocation.createFromBitmap(renderScript, input)
        val tmpOut = Allocation.createFromBitmap(renderScript, output)
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)).apply {
            setRadius(blurRadius)
            setInput(tmpIn)
            forEach(tmpOut)
        }
        tmpOut.copyTo(output)
        return output
    }

    companion object {
        private const val DEFAULT_BLUR_RADIUS = 25f
    }
}
