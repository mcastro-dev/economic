package com.matheuscastro.feature.invoice.ui.save

import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.Error
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.viewmodel.OneTimeEvent
import com.matheuscastro.core.ui.viewmodel.State
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.feature.invoice.ui.save.model.InvoiceSaveMode
import com.matheuscastro.feature.invoice.ui.save.model.UiInvoice
import com.matheuscastro.feature.invoice.ui.save.navigation.InvoiceSaveDestination

interface InvoiceSaveContract {

    companion object {
        val DESTINATION = InvoiceSaveDestination()
    }

    sealed class Output {
        object Back : Output()
    }

    data class InvoiceSaveState(
        val invoice: Data<UiInvoice>,
        val mode: InvoiceSaveMode?,
        val isSaving: Boolean,
        val saveSucceeded: InvoiceSaveOneTimeEvent.SaveSucceeded?,
        val saveFailed: InvoiceSaveOneTimeEvent.SaveFailed?
    ) : State

    sealed class InvoiceSaveEvent : UiEvent {
        data class LoadInvoice(
            val invoiceId: UId
        ) : InvoiceSaveEvent()

        data class CustomerChanged(
            val customer: String
        ) : InvoiceSaveEvent()

        data class TotalChanged(
            val total: String
        ) : InvoiceSaveEvent()

        data class DateChanged(
            val date: String
        ) : InvoiceSaveEvent()

        data class OneTimeEventConsumed(
            val oneTimeEvent: InvoiceSaveOneTimeEvent
        ) : InvoiceSaveEvent()

        object RequestPhotoFile : InvoiceSaveEvent()

        object PhotoSucceeded : InvoiceSaveEvent()

        object PhotoFailed : InvoiceSaveEvent()

        object SaveInvoice : InvoiceSaveEvent()
    }

    sealed class InvoiceSaveOneTimeEvent : OneTimeEvent {
        object SaveSucceeded : InvoiceSaveOneTimeEvent()

        data class SaveFailed(
            val error: Error
        ) : InvoiceSaveOneTimeEvent()
    }
}