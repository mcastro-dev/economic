package com.matheuscastro.feature.invoice.ui.save.composable

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.matheuscastro.feature.invoice.ui.save.model.PhotoField
import com.matheuscastro.feature.invoice.R

private typealias CameraLauncher = ManagedActivityResultLauncher<Uri, Boolean>

@Composable
fun InvoicePhotoField(
    photoField: PhotoField,
    onRequestPhotoFile: () -> Unit,
    onPhotoSuccess: () -> Unit,
    onPhotoFailure: () -> Unit,
    modifier: Modifier = Modifier,
    validationErrorMessage: String? = null,
) {
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                onPhotoSuccess()
            } else {
                onPhotoFailure()
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isSuccess ->
            if (isSuccess) {
                tryLaunchCamera(
                    cameraLauncher = cameraLauncher,
                    photoField = photoField,
                    onMissingUri = onRequestPhotoFile
                )
            }
        }
    )

    tryLaunchCamera(
        cameraLauncher = cameraLauncher,
        photoField = photoField,
        onMissingUri = null
    )

    Column(
        modifier = modifier
    ) {
        ContentCard(
            modifier = modifier,
            photoField = photoField,
            onClick = {
                when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                    PackageManager.PERMISSION_GRANTED -> {
                        tryLaunchCamera(
                            cameraLauncher = cameraLauncher,
                            photoField = photoField,
                            onMissingUri = onRequestPhotoFile
                        )
                    }
                    else -> {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }
        )
        validationErrorMessage?.let { errorMessage ->
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ContentCard(
    photoField: PhotoField,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (photoField.content.isNotEmpty()) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberImagePainter(Uri.parse(photoField.content)),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = Icons.Filled.PhotoCamera,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = stringResource(R.string.invoice_form_label_photo_description)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.invoice_form_label_photo)
            )
        }
    }
}

private fun tryLaunchCamera(
    cameraLauncher: CameraLauncher,
    photoField: PhotoField,
    onMissingUri: (() -> Unit)?,
) {
    photoField.cameraLauncherUri?.let { uri ->
        cameraLauncher.launch(uri)
    } ?: onMissingUri?.invoke()
}