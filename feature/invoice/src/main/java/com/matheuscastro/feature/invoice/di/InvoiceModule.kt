package com.matheuscastro.feature.invoice.di

import com.matheuscastro.feature.invoice.data.local.FileDataSource
import com.matheuscastro.feature.invoice.data.local.FileDataSourceImpl
import com.matheuscastro.feature.invoice.data.repository.InvoiceFilterRepositoryImpl
import com.matheuscastro.feature.invoice.data.repository.InvoiceRepositoryImpl
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceFilterInteractor
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceFilterInteractorImpl
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceInteractor
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceInteractorImpl
import com.matheuscastro.feature.invoice.domain.repository.InvoiceFilterRepository
import com.matheuscastro.feature.invoice.domain.repository.InvoiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InvoiceModule {

    @Binds
    abstract fun bindInvoiceInteractor(impl: InvoiceInteractorImpl): InvoiceInteractor

    @Binds
    abstract fun bindInvoiceFilterInteractor(impl: InvoiceFilterInteractorImpl): InvoiceFilterInteractor

    @Binds
    abstract fun bindInvoiceRepository(impl: InvoiceRepositoryImpl): InvoiceRepository

    @Binds
    abstract fun bindInvoiceFilterRepository(impl: InvoiceFilterRepositoryImpl): InvoiceFilterRepository

    @Binds
    abstract fun bindFileDataSource(impl: FileDataSourceImpl): FileDataSource
}