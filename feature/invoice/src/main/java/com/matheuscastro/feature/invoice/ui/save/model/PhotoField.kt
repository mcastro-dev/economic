package com.matheuscastro.feature.invoice.ui.save.model

import android.net.Uri
import com.matheuscastro.core.ui.model.Field

data class PhotoField(
    override val content: String,
    override val validationResult: ValidationResult?,
    val cameraLauncherUri: Uri?
) : Field<String>(content, validationResult) {

    override fun validate(): PhotoField {
        // TODO mat: validate if file is valid
        return PhotoField(
            content = content,
            validationResult = if (content.isNotBlank()) {
                ValidationResult.VALID
            } else {
                ValidationResult.INVALID
            },
            cameraLauncherUri = cameraLauncherUri
        )
    }
}

fun Uri.toPhotoField(): PhotoField =
    PhotoField(content = this.toString(), validationResult = null, cameraLauncherUri = null)