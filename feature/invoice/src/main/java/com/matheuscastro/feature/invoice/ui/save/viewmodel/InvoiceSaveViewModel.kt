package com.matheuscastro.feature.invoice.ui.save.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.data.mapSuccessContent
import com.matheuscastro.core.domain.model.InvalidIdError
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.model.Field.ValidationResult
import com.matheuscastro.core.ui.viewmodel.BaseViewModel
import com.matheuscastro.core.ui.viewmodel.UiEvent
import com.matheuscastro.feature.invoice.ui.save.InvoiceSaveContract.InvoiceSaveEvent
import com.matheuscastro.feature.invoice.ui.save.InvoiceSaveContract.InvoiceSaveOneTimeEvent
import com.matheuscastro.feature.invoice.ui.save.InvoiceSaveContract.InvoiceSaveState
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceInteractor
import com.matheuscastro.feature.invoice.domain.model.Invoice
import com.matheuscastro.feature.invoice.ui.save.mapper.toDomainModel
import com.matheuscastro.feature.invoice.ui.save.mapper.toUiModel
import com.matheuscastro.feature.invoice.ui.save.model.InvoiceSaveMode
import com.matheuscastro.feature.invoice.ui.save.model.UiInvoice
import com.matheuscastro.feature.invoice.ui.save.model.toCustomerField
import com.matheuscastro.feature.invoice.ui.save.model.toDateField
import com.matheuscastro.feature.invoice.ui.save.model.toMoneyField
import com.matheuscastro.feature.invoice.ui.save.model.toPhotoField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InvoiceSaveViewModel @Inject constructor(
    private val interactor: InvoiceInteractor
) : BaseViewModel<InvoiceSaveState>(
    initialState = InvoiceSaveState(
        invoice = Data.None,
        mode = null,
        isSaving = false,
        saveSucceeded = null,
        saveFailed = null
    )
) {

    override fun onEvent(event: UiEvent) {
        when (event) {
            is InvoiceSaveEvent.LoadInvoice -> onLoadInvoice(event.invoiceId)
            is InvoiceSaveEvent.CustomerChanged -> onCustomerChanged(event.customer)
            is InvoiceSaveEvent.TotalChanged -> onTotalChanged(event.total)
            is InvoiceSaveEvent.DateChanged -> onDateChanged(event.date)
            is InvoiceSaveEvent.SaveInvoice -> onSaveInvoice()
            is InvoiceSaveEvent.OneTimeEventConsumed -> onOneTimeEventConsumed(event.oneTimeEvent)
            is InvoiceSaveEvent.RequestPhotoFile -> onRequestPhotoFile()
            is InvoiceSaveEvent.PhotoSucceeded -> onPhotoSucceeded()
            is InvoiceSaveEvent.PhotoFailed -> onPhotoFailed()
        }
    }

    private fun onLoadInvoice(invoiceId: UId) = viewModelScope.launch {
        interactor
            .observeInvoice(invoiceId)
            .collectLatest { invoiceData ->
                if (invoiceData.isInvalidIdError()) {
                    _state.update {
                        it.copy(
                            invoice = Data.Success(
                                UiInvoice(dateField = LocalDate.now().toDateField())
                            ),
                            mode = InvoiceSaveMode.CREATE,
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            invoice = invoiceData.mapSuccessContent { invoice -> invoice.toUiModel() },
                            mode = InvoiceSaveMode.EDIT,
                        )
                    }
                }
            }
    }

    private fun onCustomerChanged(customer: String) {
        val invoiceSuccessData = _state.value.invoice.requireSuccessState()
        _state.update {
            it.copy(
                invoice = Data.Success(
                    content = invoiceSuccessData.content.copy(
                        customerField = customer.toCustomerField()
                    )
                )
            )
        }
    }

    private fun onTotalChanged(total: String) {
        val invoiceSuccessData = _state.value.invoice.requireSuccessState()
        _state.update {
            it.copy(
                invoice = Data.Success(
                    content = invoiceSuccessData.content.copy(
                        totalMoneyField = total.toMoneyField()
                    )
                )
            )
        }
    }

    private fun onDateChanged(date: String) {
        val invoiceSuccessData = _state.value.invoice.requireSuccessState()
        _state.update {
            it.copy(
                invoice = Data.Success(
                    content = invoiceSuccessData.content.copy(
                        dateField = date.toDateField()
                    )
                )
            )
        }
    }

    private fun onSaveInvoice() = viewModelScope.launch {
        val invoiceSuccessData = _state.value.invoice.requireSuccessState()

        val fieldsValidationResult = validateFields()
        if (fieldsValidationResult == ValidationResult.INVALID) {
            return@launch
        }

        interactor
            .saveInvoice(invoiceSuccessData.content.toDomainModel())
            .collectLatest { data ->
                when (data) {
                    is Data.Success -> {
                        _state.update {
                            initialState.copy(
                                saveSucceeded = InvoiceSaveOneTimeEvent.SaveSucceeded
                            )
                        }
                    }
                    is Data.Failure -> {
                        _state.update {
                            it.copy(
                                isSaving = false,
                                saveFailed = InvoiceSaveOneTimeEvent.SaveFailed(data.error)
                            )
                        }
                    }
                    is Data.Loading -> {
                        _state.update { it.copy(isSaving = true) }
                    }
                    is Data.None -> Unit
                }
            }
    }

    private fun onOneTimeEventConsumed(oneTimeEvent: InvoiceSaveOneTimeEvent) {
        when (oneTimeEvent) {
            is InvoiceSaveOneTimeEvent.SaveSucceeded -> _state.update {
                it.copy(saveSucceeded = null)
            }
            is InvoiceSaveOneTimeEvent.SaveFailed -> _state.update {
                it.copy(saveFailed = null)
            }
        }
    }

    private fun onRequestPhotoFile() = viewModelScope.launch {
        when (val photoFileData = interactor.prepareInvoicePhotoFile()) {
            is Data.Success -> {
                val invoiceSuccessData = _state.value.invoice.requireSuccessState()
                _state.update {
                    it.copy(
                        invoice = invoiceSuccessData.mapSuccessContent { invoice ->
                            invoice.copy(
                                photoField = invoice.photoField.copy(
                                    cameraLauncherUri = photoFileData.content
                                )
                            )
                        }
                    )
                }
            }
            is Data.Failure -> {
                // TODO mat: emit one time event error
            }
            else -> Unit
        }
    }

    private fun onPhotoSucceeded() {
        val invoiceSuccessData = _state.value.invoice.requireSuccessState()
        val cameraLauncherUri = requireNotNull(invoiceSuccessData.content.photoField.cameraLauncherUri)
        _state.update {
            it.copy(
                invoice = invoiceSuccessData.mapSuccessContent { invoice ->
                    invoice.copy(
                        photoField = cameraLauncherUri.toPhotoField()
                    )
                }
            )
        }
    }

    private fun onPhotoFailed() {
        val invoiceSuccessData = _state.value.invoice.requireSuccessState()
        _state.update {
            it.copy(
                invoice = invoiceSuccessData.mapSuccessContent { invoice ->
                    invoice.copy(
                        photoField = Uri.EMPTY.toPhotoField()
                    )
                }
            )
        }
    }

    private fun validateFields(): ValidationResult {
        val invoiceSuccessData = _state.value.invoice.requireSuccessState()
        val validatedDateField = invoiceSuccessData.content.dateField.validate()
        val validatedCustomerField = invoiceSuccessData.content.customerField.validate()
        val validatedTotalMoneyField = invoiceSuccessData.content.totalMoneyField.validate()
        val validatedPhotoField = invoiceSuccessData.content.photoField.validate()

        _state.update {
            it.copy(
                invoice = Data.Success(
                    content = invoiceSuccessData.content.copy(
                        dateField = validatedDateField,
                        customerField = validatedCustomerField,
                        totalMoneyField = validatedTotalMoneyField,
                        photoField = validatedPhotoField
                    )
                )
            )
        }

        return if (validatedDateField.validationResult == ValidationResult.INVALID
            || validatedCustomerField.validationResult == ValidationResult.INVALID
            || validatedTotalMoneyField.validationResult == ValidationResult.INVALID
            || validatedPhotoField.validationResult == ValidationResult.INVALID) {
            ValidationResult.INVALID
        } else {
            ValidationResult.VALID
        }
    }

    private fun Data<Invoice>.isInvalidIdError(): Boolean =
        this is Data.Failure && this.error is InvalidIdError

    private fun Data<UiInvoice>.requireSuccessState(): Data.Success<UiInvoice> =
        this as? Data.Success ?: throw RuntimeException("Unexpected invoice data state")
}