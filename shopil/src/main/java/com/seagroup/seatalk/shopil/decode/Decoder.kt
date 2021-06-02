package com.seagroup.seatalk.shopil.decode

import android.graphics.drawable.Drawable
import com.seagroup.seatalk.shopil.Result

interface Decoder {
    suspend fun decode(params: DecodeParams): Result<Drawable>
}
