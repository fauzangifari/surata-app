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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.*
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White
import com.fauzangifari.data.utils.openPdfWithIntent
import com.fauzangifari.data.utils.renderFirstPdfPage
import com.fauzangifari.data.utils.savePdfToCache
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue900
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

    LaunchedEffect(Unit) {
        val file = savePdfToCache(context, R.raw.sample, "sample.pdf")
        if (file != null) {
            bitmap = renderFirstPdfPage(context, file)
            pdfFile = file
        }
    }

    LaunchedEffect(letterId) {
        viewModel.getDetail(letterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detail Surat",
                        fontWeight = FontWeight.Medium,
                        fontFamily = PlusJakartaSans,
                        color = Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(White)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val buttonModifier = Modifier
                        .weight(1f)
                        .height(50.dp)

                    ButtonCustom(
                        modifier = buttonModifier,
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
                        fontSize = 16,
                        textColor = Blue900,
                        onClick = {

                        }
                    )

                    ButtonCustom(
                        modifier = buttonModifier,
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
                        onClick = {
                            pdfFile?.let {
                                openPdfWithIntent(context, it)
                            } ?: Toast.makeText(context, "File PDF belum tersedia", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            when {
                state.isLoading -> {
                }
            }

            Spacer(Modifier.height(16.dp))

            DataSurat(
                image = "",
                name = "Muhammad Fauzan Gifari",
                dateCreated = "12/12/2023",
                letterType = "Surat Keterangan"
            )

            Spacer(Modifier.height(16.dp))

            StatusSurat()

            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                modifier = Modifier.fillMaxWidth().aspectRatio(3f / 4f)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Pratinjau Surat", fontWeight = FontWeight.Medium, fontSize = 16.sp, fontFamily = PlusJakartaSans)

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Preview PDF",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
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
    letterType: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = PlusJakartaSans)
                    Text("Nama Pembuat", fontSize = 12.sp, fontFamily = PlusJakartaSans)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Column {
                    Text("Tanggal Dibuat", fontSize = 12.sp)
                    Text(dateCreated, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
                Spacer(Modifier.weight(1f))
                Column {
                    Text("Jenis Surat", fontSize = 12.sp, fontFamily = PlusJakartaSans)
                    Text(letterType, fontWeight = FontWeight.Medium, fontSize = 14.sp, fontFamily = PlusJakartaSans)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Lampiran", fontSize = 14.sp, fontFamily = PlusJakartaSans)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pdf),
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Dokumen_Pendukung.pdf", fontSize = 14.sp, fontFamily = PlusJakartaSans)
                        Text("2.4 MB", fontSize = 12.sp, color = Color.Gray, fontFamily = PlusJakartaSans)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusSurat() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Status Surat", fontWeight = FontWeight.Medium, fontSize = 16.sp, fontFamily = PlusJakartaSans)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailScreen() {
//    DetailScreen()
}