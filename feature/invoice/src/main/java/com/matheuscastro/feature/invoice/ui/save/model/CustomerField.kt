package com.matheuscastro.feature.invoice.ui.save.model

import com.matheuscastro.core.ui.model.Field

class CustomerField(
    customer: String,
    validationResult: ValidationResult?
) : Field<String>(customer, validationResult) {

    override fun validate(): CustomerField =
        CustomerField(
            customer = content,
            validationResult = if (content.isNotBlank()) {
                ValidationResult.VALID
            } else {
                ValidationResult.INVALID
            }
        )
}

fun String.toCustomerField(): CustomerField =
    CustomerField(customer = this, validationResult = null)