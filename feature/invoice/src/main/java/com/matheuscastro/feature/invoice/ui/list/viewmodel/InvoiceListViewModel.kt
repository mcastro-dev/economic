package com.matheuscastro.feature.invoice.ui.list.viewmodel

import com.matheuscastro.core.ui.viewmodel.BaseViewModel
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.feature.invoice.ui.list.InvoiceListContract
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class InvoiceListViewModel @Inject constructor(
    private val interactor: InvoiceInteractor
) : BaseViewModel<InvoiceListContract.InvoiceListState>(
    initialState = InvoiceListContract.InvoiceListState(
        invoicesPagingData = emptyFlow(),
        month = Calendar.getInstance().get(Calendar.MONTH),
        year = Calendar.getInstance().get(Calendar.YEAR)
    )
) {

    override fun onEvent(event: UiEvent) {
        when (event) {
            is InvoiceListContract.InvoiceListEvent.LoadInvoices -> onLoadInvoices()
        }
    }

    private fun onLoadInvoices() {
        _state.update {
            it.copy(
                invoicesPagingData = interactor.observeInvoicePages()
            )
        }
    }
}