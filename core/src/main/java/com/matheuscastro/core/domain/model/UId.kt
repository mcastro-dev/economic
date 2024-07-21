package com.matheuscastro.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UId(
    val value: Long
) : Parcelable {
    companion object {
        private const val INVALID_ID: Long = 0
        fun invalid() = UId(INVALID_ID)
    }

    init {
        require(value >= 0)
    }

    fun isInvalid() = value == INVALID_ID

    override fun toString(): String = value.toString()
}
