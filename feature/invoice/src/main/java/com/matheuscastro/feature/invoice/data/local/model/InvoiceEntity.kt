package com.matheuscastro.feature.invoice.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val customer: String,
    val date: String,
    val total: Long,
    val totalCurrency: String,
    val photoPath: String
)
