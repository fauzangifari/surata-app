package com.fauzangifari.data.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap

fun savePdfToCache(context: Context, rawResId: Int, fileName: String): File? {
    return try {
        val file = File(context.cacheDir, fileName)
        if (!file.exists()) {
            context.resources.openRawResource(rawResId).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun renderFirstPdfPage(context: Context, file: File): Bitmap? {
    return try {
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(fileDescriptor)
        val page = pdfRenderer.openPage(0)

        val bitmap = createBitmap(page.width, page.height)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        page.close()
        pdfRenderer.close()
        fileDescriptor.close()

        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun openPdfWithIntent(context: Context, file: File) {
    try {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Buka PDF dengan"))
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("PdfUtils", "Failed to open PDF", e)
        Toast.makeText(context, "Gagal membuka PDF", Toast.LENGTH_SHORT).show()
    }
}
