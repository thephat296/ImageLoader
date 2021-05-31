package com.seagroup.seatalk.shopil.decoder

import okio.BufferedSource

interface Decoder {
    suspend fun decode(source: BufferedSource): DecodeResult
}
