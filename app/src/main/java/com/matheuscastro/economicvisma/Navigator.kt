package com.matheuscastro.economicvisma

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.feature.invoice.ui.details.InvoiceDetailsContract
import com.matheuscastro.feature.invoice.ui.details.InvoiceDetailsScreen
import com.matheuscastro.feature.invoice.ui.list.InvoiceListContract
import com.matheuscastro.feature.invoice.ui.list.InvoiceListScreen
import com.matheuscastro.feature.invoice.ui.photo.InvoicePhotoContract
import com.matheuscastro.feature.invoice.ui.photo.InvoicePhotoScreen
import com.matheuscastro.feature.invoice.ui.save.InvoiceSaveContract
import com.matheuscastro.feature.invoice.ui.save.InvoiceSaveScreen

@Composable
fun Navigator() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = InvoiceListContract.DESTINATION.routeSkeleton
    ) {

        composable(
            route = InvoiceListContract.DESTINATION.routeSkeleton
        ) {
            InvoiceListScreen(
                onResult = { output ->
                    navController.navigate(
                        route = when (output) {
                            is InvoiceListContract.Output.InvoiceDetails -> InvoiceDetailsContract.DESTINATION
                                .populateRouteSkeleton(args = listOf(output.invoiceId.toString()))
                            is InvoiceListContract.Output.InvoiceCreation -> InvoiceSaveContract.DESTINATION
                                .populateRouteSkeleton(args = listOf(UId.invalid().toString()))
                        }
                    )
                }
            )
        }

        composable(
            route = InvoiceDetailsContract.DESTINATION.routeSkeleton,
            arguments = InvoiceDetailsContract.DESTINATION.arguments
        ) {
            val invoiceId = InvoiceDetailsContract.DESTINATION.extractInvoiceId(it.arguments)
            InvoiceDetailsScreen(
                invoiceId = invoiceId,
                onResult = { output ->
                    when (output) {
                        is InvoiceDetailsContract.Output.Back -> navController.navigateUp()
                        is InvoiceDetailsContract.Output.EditInvoice -> {
                            // TODO mat: navigate to edit invoice
                        }
                        is InvoiceDetailsContract.Output.OpenInvoicePhoto -> navController.navigate(
                            route = InvoicePhotoContract.DESTINATION
                                .populateRouteSkeleton(args = listOf(output.invoiceId.toString()))
                        )
                    }
                }
            )
        }

        composable(
            route = InvoiceSaveContract.DESTINATION.routeSkeleton,
            arguments = InvoiceSaveContract.DESTINATION.arguments
        ) {
            val invoiceId = InvoiceSaveContract.DESTINATION.extractInvoiceId(it.arguments)
            InvoiceSaveScreen(
                invoiceId = invoiceId,
                onResult = { output ->
                    when (output) {
                        is InvoiceSaveContract.Output.Back -> navController.navigateUp()
                    }
                }
            )
        }

        composable(
            route = InvoicePhotoContract.DESTINATION.routeSkeleton,
            arguments = InvoicePhotoContract.DESTINATION.arguments
        ) {
            val invoiceId = InvoicePhotoContract.DESTINATION.extractInvoiceId(it.arguments)
            InvoicePhotoScreen(invoiceId = invoiceId)
        }
    }
}