package com.seagroup.seatalk.shopil.decoder

interface Decoder {
    suspend fun decode(params: DecodeParams): DecodeResult
}
