package com.matheuscastro.feature.invoice.ui.save.model

import android.net.Uri
import com.matheuscastro.core.domain.model.Currency
import com.matheuscastro.core.domain.model.UId

data class UiInvoice(
    val id: UId = UId.invalid(),
    val customerField: CustomerField = "".toCustomerField(),
    val dateField: DateField = "".toDateField(),
    val totalMoneyField: MoneyField = "".toMoneyField(),
    val totalCurrency: String = Currency.EUR.name,
    val photoField: PhotoField = Uri.EMPTY.toPhotoField()
)
