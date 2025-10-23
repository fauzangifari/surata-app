package com.fauzangifari.surata.ui.screens.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.fauzangifari.domain.model.ReqLetter
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = Blue800,
                shape = RoundedCornerShape(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    contentDescription = "Tambah Surat",
                    tint = White,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
        ) {
            Text("Halo, ${greetings()}!", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = PlusJakartaSans, color = Grey900)

            Spacer(modifier = Modifier.height(4.dp))

            Text("Muhammad Fauzan Gifari", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = PlusJakartaSans, color = Grey900)

            Spacer(modifier = Modifier.height(16.dp))

            ProfileCard()

            SuratSection(navController, viewModel)

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    containerColor = White
                ) {
                    SuratForm(onClose = { showSheet = false }, viewModel = viewModel)
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

            AsyncImage(
                model = "https://avatars.githubusercontent.com/u/77602702?v=4",
                contentDescription = "Profile Picture",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
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
    val state by viewModel.letterState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Riwayat Surat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Grey900,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        when {
            state.isLoading -> {
                // 🌀 Loading shimmer
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        repeat(5) {
                            ShimmerAnimated(modifier = Modifier.padding(bottom = 16.dp))
                        }
                    }
                }
            }

            !state.error.isNullOrBlank() -> {
                // ⚠️ Error state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = "Oops..",
                            color = RedLight,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontFamily = PlusJakartaSans
                        )
                        Text(
                            text = state.error ?: "Terjadi kesalahan",
                            color = Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = PlusJakartaSans
                        )
                    }
                }
            }

            state.data.isNotEmpty() -> {
                // ✅ Data surat tampil
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(state.data) { letter ->
                        CardSurat(
                            tipeSurat = letterTypeMapper(letter.letterType),
                            status = letter.status,
                            isoDateTime = letter.createdAt,
                            onDetailClick = {
                                navController.navigate(Screen.Detail.passId(letter.id)) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }

            else -> {
                // ℹ️ Tidak ada data
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada surat yang dibuat.",
                        color = Grey500
                    )
                }
            }
        }
    }
}


