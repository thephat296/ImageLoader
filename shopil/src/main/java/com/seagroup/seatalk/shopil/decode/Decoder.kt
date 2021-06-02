package com.seagroup.seatalk.shopil.decode

interface Decoder {
    suspend fun decode(params: DecodeParams): DecodeResult
}
