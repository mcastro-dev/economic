package com.matheuscastro.feature.invoice.ui.photo.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.data.mapSuccessContent
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.viewmodel.BaseViewModel
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.feature.invoice.ui.photo.InvoicePhotoContract.InvoicePhotoEvent
import com.matheuscastro.feature.invoice.ui.photo.InvoicePhotoContract.InvoicePhotoState
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoicePhotoViewModel @Inject constructor(
    private val interactor: InvoiceInteractor
) : BaseViewModel<InvoicePhotoState>(
    initialState = InvoicePhotoState(
        invoicePhotoUri = Data.None
    )
) {
    override fun onEvent(event: UiEvent) {
        when (event) {
            is InvoicePhotoEvent.LoadInvoicePhoto -> onLoadInvoicePhoto(event.invoiceId)
        }
    }

    private fun onLoadInvoicePhoto(invoiceId: UId) = viewModelScope.launch {
        interactor
            .observeInvoice(invoiceId)
            .collectLatest { invoiceData ->
                when (invoiceData) {
                    is Data.Success -> invoiceData.mapSuccessContent { invoice ->
                        _state.update {
                            it.copy(invoicePhotoUri = Data.Success(invoice.photoUri))
                        }
                    }
                    else -> {
                        _state.update {
                            it.copy(invoicePhotoUri = invoiceData as Data<Uri>)
                        }
                    }
                }
            }
    }
}