package com.matheuscastro.feature.invoice.ui.details.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.navigation.Destination

class InvoiceDetailsDestination : Destination(
    routeSkeleton = "InvoiceDetails/{invoiceId}",
    arguments = listOf(
        navArgument("invoiceId") { type = NavType.LongType }
    )
) {
    override fun populateRouteSkeleton(args: List<String>) =
        "InvoiceDetails/${args.first()}"

    fun extractInvoiceId(bundle: Bundle?): UId =
        bundle
            ?.getLong("invoiceId")
            ?.let { id ->
                UId(id)
            } ?: UId.invalid()
}