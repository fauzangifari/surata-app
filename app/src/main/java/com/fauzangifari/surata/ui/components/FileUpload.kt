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
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey800
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans

@Composable
fun FileUpload(modifier: Modifier = Modifier, onFileSelected: (Uri?) -> Unit) {
    val context = LocalContext.current
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf<String?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFile = uri
        fileName = uri?.let { getFileName(it, context) }
        onFileSelected(uri)
    }

    Column(
        modifier = modifier
            .fillMaxWidth().height(150.dp)
            .border(
                width = if (selectedFile != null) 1.dp else 1.dp,
                color = if (selectedFile != null) Blue800 else Grey900,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent)
            .clickable { filePickerLauncher.launch("application/pdf") },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (selectedFile == null){
            Image(
                painter = painterResource(id = R.drawable.ic_upload_file_24),
                contentDescription = "Upload Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tekan sini untuk upload",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Grey500,
                fontFamily = PlusJakartaSans
            )
            Text(
                text = "dokumen pendukung",
                fontSize = 14.sp,
                color = Grey500,
                fontFamily = PlusJakartaSans
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Format mendukung: PDF (maks 5mb)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Grey500,
                fontFamily = PlusJakartaSans
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_picture_as_pdf_24),
                contentDescription = "PDF Icon",
                modifier = Modifier.size(40.dp),
                colorFilter = ColorFilter.tint(Grey800)
            )
            Spacer(modifier = Modifier.height(8.dp))
            fileName?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = PlusJakartaSans
                )
            }
        }
    }
}

fun getFileName(uri: Uri, context: Context): String {
    var fileName: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex("_display_name")
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
    }
    return fileName ?: "Unknown File"
}


@Preview(showBackground = true)
@Composable
fun FileUploadPreview() {
    FileUpload(onFileSelected = {})
}
