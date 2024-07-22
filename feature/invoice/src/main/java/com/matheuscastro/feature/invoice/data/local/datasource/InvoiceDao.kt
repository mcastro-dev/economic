package com.matheuscastro.feature.invoice.data.local.datasource

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.matheuscastro.feature.invoice.data.local.model.InvoiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {

    @Upsert
    suspend fun upsertInvoice(invoice: InvoiceEntity)

    @Query("SELECT * FROM invoices ORDER BY date DESC, customer ASC")
    fun getInvoicesPages(): PagingSource<Int, InvoiceEntity>

    @Query("SELECT * FROM invoices WHERE id = :id")
    fun getInvoiceById(id: Long): Flow<InvoiceEntity?>
}