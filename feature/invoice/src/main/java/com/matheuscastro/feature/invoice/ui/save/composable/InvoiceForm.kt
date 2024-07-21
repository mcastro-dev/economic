package com.matheuscastro.feature.invoice.ui.save.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.matheuscastro.core.ui.composable.FormOutlinedTextField
import com.matheuscastro.core.ui.model.Field
import com.matheuscastro.feature.invoice.ui.save.model.UiInvoice
import com.matheuscastro.feature.invoice.R

@Composable
fun InvoiceForm(
    invoice: UiInvoice,
    isSaving: Boolean,
    onCustomerChanged: (String) -> Unit,
    onTotalChanged: (String) -> Unit,
    onDateChanged: (String) -> Unit,
    onRequestPhotoFile: () -> Unit,
    onPhotoSuccess: () -> Unit,
    onPhotoFailure: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        InvoicePhotoField(
            photoField = invoice.photoField,
            onRequestPhotoFile = onRequestPhotoFile,
            onPhotoSuccess = onPhotoSuccess,
            onPhotoFailure = onPhotoFailure,
            validationErrorMessage = when (invoice.photoField.validationResult) {
                Field.ValidationResult.INVALID -> stringResource(id = R.string.invoice_form_photo_field_error)
                else -> null
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        FormOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = isSaving,
            value = invoice.customerField.content,
            validationErrorMessage = when (invoice.customerField.validationResult) {
                Field.ValidationResult.INVALID -> stringResource(id = R.string.invoice_form_customer_field_error)
                else -> null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            label = {
                Text(stringResource(id = R.string.invoice_form_label_customer))
            },
            onValueChange = { value ->
                onCustomerChanged(value)
            },
        )
        Spacer(modifier = Modifier.height(12.dp))
        FormOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = isSaving,
            value = invoice.totalMoneyField.content,
            validationErrorMessage = when (invoice.totalMoneyField.validationResult) {
                Field.ValidationResult.INVALID -> stringResource(id = R.string.invoice_form_total_field_error)
                else -> null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = {
                Text(stringResource(id = R.string.invoice_form_label_total))
            },
            leadingIcon = {
                Text(invoice.totalCurrency)
            },
            onValueChange = { value ->
                onTotalChanged(value)
            },
        )
        Spacer(modifier = Modifier.height(12.dp))
        FormOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = isSaving,
            value = invoice.dateField.content,
            validationErrorMessage = when (invoice.dateField.validationResult) {
                Field.ValidationResult.INVALID -> stringResource(id = R.string.invoice_form_date_field_error)
                else -> null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = {
                Text(stringResource(id = R.string.invoice_form_label_date))
            },
            placeholder = {
                Text(stringResource(id = R.string.invoice_form_label_date_hint))
            },
            onValueChange = { value ->
                onDateChanged(value)
            },
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}