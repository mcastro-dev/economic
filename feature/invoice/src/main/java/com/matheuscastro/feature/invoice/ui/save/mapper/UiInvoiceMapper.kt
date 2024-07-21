package com.matheuscastro.feature.invoice.ui.save.mapper

import android.net.Uri
import com.matheuscastro.core.domain.model.Currency
import com.matheuscastro.core.domain.model.Money
import com.matheuscastro.feature.invoice.ui.save.model.UiInvoice
import com.matheuscastro.feature.invoice.ui.save.model.toCustomerField
import com.matheuscastro.feature.invoice.ui.save.model.toDateField
import com.matheuscastro.feature.invoice.ui.save.model.toMoneyField
import com.matheuscastro.feature.invoice.ui.save.model.toPhotoField
import com.matheuscastro.feature.invoice.domain.model.Invoice
import java.time.LocalDate

internal fun Invoice.toUiModel(): UiInvoice =
    UiInvoice(
        id = this.id,
        customerField = this.customer.toCustomerField(),
        dateField = this.date.toString().toDateField(),
        totalMoneyField = (this.total.amount / 100).toString().toMoneyField(),
        totalCurrency = this.total.currency.name,
        photoField = this.photoUri.toPhotoField()
    )

internal fun UiInvoice.toDomainModel(): Invoice =
    Invoice(
        id = this.id,
        customer = this.customerField.content,
        date = LocalDate.parse(this.dateField.content),
        total = Money(
            amount = this.totalMoneyField.content.toLongOrNull()?.let { it * 100 } ?: 0,
            currency = Currency.valueOf(this.totalCurrency)
        ),
        photoUri = Uri.parse(this.photoField.content)
    )