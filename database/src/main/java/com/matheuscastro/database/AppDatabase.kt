package com.matheuscastro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matheuscastro.database.dao.InvoiceDao
import com.matheuscastro.database.entity.invoice.InvoiceEntity

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