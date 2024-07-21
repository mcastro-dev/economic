package com.matheuscastro.core.ui.model

abstract class Field<T>(
    open val content: T,
    open val validationResult: ValidationResult?
) {

    abstract fun validate(): Field<T>

    enum class ValidationResult {
        VALID,
        INVALID
    }
}