package com.matheuscastro.feature.invoice.ui.save.model

import com.matheuscastro.core.ui.model.Field
import java.time.LocalDate
import java.time.format.DateTimeParseException

class DateField(
    date: String,
    validationResult: ValidationResult?
) : Field<String>(date, validationResult) {

    override fun validate(): DateField {
        val datePattern = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        return DateField(
            date = content,
            validationResult = if (content.matches(datePattern) && isDateParseSuccessful()) {
                ValidationResult.VALID
            } else {
                ValidationResult.INVALID
            }
        )
    }

    private fun isDateParseSuccessful(): Boolean =
        try {
            LocalDate.parse(content)
            true
        } catch (e: DateTimeParseException) {
            false
        }
}

fun String.toDateField(): DateField =
    DateField(date = this, validationResult = null)

fun LocalDate.toDateField(): DateField =
    this.toString().toDateField()