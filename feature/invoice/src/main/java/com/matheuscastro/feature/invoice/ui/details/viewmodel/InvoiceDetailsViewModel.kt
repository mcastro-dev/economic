package com.matheuscastro.feature.invoice.ui.details.viewmodel

import androidx.lifecycle.viewModelScope
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.viewmodel.BaseViewModel
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceInteractor
import com.matheuscastro.feature.invoice.ui.details.InvoiceDetailsContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailsViewModel @Inject constructor(
    private val interactor: InvoiceInteractor
) : BaseViewModel<InvoiceDetailsContract.InvoiceDetailsState>(
    initialState = InvoiceDetailsContract.InvoiceDetailsState(
        invoice = Data.None
    )
) {

    override fun onEvent(event: UiEvent) {
        when (event) {
            is InvoiceDetailsContract.InvoiceDetailsEvent.LoadInvoice -> onLoadInvoice(event.invoiceId)
        }
    }

    private fun onLoadInvoice(invoiceId: UId) = viewModelScope.launch {
        interactor
            .observeInvoice(invoiceId)
            .collectLatest { invoiceData ->
                _state.update {
                    it.copy(invoice = invoiceData)
                }
            }
    }
}