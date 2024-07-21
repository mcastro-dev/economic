package com.matheuscastro.feature.invoice.data.repository

import android.net.Uri
import android.os.Environment
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.database.dao.InvoiceDao
import com.matheuscastro.feature.invoice.data.local.FileDataSource
import com.matheuscastro.feature.invoice.data.mapper.toDomain
import com.matheuscastro.feature.invoice.data.mapper.toEntity
import com.matheuscastro.feature.invoice.domain.model.Invoice
import com.matheuscastro.feature.invoice.domain.model.InvoiceNotFoundError
import com.matheuscastro.feature.invoice.domain.model.SaveInvoiceError
import com.matheuscastro.feature.invoice.domain.repository.InvoiceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class InvoiceRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val invoiceDataSource: InvoiceDao,
    private val fileDataSource: FileDataSource
) : InvoiceRepository {

    override fun observeInvoicePages(pageSize: Int): Flow<PagingData<Invoice>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize
            )
        ) {
            invoiceDataSource.getInvoicesPages()
        }
            .flow
            .map { pagingData ->
                pagingData.map { invoiceEntity ->
                    invoiceEntity.toDomain()
                }
            }
            .flowOn(dispatcher)

    override fun observeInvoice(invoiceId: UId): Flow<Data<Invoice?>> =
        flow {
            emit(Data.Loading)
        }.flatMapConcat {
            invoiceDataSource.getInvoiceById(invoiceId.value)
        }.map { invoiceEntity ->
            invoiceEntity?.let {
                Data.Success(invoiceEntity.toDomain())
            } ?: Data.Failure(InvoiceNotFoundError)
        }.flowOn(dispatcher)

    override fun saveInvoice(invoice: Invoice): Flow<Data<Unit>> =
        flow {
            emit(Data.Loading)
            try {
                invoiceDataSource.upsertInvoice(invoice.toEntity())
                emit(Data.Success(Unit))

            } catch (e: Exception) {
                // TODO mat: Catch only specific exceptions
                e.printStackTrace()
                emit(Data.Failure(SaveInvoiceError(cause = e)))
            }
        }.flowOn(dispatcher)

    override suspend fun createInvoicePhotoFile(): Data<Uri> = withContext(dispatcher) {
        return@withContext fileDataSource.createFile(
            externalFilesDirectory = Environment.DIRECTORY_PICTURES,
            filePrefix = "invoice_",
            fileSuffix = ".jpg"
        )
    }
}