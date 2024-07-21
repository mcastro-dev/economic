package com.matheuscastro.feature.invoice.domain.model

import android.net.Uri
import com.matheuscastro.core.domain.model.Money
import com.matheuscastro.core.domain.model.UId
import java.time.LocalDate

data class Invoice(
    val id: UId,
    val customer: String,
    val date: LocalDate,
    val total: Money,
    val photoUri: Uri
)
