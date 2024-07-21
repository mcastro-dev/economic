package com.matheuscastro.feature.invoice.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matheuscastro.feature.invoice.R
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.composable.FullPageLoadingIndicator
import com.matheuscastro.core.ui.composable.MoneyItem
import com.matheuscastro.core.ui.composable.TopBar
import com.matheuscastro.feature.invoice.domain.model.Invoice
import com.matheuscastro.feature.invoice.ui.details.InvoiceDetailsContract.InvoiceDetailsEvent.LoadInvoice
import com.matheuscastro.feature.invoice.ui.photo.composable.InvoicePhoto
import com.matheuscastro.feature.invoice.ui.details.viewmodel.InvoiceDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailsScreen(
    invoiceId: UId,
    onResult: (InvoiceDetailsContract.Output) -> Unit,
    viewModel: InvoiceDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(LoadInvoice(invoiceId)) {
        viewModel.onEvent(LoadInvoice(invoiceId))
    }
    
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.invoice_details_title),
                onBackClicked = {
                    onResult(InvoiceDetailsContract.Output.Back)
                }
            )
        }
    ) { padding ->
        when (val invoiceData = state.invoice) {
            is Data.Success -> SuccessState(invoiceData, onResult, padding)
            is Data.Failure -> FailureState(padding)
            is Data.Loading -> LoadingState(padding)
            is Data.None -> Unit
        }
    }
}

@Composable
private fun SuccessState(
    invoiceData: Data.Success<Invoice>,
    onResult: (InvoiceDetailsContract.Output) -> Unit,
    padding: PaddingValues
) = invoiceData.content.let { invoice ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        InvoicePhoto(
            modifier = Modifier.height(280.dp),
            photoUri = invoice.photoUri,
            onClick = {
                onResult(InvoiceDetailsContract.Output.OpenInvoicePhoto(invoice.id))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InvoiceProperty(
            label = stringResource(R.string.invoice_details_label_customer),
            content = {
                InvoicePropertyTextContent(invoice.customer)
            }
        )
        InvoiceProperty(
            label = stringResource(R.string.invoice_details_label_total),
            content = {
                MoneyItem(money = invoice.total)
            }
        )
        InvoiceProperty(
            label = stringResource(R.string.invoice_details_label_date),
            content = {
                InvoicePropertyTextContent(invoice.date.toString())
            }
        )
    }
}

@Composable
private fun FailureState(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.invoice_details_invoice_load_failure))
    }
}

@Composable
private fun LoadingState(padding: PaddingValues) {
    FullPageLoadingIndicator(
        modifier = Modifier.padding(padding)
    )
}

@Composable
private fun InvoiceProperty(label: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        content()
        Spacer(modifier = Modifier.height(4.dp))
        Divider()
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InvoicePropertyTextContent(value: String) {
    Text(
        text = value,
        style = MaterialTheme.typography.bodyLarge
    )
}