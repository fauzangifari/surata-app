package com.fauzangifari.surata.ui.screens.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.*
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White
import com.fauzangifari.data.utils.openPdfWithIntent
import com.fauzangifari.data.utils.renderFirstPdfPage
import com.fauzangifari.data.utils.savePdfToCache
import com.fauzangifari.surata.ui.theme.BackgroundLight
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue900
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey700
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.GreenDark
import com.fauzangifari.surata.ui.theme.GreenLight
import com.fauzangifari.surata.ui.theme.Grey200
import com.fauzangifari.surata.ui.theme.RedDark
import com.fauzangifari.surata.ui.theme.RedLight
import com.fauzangifari.surata.ui.theme.YellowDark
import com.fauzangifari.surata.ui.theme.YellowLight
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    letterId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfFile by remember { mutableStateOf<File?>(null) }

    val viewModel: DetailViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val userName by viewModel.userNameState.collectAsStateWithLifecycle()

    // Load sample PDF untuk preview
    LaunchedEffect(Unit) {
        val file = savePdfToCache(context, R.raw.sample, "sample.pdf")
        if (file != null) {
            bitmap = renderFirstPdfPage(context, file)
            pdfFile = file
        }
    }

    // Panggil API detail surat
    LaunchedEffect(letterId) {
        viewModel.getDetail(letterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detail Surat",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PlusJakartaSans,
                        color = Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ButtonCustom(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        value = "Bagikan",
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_share),
                                contentDescription = "Tombol Bagikan",
                                tint = Blue900
                            )
                        },
                        buttonType = ButtonType.REGULAR,
                        buttonStyle = ButtonStyle.OUTLINED,
                        textColor = Blue900,
                        fontSize = 16,
                        onClick = { /* TODO: Implement share */ }
                    )

                    ButtonCustom(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        value = "Cetak PDF",
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_print),
                                contentDescription = "Cetak",
                                tint = White
                            )
                        },
                        buttonType = ButtonType.REGULAR,
                        buttonStyle = ButtonStyle.FILLED,
                        fontSize = 16,
                        textColor = White,
                        onClick = {
                            pdfFile?.let {
                                openPdfWithIntent(context, it)
                            } ?: Toast.makeText(
                                context,
                                "File PDF belum tersedia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(scrollState)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(20.dp))
            }

            val data = state.data
            DataSurat(
                image = "https://avatars.githubusercontent.com/u/77602702?v=4",
                name = userName ?: data?.applicantName ?: "-",
                dateCreated = data?.createdAt ?: "-",
                letterType = letterMapper(data?.letterType ?: "-"),
                letterNumber = data?.letterNumber ?: "-",
                letterStatus = letterStatusToIndonesian(data?.status ?: "-"),
                ccList = data?.cc ?: emptyList(),
                attachment = data?.attachment,
                reason = data?.reason
            )

            Spacer(Modifier.height(20.dp))

//            StatusSurat(status = data?.status ?: "-")
//
//            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Pratinjau Surat",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        fontFamily = PlusJakartaSans
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Preview PDF",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Text("PDF tidak tersedia atau gagal dimuat.")
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun DataSurat(
    image: String,
    name: String,
    dateCreated: String,
    letterType: String,
    letterNumber: String,
    letterStatus: String,
    ccList: List<String>,
    attachment: String?,
    reason: String?
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Profile Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = image,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        name.ifBlank { "-" },
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = PlusJakartaSans
                    )
                    Text(
                        "Pembuat Surat",
                        fontSize = 12.sp,
                        color = Grey700,
                        fontFamily = PlusJakartaSans
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color(0xFFE0E0E0)
            )

            // Info Section
            InfoRow("Waktu dan Tanggal", formatDateTime(dateCreated))
            InfoRow("Jenis Surat", letterType)
            InfoRow("Nomor Surat", letterNumber)

            // Status Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status",
                    fontSize = 12.sp,
                    fontFamily = PlusJakartaSans,
                    color = Grey700,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = getStatusBackgroundColor(letterStatus).copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = letterStatus.ifBlank { "-" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = getStatusColor(letterStatus),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontFamily = PlusJakartaSans
                    )
                }
            }

            // Reason Section (if available)
            if (!reason.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Alasan/Keterangan",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    fontFamily = PlusJakartaSans,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = reason,
                        fontSize = 13.sp,
                        color = Black,
                        fontFamily = PlusJakartaSans,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // CC Section (only for Surat Dispensasi)
            if (letterType == "Surat Dispensasi" && ccList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_people),
                        contentDescription = null,
                        tint = Blue900,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Penerima Tembusan (CC)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        fontFamily = PlusJakartaSans,
                        color = Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ccList.forEachIndexed { index, cc ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Blue900)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = cc,
                                    fontSize = 13.sp,
                                    color = Black,
                                    fontFamily = PlusJakartaSans
                                )
                            }
                        }
                    }
                }
            }

            // Attachment Section
            if (!attachment.isNullOrBlank()) {
                val context = LocalContext.current
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_attachment),
                        contentDescription = null,
                        tint = Blue900,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Dokumen Pendukung",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        fontFamily = PlusJakartaSans,
                        color = Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(attachment)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Tidak dapat membuka dokumen",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pdf),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Lampiran",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = PlusJakartaSans,
                                color = Black,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap untuk membuka",
                                fontSize = 12.sp,
                                color = Blue900,
                                fontFamily = PlusJakartaSans
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_open_in_new),
                            contentDescription = "Buka dokumen",
                            tint = Blue900,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontFamily = PlusJakartaSans,
            color = Grey700,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value.ifBlank { "-" },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Black,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
    }
}