@Composable
fun SuratForm(
    onClose: () -> Unit,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val postState by viewModel.postLetterState.collectAsStateWithLifecycle()

    var description by remember { mutableStateOf("") }
    var selectedSurat by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    // ⏰ Untuk tanggal dan waktu
    var beginDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var beginTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    var isChecked by remember { mutableStateOf(false) }
    var selectedStudents by remember { mutableStateOf(mutableListOf<String>()) }

    LaunchedEffect(postState.isSuccess, postState.error) {
        when {
            postState.isSuccess -> {
                Toast.makeText(context, "Surat berhasil dibuat!", Toast.LENGTH_SHORT).show()

                description = ""
                selectedSurat = ""
                subject = ""
                beginDate = ""
                endDate = ""
                beginTime = ""
                endTime = ""
                isChecked = false
                selectedStudents.clear()

                onClose()
            }
            !postState.error.isNullOrBlank() -> {
                Toast.makeText(context, postState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Buat Surat",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Grey900
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Jenis Surat
        SectionTitle("Informasi Surat")
        DropdownField(
            label = "Jenis Surat",
            items = listOf(
                "Surat Dispensasi",
                "Surat Rekomendasi",
                "Surat Keterangan Aktif",
                "Surat Tugas"
            ),
            placeHolder = "Pilih Jenis Surat",
            onItemSelected = { selectedSurat = it }
        )

        AnimatedVisibility(visible = selectedSurat.isNotBlank()) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                TextInput(
                    label = "Judul Surat",
                    placeholder = "Masukkan judul surat",
                    value = subject,
                    onValueChange = { subject = it },
                    singleLine = true
                )
            }
        }

        // 🧑‍🎓 Surat Dispensasi → Pilih siswa
        if (selectedSurat == "Surat Dispensasi") {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Pilih Siswa")
            MultiPickedField(
                selectedStudents = selectedStudents,
                onSelectedChange = { selectedStudents = it.toMutableList() }
            )
            if (selectedStudents.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Terpilih: ${selectedStudents.joinToString(", ")}",
                    fontSize = 13.sp,
                    color = Grey700
                )
            }
        }

        // 📅 Surat Dispensasi & Surat Tugas → butuh tanggal + jam
        if (selectedSurat == "Surat Dispensasi" || selectedSurat == "Surat Tugas") {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Tanggal & Waktu")

            DateInput(
                label = "Tanggal Mulai",
                onDateSelected = { beginDate = it },
                placeholder = "Pilih Tanggal Mulai"
            )
            Spacer(modifier = Modifier.height(8.dp))
            TimeInput(
                context = context,
                label = "Waktu Mulai",
                placeHolder = "Pilih Waktu Mulai",
                onTimeSelected = { beginTime = it }
            )

            Spacer(modifier = Modifier.height(8.dp))
            DateInput(
                label = "Tanggal Berakhir",
                onDateSelected = { endDate = it },
                placeholder = "Pilih Tanggal Berakhir"
            )
            Spacer(modifier = Modifier.height(8.dp))
            TimeInput(
                context = context,
                label = "Waktu Selesai",
                placeHolder = "Pilih Waktu Selesai",
                onTimeSelected = { endTime = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Keterangan")
        DescriptionInput(
            label = "Keterangan (Opsional)",
            placeholder = "Masukkan keterangan surat",
            value = description,
            onValueChange = { description = it },
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Dokumen Pendukung")
        FileUpload(onFileSelected = { /* TODO: Upload ke Firebase nanti */ })

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Grey200, thickness = 1.dp, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(
            value = if (postState.isLoading) "Mengirim..." else "Buat Surat",
            fontSize = 14,
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.FILLED,
            backgroundColor = if (postState.isLoading) Grey500 else Blue800,
            textColor = White,
            onClick = {
                if (!postState.isLoading) {
                    val beginIso = if (beginDate.isNotEmpty() && beginTime.isNotEmpty()) {
                        toIsoDate(beginDate, beginTime)
                    } else ""
                    val endIso = if (endDate.isNotEmpty() && endTime.isNotEmpty()) {
                        toIsoDate(endDate, endTime)
                    } else ""

                    val mappedLetterType = when (selectedSurat) {
                        "Surat Dispensasi" -> "DISPENSATION"
                        "Surat Rekomendasi" -> "RECOMMENDATION"
                        "Surat Keterangan Aktif" -> "ACTIVE_CERTIFICATE"
                        "Surat Tugas" -> "ASSIGNMENT"
                        else -> selectedSurat
                    }

                    val req = ReqLetter(
                        letterType = mappedLetterType,
                        subject = subject,
                        beginDate = if (beginIso.isNotBlank()) beginIso else null,
                        endDate = if (endIso.isNotBlank()) endIso else null,
                        reason = if (description.isNotBlank()) description else null,
                        isPrinted = isChecked,
                        cc = if (selectedStudents.isNotEmpty()) selectedStudents else emptyList(),
                        attachment = "https://via.placeholder.com/570x589/a409d8/cf57ba.gif"
                    )


                    viewModel.postLetter(req)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )
    }
}

fun toIsoDate(date: String, time: String): String {
    return try {
        val parts = date.split("/")
        val day = parts[0].padStart(2, '0')
        val month = parts[1].padStart(2, '0')
        val year = parts[2]
        val cleanTime = time.trim().replace(" ", "")
        "${year}-${day}-${month}T${cleanTime}:00.000+08:00"
    } catch (e: Exception) {
        ""
    }
}


@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Blue800,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

fun letterTypeMapper(type: String): String {
    return when (type) {
        "DISPENSATION" -> "Surat Dispensasi"
        "RECOMMENDATION" -> "Surat Rekomendasi"
        "ACTIVE_CERTIFICATE" -> "Surat Keterangan Aktif"
        "ASSIGNMENT" -> "Surat Tugas"
        else -> type
    }
}

fun greetings(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "Selamat Pagi"
        in 12..17 -> "Selamat Siang"
        else -> "Selamat Malam"
    }
}

@Composable
fun ShimmerAnimated(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        Grey300.copy(alpha = 0.9f),
        Grey100.copy(alpha = 0.3f),
        Grey300.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            )
        ),
        label = "shimmerAnim"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(0f, 0f),
        end = Offset(x = translateAnim, y = translateAnim)
    )

    ShimmerEffect(brush = brush, modifier = modifier)
}

@Composable
fun ShimmerEffect(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Grey300
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .width(75.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(50))
                            .background(brush)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(50))
                            .background(brush)
                    )
                }

                VerticalDivider(
                    color = Grey300,
                    modifier = Modifier
                        .height(40.dp)
                        .padding(horizontal = 8.dp)
                )

                // Bagian Tanggal
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerEffectPreview() {
    ShimmerEffect(brush = Brush.linearGradient(listOf(Grey300, Grey100, Grey300)), modifier = Modifier.padding(16.dp))
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(navController = rememberNavController(), koinViewModel())
}
