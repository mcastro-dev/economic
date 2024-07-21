package com.matheuscastro.feature.invoice.domain.model

import com.matheuscastro.core.domain.model.Error

object InvoiceNotFoundError : Error(cause = null)
class SaveInvoiceError(cause: Throwable?) : Error(cause)

class CreateFileError(cause: Throwable?) : Error(cause)