@Composable
fun StatusSurat(status: String) {
    val color = getStatusColor(status)
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Status Surat", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    letterStatusToIndonesian(status),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = color
                )
            }
        }
    }
}

fun getStatusColor(status: String): Color = when (status.lowercase()) {
    "menunggu", "pending" -> Grey900
    "disetujui", "approved" -> GreenDark
    "ditolak", "rejected" -> RedDark
    "diproses", "process" -> YellowDark
    "revisi", "revision" -> YellowDark
    "dibatalkan", "cancelled" -> Grey900
    else -> Grey900
}

fun getStatusBackgroundColor(status: String): Color = when (status.lowercase()) {
    "menunggu", "pending" -> Grey500
    "disetujui", "approved" -> GreenLight
    "ditolak", "rejected" -> RedLight
    "diproses", "process" -> YellowLight
    "revisi", "revision" -> YellowLight
    "dibatalkan", "cancelled" -> Grey500
    else -> Grey500
}

fun letterStatusToIndonesian(status: String): String = when (status.lowercase()) {
    "pending" -> "Menunggu"
    "approved" -> "Disetujui"
    "rejected" -> "Ditolak"
    "process" -> "Diproses"
    "revision" -> "Revisi"
    "cancelled" -> "Dibatalkan"
    else -> "-"
}

fun letterMapper(letterType: String): String = when (letterType.lowercase()) {
    "recomendation" -> "Surat Rekomendasi"
    "assignment" -> "Surat Tugas"
    "active_statement" -> "Surat Keterangan Aktif"
    "dispensation" -> "Surat Dispensasi"
    else -> "-"
}

fun formatDateTime(isoDate: String): String {
    return try {
        if (isoDate.isBlank() || isoDate == "-") return "-"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("Asia/Singapore")

        val date = inputFormat.parse(isoDate)

        if (date != null) {
            val outputFormat = SimpleDateFormat("HH:mm, dd MMMM yyyy", Locale("id", "ID"))
            outputFormat.timeZone = TimeZone.getTimeZone("Asia/Singapore")
            outputFormat.format(date)
        } else {
            "-"
        }
    } catch (e: Exception) {
        android.util.Log.e("DateFormat", "Error formatting date: ${e.message}")
        "-"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailScreen() {
//    DetailScreen()
}

// Helper function to extract file name from URL
fun extractFileName(url: String): String {
    return try {
        val uri = Uri.parse(url)
        val path = uri.path ?: url
        val fileName = path.split("/").lastOrNull() ?: "Dokumen"

        // Decode URL encoded characters
        Uri.decode(fileName).let {
            // If file name is too generic or encoded, return a readable name
            if (it.length > 50) {
                it.take(47) + "..."
            } else {
                it
            }
        }
    } catch (e: Exception) {
        "Dokumen Pendukung"
    }
}
