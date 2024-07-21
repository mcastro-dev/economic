package com.matheuscastro.feature.invoice.ui.photo

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.composable.FullPageLoadingIndicator
import com.matheuscastro.feature.invoice.R
import com.matheuscastro.feature.invoice.ui.photo.InvoicePhotoContract.InvoicePhotoEvent
import com.matheuscastro.feature.invoice.ui.photo.composable.InvoicePhoto
import com.matheuscastro.feature.invoice.ui.photo.viewmodel.InvoicePhotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicePhotoScreen(
    invoiceId: UId,
    viewModel: InvoicePhotoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(InvoicePhotoEvent.LoadInvoicePhoto(invoiceId)) {
        viewModel.onEvent(InvoicePhotoEvent.LoadInvoicePhoto(invoiceId))
    }

    Scaffold { padding ->
        when (val photoUriData = state.invoicePhotoUri) {
            is Data.Success -> SuccessState(photoUriData, padding)
            is Data.Failure -> FailureState(padding)
            is Data.Loading -> LoadingState(padding)
            is Data.None -> Unit
        }
    }
}

@Composable
private fun SuccessState(
    photoUriData: Data.Success<Uri>,
    padding: PaddingValues
) {
    InvoicePhoto(
        modifier = Modifier.fillMaxSize().padding(padding),
        photoUri = photoUriData.content
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
        Text(stringResource(R.string.invoice_details_invoice_load_failure))
    }
}

@Composable
private fun LoadingState(padding: PaddingValues) {
    FullPageLoadingIndicator(
        modifier = Modifier.padding(padding)
    )
}