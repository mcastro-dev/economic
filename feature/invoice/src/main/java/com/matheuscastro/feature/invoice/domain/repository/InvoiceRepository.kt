package com.matheuscastro.feature.invoice.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.feature.invoice.domain.model.Invoice
import kotlinx.coroutines.flow.Flow

internal interface InvoiceRepository {
    fun observeInvoicePages(pageSize: Int): Flow<PagingData<Invoice>>
    fun observeInvoice(invoiceId: UId): Flow<Data<Invoice?>>
    fun saveInvoice(invoice: Invoice): Flow<Data<Unit>>
    suspend fun createInvoicePhotoFile(): Data<Uri>
}