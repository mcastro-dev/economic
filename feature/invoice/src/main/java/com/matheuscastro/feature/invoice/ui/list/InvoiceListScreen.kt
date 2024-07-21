package com.matheuscastro.feature.invoice.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.matheuscastro.core.ui.composable.TopBar
import com.matheuscastro.feature.invoice.R
import com.matheuscastro.feature.invoice.ui.list.InvoiceListContract.InvoiceListEvent.LoadInvoices
import com.matheuscastro.feature.invoice.ui.list.composable.InvoicesList
import com.matheuscastro.feature.invoice.ui.list.viewmodel.InvoiceListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceListScreen(
    onResult: (InvoiceListContract.Output) -> Unit,
    viewModel: InvoiceListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val invoicePagingItems = state.invoicesPagingData.collectAsLazyPagingItems()

    LaunchedEffect(LoadInvoices) {
        viewModel.onEvent(LoadInvoices)
    }

    Scaffold(
        topBar = {
            TopBar(stringResource(R.string.invoice_list_title))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onResult(InvoiceListContract.Output.InvoiceCreation)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.invoice_list_create_button_description)
                )
            }
        }
    ) { padding ->
        InvoicesList(
            invoicePagingItems = invoicePagingItems,
            onInvoiceClicked = { id ->
                onResult(InvoiceListContract.Output.InvoiceDetails(invoiceId = id))
            },
            onEmptyListClicked = {
                onResult(InvoiceListContract.Output.InvoiceCreation)
            },
            modifier = Modifier.padding(padding)
        )
    }
}

