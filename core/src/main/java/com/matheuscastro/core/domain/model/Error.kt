package com.matheuscastro.core.domain.model

abstract class Error(
    val cause: Throwable?
)

object InvalidIdError : Error(null)