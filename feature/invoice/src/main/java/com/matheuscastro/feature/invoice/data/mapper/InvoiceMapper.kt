package com.matheuscastro.feature.invoice.data.mapper

import android.net.Uri
import com.matheuscastro.core.domain.model.Currency
import com.matheuscastro.core.domain.model.Money
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.feature.invoice.data.local.model.InvoiceEntity
import com.matheuscastro.feature.invoice.domain.model.Invoice
import java.time.LocalDate

internal fun InvoiceEntity.toDomain(): Invoice =
    Invoice(
        id = this.id?.let { UId(it) } ?: UId.invalid(),
        customer = this.customer,
        date = LocalDate.parse(this.date),
        total = Money(this.total, Currency.EUR),
        photoUri = Uri.parse(this.photoPath)
    )

internal fun Invoice.toEntity(): InvoiceEntity =
    InvoiceEntity(
        id = if (this.id.isInvalid()) {
            null
        } else {
            this.id.value
       },
        customer = this.customer,
        date = this.date.toString(),
        total = this.total.amount,
        totalCurrency = this.total.currency.name,
        photoPath = this.photoUri.toString()
    )