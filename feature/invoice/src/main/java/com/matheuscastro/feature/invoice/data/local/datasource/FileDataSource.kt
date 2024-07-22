package com.matheuscastro.feature.invoice.data.local.datasource

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.matheuscastro.core.data.Data
import com.matheuscastro.feature.invoice.domain.model.CreateFileError
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

internal interface FileDataSource {
    suspend fun createFile(
        externalFilesDirectory: String,
        filePrefix: String,
        fileSuffix: String
    ): Data<Uri>
}

internal class FileDataSourceImpl @Inject constructor(
    @ApplicationContext
    private val appContext: Context,
    private val dispatcher: CoroutineDispatcher
) : FileDataSource {

    override suspend fun createFile(
        externalFilesDirectory: String,
        filePrefix: String,
        fileSuffix: String
    ): Data<Uri> = withContext(dispatcher) {
        val storageDir: File = appContext.getExternalFilesDir(externalFilesDirectory)
            ?: return@withContext Data.Failure(CreateFileError(cause = null))

        try {
            val file = File.createTempFile(filePrefix, fileSuffix, storageDir)
            val uri = FileProvider.getUriForFile(appContext, appContext.packageName, file)
            Data.Success(uri)

        } catch (e: IOException) {
            Data.Failure(CreateFileError(cause = e))
        }
    }
}