package com.matheuscastro.feature.invoice.domain.interactor

import android.net.Uri
import androidx.paging.PagingData
import com.matheuscastro.core.data.Data
import com.matheuscastro.core.domain.model.Currency
import com.matheuscastro.core.domain.model.InvalidIdError
import com.matheuscastro.core.domain.model.Money
import com.matheuscastro.core.domain.model.UId
import com.matheuscastro.feature.invoice.domain.interactor.InvoiceInteractorImpl.Companion.PAGE_SIZE
import com.matheuscastro.feature.invoice.domain.model.Invoice
import com.matheuscastro.feature.invoice.domain.model.InvoiceFilter
import com.matheuscastro.feature.invoice.domain.model.InvoiceNotFoundError
import com.matheuscastro.feature.invoice.domain.repository.InvoiceFilterRepository
import com.matheuscastro.feature.invoice.domain.repository.InvoiceRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.LocalDate

internal class InvoiceInteractorImplTest {

    private val invoiceRepository: InvoiceRepository = mock()
    private val invoiceFilterRepository: InvoiceFilterRepository = mock()
    private val sut = InvoiceInteractorImpl(
        invoiceRepository, invoiceFilterRepository
    )

    @Test
    fun `GIVEN filter data success and invoice pages WHEN observe invoice pages THEN return pages`() = runTest {
        val filterData = Data.Success(
            InvoiceFilter(date = LocalDate.of(2024, 1, 1))
        )
        val invoices: List<Invoice> = mock()
        val pagingData = PagingData.from(invoices)
        `when`(invoiceFilterRepository.observeInvoiceFilter()).thenReturn(flowOf(filterData))
        `when`(invoiceRepository.observeInvoicePages(PAGE_SIZE)).thenReturn(flowOf(pagingData))

        val actual = sut.observeInvoicePages().first()

        assertEquals(pagingData, actual)
    }

    @Test
    fun `GIVEN invalid invoice id WHEN observe invoice THEN return failure`() = runTest {
        val invalidInvoiceId = UId.invalid()

        val actual = sut.observeInvoice(invalidInvoiceId).first()

        val expected = Data.Failure(InvalidIdError)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN valid invoice id and invoice found WHEN observe invoice THEN return success`() = runTest {
        val validInvoiceId = UId(1)
        val invoice = Invoice(
            id = validInvoiceId,
            customer = "Customer",
            date = LocalDate.of(2024, 1, 1),
            total = Money(100, Currency.EUR),
            photoUri = mock()
        )
        val invoiceData: Data<Invoice?> = Data.Success(invoice)
        `when`(invoiceRepository.observeInvoice(validInvoiceId)).thenReturn(flowOf(invoiceData))

        val actual = sut.observeInvoice(validInvoiceId).first()

        val expected = Data.Success(invoice)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN valid invoice id but invoice not found WHEN observe invoice THEN return failure`() = runTest {
        val validInvoiceId = UId(1)
        val invoice: Invoice? = null
        val invoiceData: Data<Invoice?> = Data.Success(invoice)
        `when`(invoiceRepository.observeInvoice(validInvoiceId)).thenReturn(flowOf(invoiceData))

        val actual = sut.observeInvoice(validInvoiceId).first()

        val expected = Data.Failure(InvoiceNotFoundError)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN invoice WHEN save invoice THEN forward to repository`() = runTest {
        val invoice = Invoice(
            id = UId(1),
            customer = "Customer",
            date = LocalDate.of(2024, 1, 1),
            total = Money(100, Currency.EUR),
            photoUri = mock()
        )
        `when`(invoiceRepository.saveInvoice(invoice)).thenReturn(flowOf(Data.Success(Unit)))

        val actual = sut.saveInvoice(invoice).first()

        val expected = Data.Success(Unit)
        assertEquals(expected, actual)
        verify(invoiceRepository).saveInvoice(invoice)
    }

    @Test
    fun `WHEN prepare invoice photo file THEN forward to repository`() = runTest {
        val uriData: Data<Uri> = Data.Success(mock())
        `when`(invoiceRepository.createInvoicePhotoFile()).thenReturn(uriData)

        val actual = sut.prepareInvoicePhotoFile()

        assertEquals(uriData, actual)
        verify(invoiceRepository).createInvoicePhotoFile()
    }
}