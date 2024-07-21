package com.matheuscastro.feature.invoice.domain.interactor

import android.net.Uri
import androidx.paging.PagingData
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.InvalidIdError
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.feature.invoice.domain.model.Invoice
import com.matheuscastro.feature.invoice.domain.model.InvoiceNotFoundError
import com.matheuscastro.feature.invoice.domain.repository.InvoiceFilterRepository
import com.matheuscastro.feature.invoice.domain.repository.InvoiceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface InvoiceInteractor {
    fun observeInvoicePages(): Flow<PagingData<Invoice>>
    fun observeInvoice(invoiceId: UId): Flow<Data<Invoice>>
    fun saveInvoice(invoice: Invoice): Flow<Data<Unit>>
    suspend fun prepareInvoicePhotoFile(): Data<Uri>
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class InvoiceInteractorImpl @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceFilterRepository: InvoiceFilterRepository
) : InvoiceInteractor {

    override fun observeInvoicePages(): Flow<PagingData<Invoice>> =
        invoiceFilterRepository
            .observeInvoiceFilter()
            .flatMapLatest { invoiceFilterData ->
                // TODO mat: use the invoice filter
                invoiceRepository.observeInvoicePages(PAGE_SIZE)
            }

    override fun observeInvoice(invoiceId: UId): Flow<Data<Invoice>> =
        if (invoiceId.isInvalid()) {
            flowOf(Data.Failure(InvalidIdError))
        } else {
            invoiceRepository.observeInvoice(invoiceId)
                .map { invoiceData ->
                    when (invoiceData) {
                        is Data.Success -> invoiceData.content?.let { invoice ->
                            Data.Success(invoice)
                        } ?: Data.Failure(InvoiceNotFoundError)
                        else -> invoiceData as Data<Invoice>
                    }
                }
        }

    override fun saveInvoice(invoice: Invoice): Flow<Data<Unit>> =
        invoiceRepository.saveInvoice(invoice)

    override suspend fun prepareInvoicePhotoFile(): Data<Uri> =
        invoiceRepository.createInvoicePhotoFile()

    companion object {
        const val PAGE_SIZE = 10
    }
}