package com.matheuscastro.feature.invoice.domain.repository

import com.matheuscastro.core.data.Data
import com.matheuscastro.feature.invoice.domain.model.InvoiceFilter
import kotlinx.coroutines.flow.Flow

interface InvoiceFilterRepository {
    fun observeInvoiceFilter(): Flow<Data<InvoiceFilter>>
    fun saveInvoiceFilter(filter: InvoiceFilter): Flow<Data<Unit>>
}