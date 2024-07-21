package com.matheuscastro.feature.invoice.ui.list.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.feature.invoice.R
import com.matheuscastro.feature.invoice.domain.model.Invoice

@Composable
fun InvoicesList(
    invoicePagingItems: LazyPagingItems<Invoice>,
    onInvoiceClicked: (UId) -> Unit,
    onEmptyListClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp)
    ) {
        if (invoicePagingItems.itemCount == 0
            && invoicePagingItems.loadState.append is LoadState.NotLoading
            && invoicePagingItems.loadState.prepend is LoadState.NotLoading
            && invoicePagingItems.loadState.refresh is LoadState.NotLoading) {
            item {
                EmptyList(
                    onClick = onEmptyListClicked
                )
            }
        } else {
            items(
                count = invoicePagingItems.itemCount,
                key = invoicePagingItems.itemKey { requireNotNull(it.id) },
            ) { index ->
                invoicePagingItems[index]?.let { invoice ->
                    InvoiceItem(
                        invoice = invoice,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onInvoiceClicked(invoice.id)
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyList(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.AddCircle,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = stringResource(R.string.invoice_list_create_button_description)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.invoice_list_empty_cta),
                textAlign = TextAlign.Center
            )
        }
    }
}