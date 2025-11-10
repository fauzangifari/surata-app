package com.fauzangifari.surata.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey800
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans

data class FileData(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String
)

@Composable
fun FileUpload(
    modifier: Modifier = Modifier,
    onFileSelected: (FileData?) -> Unit,
    isUploading: Boolean = false,
    uploadProgress: Int = 0,
    errorMessage: String? = null
) {
    val context = LocalContext.current
    var selectedFile by remember { mutableStateOf<FileData?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = getFileName(uri, context)
            val fileSize = getFileSize(uri, context)
            val mimeType = context.contentResolver.getType(uri) ?: "application/pdf"

            val fileData = FileData(uri, fileName, fileSize, mimeType)
            selectedFile = fileData
            onFileSelected(fileData)
        }
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(
                    width = 1.dp,
                    color = when {
                        errorMessage != null -> Color.Red
                        selectedFile != null -> Blue800
                        else -> Grey900
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.Transparent)
                .clickable(enabled = !isUploading) {
                    filePickerLauncher.launch("application/pdf")
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                isUploading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { uploadProgress / 100f },
                            modifier = Modifier.size(40.dp),
                            color = Blue800,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mengupload... $uploadProgress%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Grey900,
                            fontFamily = PlusJakartaSans
                        )
                    }
                }
                selectedFile != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_picture_as_pdf_24),
                            contentDescription = "PDF Icon",
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(Grey800)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedFile!!.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Black,
                            fontFamily = PlusJakartaSans
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatFileSize(selectedFile!!.size),
                            fontSize = 12.sp,
                            color = Grey500,
                            fontFamily = PlusJakartaSans
                        )
                    }
                }
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_upload_file_24),
                            contentDescription = "Upload Icon",
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(Grey500)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tekan sini untuk upload",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Grey900,
                            fontFamily = PlusJakartaSans
                        )
                        Text(
                            text = "dokumen pendukung",
                            fontSize = 14.sp,
                            color = Grey900,
                            fontFamily = PlusJakartaSans
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Format mendukung: PDF (maks 5mb)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Grey500,
                            fontFamily = PlusJakartaSans
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color.Red,
                fontFamily = PlusJakartaSans
            )
        }
    }
}

fun getFileName(uri: Uri, context: Context): String {
    var fileName = "file.pdf"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex("_display_name")
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
    }
    return fileName
}

fun getFileSize(uri: Uri, context: Context): Long {
    var fileSize = 0L
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val sizeIndex = it.getColumnIndex("_size")
            if (sizeIndex != -1) {
                fileSize = it.getLong(sizeIndex)
            }
        }
    }
    return fileSize
}

fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${size / 1024} KB"
        else -> "${size / (1024 * 1024)} MB"
    }
}

@Preview(showBackground = true)
@Composable
fun FileUploadPreview() {
    FileUpload(onFileSelected = {})
}
