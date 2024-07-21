package com.matheuscastro.feature.invoice.ui.save.model

import com.matheuscastro.core.ui.model.Field

class MoneyField(
    moneyAmount: String,
    validationResult: ValidationResult?
) : Field<String>(moneyAmount, validationResult) {

    override fun validate(): MoneyField {
        val moneyPattern = Regex("^\\d+$")
        return MoneyField(
            moneyAmount = content,
            validationResult = if (content.isNotBlank() && content.matches(moneyPattern)) {
                ValidationResult.VALID
            } else {
                ValidationResult.INVALID
            }
        )
    }
}

fun String.toMoneyField(): MoneyField =
    MoneyField(moneyAmount = this, validationResult = null)