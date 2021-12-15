package com.vtplib.imageloader

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

    inline fun doOnSuccess(onSuccess: (data: R) -> Unit): Result<R> {
        if (this is Success) {
            onSuccess(data)
        }
        return this
    }

    inline fun doOnError(onError: (Throwable) -> Unit): Result<R> {
        if (this is Error) {
            onError(throwable)
        }
        return this
    }
}

internal val <T> Result<T>.data
    get() = (this as? Result.Success)?.data
