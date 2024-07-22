package com.matheuscastro.database.di

import android.content.Context
import androidx.room.Room
import com.matheuscastro.database.AppDatabase
import com.matheuscastro.feature.invoice.data.local.datasource.InvoiceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = AppDatabase.DB_FILE_NAME,
    ).build()

    @Provides
    @Singleton
    fun provideInvoiceDao(
        database: AppDatabase
    ): InvoiceDao = database.invoiceDao()
}