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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.*
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White
import com.fauzangifari.surata.utils.openPdfWithIntent
import com.fauzangifari.surata.utils.renderFirstPdfPage
import com.fauzangifari.surata.utils.savePdfToCache
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(Unit) {
        val file = savePdfToCache(context, R.raw.sample, "sample.pdf")
        if (file != null) {
            bitmap = renderFirstPdfPage(context, file)
            pdfFile = file
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detail Surat", fontWeight = FontWeight.Medium, fontFamily = PlusJakartaSans, color = Grey900)},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
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
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Collapse(label = "Data Surat")

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
