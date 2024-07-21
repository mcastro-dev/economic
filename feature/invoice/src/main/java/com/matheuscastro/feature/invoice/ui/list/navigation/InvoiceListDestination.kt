package com.matheuscastro.feature.invoice.ui.list.navigation

import com.matheuscastro.core.ui.navigation.Destination

class InvoiceListDestination : Destination(
    routeSkeleton = "InvoiceList",
    arguments = emptyList()
) {
    override fun populateRouteSkeleton(args: List<String>): String = routeSkeleton
}