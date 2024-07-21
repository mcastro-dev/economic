package com.matheuscastro.feature.invoice.ui.photo

import android.net.Uri
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.viewmodel.State
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.feature.invoice.ui.photo.navigation.InvoicePhotoDestination

interface InvoicePhotoContract {

    companion object {
        val DESTINATION = InvoicePhotoDestination()
    }

    sealed class Output {
        object Back : Output()
    }

    data class InvoicePhotoState(
        val invoicePhotoUri: Data<Uri>
    ) : State

    sealed class InvoicePhotoEvent : UiEvent {
        data class LoadInvoicePhoto(
            val invoiceId: UId
        ) : InvoicePhotoEvent()
    }
}