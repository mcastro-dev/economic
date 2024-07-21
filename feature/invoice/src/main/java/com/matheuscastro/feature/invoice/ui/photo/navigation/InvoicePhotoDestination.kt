package com.matheuscastro.feature.invoice.ui.photo.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.core.ui.navigation.Destination

class InvoicePhotoDestination : Destination(
    routeSkeleton = "InvoicePhoto/{invoiceId}",
    arguments = listOf(
        navArgument("invoiceId") { type = NavType.LongType }
    )
) {
    override fun populateRouteSkeleton(args: List<String>) =
        "InvoicePhoto/${args.first()}"

    fun extractInvoiceId(bundle: Bundle?): UId =
        bundle
            ?.getLong("invoiceId")
            ?.let { id ->
                UId(id)
            } ?: UId.invalid()
}