package com.matheuscastro.feature.invoice.ui.save.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.navigation.Destination

class InvoiceSaveDestination : Destination(
    routeSkeleton = "InvoiceSave/{invoiceId}",
    arguments = listOf(
        navArgument("invoiceId") { type = NavType.LongType }
    )
) {
    override fun populateRouteSkeleton(args: List<String>) =
        "InvoiceSave/${args.first()}"

    fun extractInvoiceId(bundle: Bundle?): UId =
        bundle
            ?.getLong("invoiceId")
            ?.let { id ->
                UId(id)
            } ?: UId.invalid()
}