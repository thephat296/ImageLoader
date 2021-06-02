package com.seagroup.seatalk.shopil

internal sealed class Result<out R> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()

    inline fun <S> map(transform: (data: R) -> S): Result<S> = when (this) {
        is Error -> Error(throwable)
        is Success -> Success(transform(data))
    }

    inline fun <S> flatMap(transform: (data: R) -> Result<S>): Result<S> = when (this) {
        is Error -> Error(throwable)
        is Success -> transform(data)
    }
}
