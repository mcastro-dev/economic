package com.matheuscastro.feature.invoice.domain.interactor

import com.matheuscastro.core.data.Data
import com.matheuscastro.feature.invoice.domain.model.InvoiceFilter
import com.matheuscastro.feature.invoice.domain.repository.InvoiceFilterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface InvoiceFilterInteractor {
    fun observeInvoiceFilter(): Flow<Data<InvoiceFilter>>
    fun saveInvoiceFilter(filter: InvoiceFilter): Flow<Data<Unit>>
}

internal class InvoiceFilterInteractorImpl @Inject constructor(
    private val repository: InvoiceFilterRepository
) : InvoiceFilterInteractor {

    override fun observeInvoiceFilter(): Flow<Data<InvoiceFilter>> =
        repository.observeInvoiceFilter()

    override fun saveInvoiceFilter(filter: InvoiceFilter): Flow<Data<Unit>> =
        repository.saveInvoiceFilter(filter)
}