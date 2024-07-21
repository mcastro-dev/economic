package com.matheuscastro.feature.invoice.ui.list

import androidx.paging.PagingData
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.viewmodel.State
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.feature.invoice.domain.model.Invoice
import com.matheuscastro.feature.invoice.ui.list.navigation.InvoiceListDestination
import kotlinx.coroutines.flow.Flow

interface InvoiceListContract {

    companion object {
        val DESTINATION = InvoiceListDestination()
    }

    sealed class Output {
        data class InvoiceDetails(
            val invoiceId: UId
        ) : Output()

        object InvoiceCreation : Output()
    }

    data class InvoiceListState(
        val invoicesPagingData: Flow<PagingData<Invoice>>,
        val month: Int,
        val year: Int
    ) : State

    sealed class InvoiceListEvent : UiEvent {
        object LoadInvoices : InvoiceListEvent()
    }
}