package com.matheuscastro.core.ui.navigation

import androidx.navigation.NamedNavArgument

abstract class Destination(
    val routeSkeleton: String,
    val arguments: List<NamedNavArgument>
) {
    abstract fun populateRouteSkeleton(args: List<String>): String
}