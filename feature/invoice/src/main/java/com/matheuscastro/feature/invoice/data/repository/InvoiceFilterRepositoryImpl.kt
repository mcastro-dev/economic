package com.matheuscastro.feature.invoice.data.repository

import com.matheuscastro.core.data.Data
import com.matheuscastro.feature.invoice.domain.model.InvoiceFilter
import com.matheuscastro.feature.invoice.domain.repository.InvoiceFilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class InvoiceFilterRepositoryImpl @Inject constructor() : InvoiceFilterRepository {

    override fun observeInvoiceFilter(): Flow<Data<InvoiceFilter>> {
        // TODO mat: use a memory local data source to get the filter
        return flowOf(Data.None)
    }

    override fun saveInvoiceFilter(filter: InvoiceFilter): Flow<Data<Unit>> {
        // TODO mat: use a memory local data source to save the filter
        return flowOf(Data.None)
    }
}