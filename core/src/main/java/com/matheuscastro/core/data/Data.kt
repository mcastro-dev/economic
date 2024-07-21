package com.matheuscastro.core.data

import com.matheuscastro.core.domain.model.Error

sealed class Data<out T> {
    object None : Data<Nothing>()

    object Loading : Data<Nothing>()

    data class Success<out T>(
        val content: T
    ) : Data<T>()

    data class Failure(
        val error: Error
    ) : Data<Nothing>()
}

fun <T, S> Data<T>.mapSuccessContent(contentMapper: (T) -> S): Data<S> =
    when (this) {
        is Data.Success -> Data.Success(contentMapper(this.content))
        else -> this as Data<S>
    }
