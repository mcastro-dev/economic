package com.matheuscastro.feature.invoice.ui.details

import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.core.ui.viewmodel.State
import com.matheuscastro.feature.invoice.domain.model.Invoice
import com.matheuscastro.feature.invoice.ui.details.navigation.InvoiceDetailsDestination

interface InvoiceDetailsContract {

    companion object {
        val DESTINATION = InvoiceDetailsDestination()
    }

    sealed class Output {
        object Back : Output()

        data class EditInvoice(
            val invoiceId: UId
        ) : Output()

        data class OpenInvoicePhoto(
            val invoiceId: UId
        ) : Output()
    }

    data class InvoiceDetailsState(
        val invoice: Data<Invoice>
    ) : State

    sealed class InvoiceDetailsEvent : UiEvent {
        data class LoadInvoice(
            val invoiceId: UId
        ) : InvoiceDetailsEvent()
    }
}