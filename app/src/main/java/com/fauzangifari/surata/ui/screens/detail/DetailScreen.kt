package com.fauzangifari.surata.ui.screens.detail

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.fauzangifari.surata.ui.theme.Grey700
import org.koin.androidx.compose.koinViewModel
import java.io.File

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
                name = data?.applicantName ?: "-",
                dateCreated = data?.createdAt ?: "-",
                letterType = letterMapper(data?.letterType ?: "-"),
                letterNumber = data?.letterNumber ?: "-",
                letterStatus = letterStatusToIndonesian(data?.status ?: "-")
            )

            Spacer(Modifier.height(20.dp))

            StatusSurat(status = data?.status ?: "-")

            Spacer(Modifier.height(20.dp))

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
    letterStatus: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    Text(name.ifBlank { "-" }, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Pembuat Surat", fontSize = 12.sp, color = Grey700)
                }
            }

            Divider(Modifier.padding(vertical = 12.dp))

            InfoRow("Tanggal Dibuat", dateCreated)
            InfoRow("Jenis Surat", letterType)
            InfoRow("Nomor Surat", letterNumber)
            InfoRow("Status", letterStatus)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Lampiran", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pdf),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Dokumen_Pendukung.pdf", fontSize = 14.sp)
                        Text("2.4 MB", fontSize = 12.sp, color = Grey700)
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
    "pending" -> Color(0xFFFFA500)
    "approved" -> Color(0xFF4CAF50)
    "rejected" -> Color(0xFFF44336)
    "process" -> Color(0xFF2196F3)
    "revision" -> Color(0xFFFFC107)
    "cancelled" -> Color(0xFF9E9E9E)
    else -> Grey700
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


@Preview(showBackground = true)
@Composable
fun PreviewDetailScreen() {
//    DetailScreen()
}