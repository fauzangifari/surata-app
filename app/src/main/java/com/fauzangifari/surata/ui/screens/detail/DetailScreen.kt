package com.fauzangifari.surata.ui.screens.detail

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                        color = Grey900
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
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        state.error.isNotEmpty() -> {
                            Text(
                                text = state.error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        else -> {
                            Collapse(
                                label = "Data Surat",
                                namaPembuat = state.data?.applicantName ?: "--",
                                tanggalSurat = state.data?.createdAt ?: "--",
                                jenisSurat = state.data?.letterType ?: "--",
                                dokumenNama = "SP Lomba HUT SMA Negeri 3 Samarinda 2024.pdf",
                                dokumenTipe = "PDF"
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(3f / 4f)
                                    .background(White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = White),
                            ) {
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
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                ButtonCustom(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    value = "Cetak PDF",
                    fontSize = 14,
                    buttonType = ButtonType.REGULAR,
                    buttonStyle = ButtonStyle.FILLED,
                    onClick = {
                        pdfFile?.let {
                            openPdfWithIntent(context, it)
                        } ?: Toast.makeText(context, "File PDF belum tersedia", Toast.LENGTH_SHORT).show()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_print),
                            contentDescription = "Cetak",
                            tint = White
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDetailScreen() {
//    DetailScreen()
}
