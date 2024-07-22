package com.matheuscastro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matheuscastro.feature.invoice.data.local.datasource.InvoiceDao
import com.matheuscastro.feature.invoice.data.local.model.InvoiceEntity

@Database(
    entities = [InvoiceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_FILE_NAME = "economic.db"
    }

    abstract fun invoiceDao(): InvoiceDao
}