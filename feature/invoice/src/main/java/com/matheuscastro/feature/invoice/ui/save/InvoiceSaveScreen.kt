package com.matheuscastro.feature.invoice.ui.save

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.composable.FullPageLoadingIndicator
import com.matheuscastro.core.ui.composable.TopBar
import com.matheuscastro.feature.invoice.R
import com.matheuscastro.feature.invoice.ui.save.InvoiceSaveContract.InvoiceSaveEvent
import com.matheuscastro.feature.invoice.ui.save.composable.InvoiceForm
import com.matheuscastro.feature.invoice.ui.save.model.InvoiceSaveMode.*
import com.matheuscastro.feature.invoice.ui.save.model.UiInvoice
import com.matheuscastro.feature.invoice.ui.save.viewmodel.InvoiceSaveViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceSaveScreen(
    invoiceId: UId,
    onResult: (InvoiceSaveContract.Output) -> Unit,
    viewModel: InvoiceSaveViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.saveSucceeded?.let { saveSucceeded ->
        ShowToast(
            context = LocalContext.current,
            text = stringResource(R.string.invoice_save_toast_success)
        )
        onResult(InvoiceSaveContract.Output.Back)
        viewModel.onEvent(InvoiceSaveEvent.OneTimeEventConsumed(saveSucceeded))
    }

    state.saveFailed?.let { saveFailed ->
        ShowToast(
            context = LocalContext.current,
            text = stringResource(R.string.invoice_save_toast_failure)
        )
        viewModel.onEvent(InvoiceSaveEvent.OneTimeEventConsumed(saveFailed))
    }

    LaunchedEffect(InvoiceSaveEvent.LoadInvoice(invoiceId)) {
        viewModel.onEvent(InvoiceSaveEvent.LoadInvoice(invoiceId))
    }

    Scaffold(
        topBar = {
            TopBar(
                title = when (state.mode) {
                    CREATE -> stringResource(id = R.string.invoice_save_create_mode_title)
                    EDIT -> stringResource(id = R.string.invoice_save_edit_mode_title)
                    null -> ""
                },
                onBackClicked = {
                    onResult(InvoiceSaveContract.Output.Back)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                isSaving = state.isSaving,
                onClick = {
                    viewModel.onEvent(InvoiceSaveEvent.SaveInvoice)
                }
            )
        }
    ) { padding ->
        when (val invoiceData = state.invoice) {
            is Data.Loading -> LoadingState(padding)
            is Data.Failure -> FailureState(padding)
            is Data.Success -> SuccessState(
                invoice = invoiceData.content,
                isSaving = state.isSaving,
                padding = padding,
                onCustomerChanged = {
                    viewModel.onEvent(InvoiceSaveEvent.CustomerChanged(it))
                },
                onTotalChanged = {
                    viewModel.onEvent(InvoiceSaveEvent.TotalChanged(it))
                },
                onDateChanged = {
                    viewModel.onEvent(InvoiceSaveEvent.DateChanged(it))
                },
                onRequestPhotoFile = {
                    viewModel.onEvent(InvoiceSaveEvent.RequestPhotoFile)
                },
                onPhotoSuccess = {
                    viewModel.onEvent(InvoiceSaveEvent.PhotoSucceeded)
                },
                onPhotoFailure = {
                    viewModel.onEvent(InvoiceSaveEvent.PhotoFailed)
                }
            )
            is Data.None -> Unit
        }
    }
}

@Composable
private fun SuccessState(
    invoice: UiInvoice,
    isSaving: Boolean,
    padding: PaddingValues,
    onCustomerChanged: (String) -> Unit,
    onTotalChanged: (String) -> Unit,
    onDateChanged: (String) -> Unit,
    onRequestPhotoFile: () -> Unit,
    onPhotoSuccess: () -> Unit,
    onPhotoFailure: () -> Unit
) {
    InvoiceForm(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        invoice = invoice,
        isSaving = isSaving,
        onCustomerChanged = onCustomerChanged,
        onTotalChanged = onTotalChanged,
        onDateChanged = onDateChanged,
        onRequestPhotoFile = onRequestPhotoFile,
        onPhotoSuccess = onPhotoSuccess,
        onPhotoFailure = onPhotoFailure
    )
}

@Composable
private fun FailureState(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(id = R.string.invoice_save_invoice_load_failure))
    }
}

@Composable
private fun LoadingState(padding: PaddingValues) {
    FullPageLoadingIndicator(
        modifier = Modifier.padding(padding)
    )
}

@Composable
private fun FloatingActionButton(isSaving: Boolean, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            if (!isSaving) {
                onClick()
            }
        }
    ) {
        if (isSaving) {
            CircularProgressIndicator()
        } else {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = stringResource(id = R.string.invoice_save_button_description)
            )
        }
    }
}

@Composable
private fun ShowToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}