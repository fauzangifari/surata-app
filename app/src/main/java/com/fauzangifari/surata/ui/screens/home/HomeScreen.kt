package com.fauzangifari.surata.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.*
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.items
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = Blue800,
                shape = RoundedCornerShape(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    contentDescription = "Tambah Surat",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp)
        ) {
            Text("Halo,", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = PlusJakartaSans, color = Grey900)
            Text("Muhammad Fauzan Gifari", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = PlusJakartaSans, color = Grey900)

            Spacer(modifier = Modifier.height(16.dp))

            ProfileCard()

            SuratSection(navController, viewModel)

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                ) {
                    SuratForm(onClose = { showSheet = false })
                }
            }
        }
    }
}

@Composable
fun ProfileCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Blue800),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Grey500)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Siswa Aktif", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("fauzan@sman1samarinda.sch.id", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SuratSection(
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Riwayat Surat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Grey900,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            !state.error.isNullOrBlank() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "Terjadi kesalahan",
                        color = RedDark
                    )
                }
            }

            state.letters.isNotEmpty() -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(state.letters) { letter ->
                        CardSurat(
                            tipeSurat = letter.letterType,
                            status = letter.status,
                            isoDateTime = letter.createdAt,
                            onDetailClick = {
                                navController.navigate(Screen.Detail.passId(letter.id))
                            }
                        )
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada surat.",
                        color = Grey500
                    )
                }
            }
        }
    }
}



@Composable
fun SuratForm(onClose: () -> Unit) {
    val context = LocalContext.current
    var description by remember { mutableStateOf("") }
    var selectedSurat by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Buat Surat", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        DropdownField(
            label = "Jenis Surat",
            items = listOf("Surat Dispensasi", "Surat Rekomendasi", "Surat Keterangan Aktif", "Surat Tugas"),
            placeHolder = "Pilih Jenis Surat",
            onItemSelected = { selectedSurat = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedSurat) {
            "Surat Dispensasi", "Surat Tugas" -> {
                DateInput(label = "Tanggal Mulai", onDateSelected = {}, placeholder = "Pilih Tanggal Mulai", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                DateInput(label = "Tanggal Berakhir", onDateSelected = {}, placeholder = "Pilih Tanggal Berakhir", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                if (selectedSurat == "Surat Dispensasi") {
                    TimeInput(context = context, label = "Waktu Mulai", placeHolder = "Pilih Waktu Mulai", onTimeSelected = {}, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    TimeInput(context = context, label = "Waktu Selesai", placeHolder = "Pilih Waktu Selesai", onTimeSelected = {}, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionInput(
            label = "Keterangan (Opsional)",
            placeholder = "Masukkan keterangan surat",
            value = description,
            onValueChange = { description = it },
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        FileUpload(onFileSelected = {})

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ambil Surat di Tempat", fontSize = 14.sp, color = Grey900)
            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier.scale(0.75f),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Blue900,
                    uncheckedThumbColor = Grey500
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ButtonCustom(
            value = "Buat Surat",
            fontSize = 14,
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.FILLED,
            backgroundColor = Blue800,
            textColor = White,
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(navController = rememberNavController(), koinViewModel())
}
