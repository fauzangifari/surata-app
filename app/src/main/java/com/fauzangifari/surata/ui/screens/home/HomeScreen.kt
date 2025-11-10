package com.fauzangifari.surata.ui.screens.home

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
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    val userName by viewModel.userNameState.collectAsStateWithLifecycle()
    val userEmail by viewModel.userEmailState.collectAsStateWithLifecycle()

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf<ToastType>(ToastType.SUCCESS) }
    var toastVisible by remember { mutableStateOf(false) }

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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
            ) {
                Text("Halo, ${greetings()}!", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = PlusJakartaSans, color = Grey900)

                Spacer(modifier = Modifier.height(4.dp))

                Text(userName ?: "-", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = PlusJakartaSans, color = Grey900)

                Spacer(modifier = Modifier.height(16.dp))

                ProfileCard(email = userEmail)

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                ) {
                    SuratSection(navController, viewModel)
                }

                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = sheetState,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        containerColor = White
                    ) {
                        SuratForm(
                            onClose = { showSheet = false },
                            viewModel = viewModel,
                            onShowToast = { message, type ->
                                toastMessage = message
                                toastType = type
                                toastVisible = true
                            }
                        )
                    }
                }
            }

            toastMessage?.let { msg ->
                CustomToast(
                    message = msg,
                    type = toastType,
                    visible = toastVisible,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
            }

            LaunchedEffect(toastVisible) {
                if (toastVisible) {
                    kotlinx.coroutines.delay(3000)
                    toastVisible = false
                }
            }
        }
    }
}

@Composable
fun ProfileCard(email: String?) {
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
                Text(email ?: "-", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

 @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuratSection(
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val state by viewModel.letterState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Riwayat Surat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Grey900,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.refreshLetters() },
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                when {
                    state.isLoading -> {
                        items(5) { idx ->
                            ShimmerAnimated(modifier = Modifier.padding(bottom = 0.dp))
                        }
                    }

                    !state.error.isNullOrBlank() -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 48.dp),
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
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tarik ke bawah untuk menyegarkan",
                                        color = Grey500,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    state.data.isNotEmpty() -> {
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

                    else -> {
                        item {
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
        }
    }
}


@Composable
fun SuratForm(
    onClose: () -> Unit,
    viewModel: HomeViewModel,
    onShowToast: (String, ToastType) -> Unit
) {
    val context = LocalContext.current
    val postState by viewModel.postLetterState.collectAsStateWithLifecycle()
    val studentState by viewModel.studentState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()

    // Determine if form should be disabled
    val isFormDisabled = postState.isLoading || uploadState.isUploading

    LaunchedEffect(postState.isSuccess, postState.error) {
        when {
            postState.isSuccess -> {
                onShowToast("Surat berhasil dibuat!", ToastType.SUCCESS)
                viewModel.resetForm()
                kotlinx.coroutines.delay(100)
                onClose()
            }
            !postState.error.isNullOrBlank() -> {
                onShowToast(postState.error ?: "Terjadi kesalahan", ToastType.ERROR)
            }
        }
    }

    LaunchedEffect(uploadState.error) {
        if (!uploadState.error.isNullOrBlank()) {
            onShowToast(uploadState.error ?: "Gagal upload file", ToastType.ERROR)
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
            color = Grey900,
            fontFamily = PlusJakartaSans
        )

        Spacer(modifier = Modifier.height(20.dp))

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
            onItemSelected = { viewModel.updateFormField(FormField.SELECTED_LETTER, it) }
        )

        AnimatedVisibility(visible = formState.selectedLetter.isNotBlank()) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                TextInput(
                    label = "Judul Surat",
                    placeholder = "Masukkan judul surat",
                    value = formState.subject,
                    onValueChange = { viewModel.updateFormField(FormField.SUBJECT, it) },
                    singleLine = true,
                    isEnabled = !isFormDisabled
                )
            }
        }

        if (formState.selectedLetter == "Surat Dispensasi") {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Pilih Siswa (Opsional)")

            MultiPickedField(
                students = studentState.data,
                selectedStudentIds = formState.selectedStudentIds,
                isLoading = studentState.isLoading,
                onSelectedChange = { viewModel.updateFormField(FormField.SELECTED_STUDENTS, it) }
            )

            if (formState.selectedStudentIds.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                val selectedNames = studentState.data
                    .filter { it.id in formState.selectedStudentIds }
                    .mapNotNull { it.name }
                Text(
                    text = "Terpilih: ${selectedNames.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = Grey700,
                    fontFamily = PlusJakartaSans
                )
            }
        }

        if (formState.selectedLetter in listOf("Surat Dispensasi", "Surat Tugas")) {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Tanggal & Waktu")

            DateInput(
                label = "Tanggal Mulai",
                value = formState.beginDate,
                error = formState.beginDateError,
                onDateSelected = { viewModel.updateFormField(FormField.BEGIN_DATE, it) },
                placeholder = "Pilih Tanggal Mulai"
            )

            Spacer(modifier = Modifier.height(12.dp))

            TimeInput(
                context = context,
                label = "Waktu Mulai",
                value = formState.beginTime,
                error = formState.beginTimeError,
                placeHolder = "Pilih Waktu Mulai",
                onTimeSelected = { viewModel.updateFormField(FormField.BEGIN_TIME, it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            DateInput(
                label = "Tanggal Berakhir",
                value = formState.endDate,
                error = formState.endDateError,
                onDateSelected = { viewModel.updateFormField(FormField.END_DATE, it) },
                placeholder = "Pilih Tanggal Berakhir"
            )

            Spacer(modifier = Modifier.height(12.dp))

            TimeInput(
                context = context,
                label = "Waktu Selesai",
                value = formState.endTime,
                error = formState.endTimeError,
                placeHolder = "Pilih Waktu Selesai",
                onTimeSelected = { viewModel.updateFormField(FormField.END_TIME, it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Keterangan")
        DescriptionInput(
            label = "Keterangan (Opsional)",
            placeholder = "Masukkan keterangan surat",
            value = formState.description,
            onValueChange = { viewModel.updateFormField(FormField.DESCRIPTION, it) },
            maxLines = 5,
            isEnabled = !isFormDisabled
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Dokumen Pendukung")
        FileUpload(
            onFileSelected = { fileData ->
                if (fileData != null) {
                    val maxSize = 5 * 1024 * 1024 // 5MB
                    if (fileData.size > maxSize) {
                        onShowToast("Ukuran file maksimal 5MB", ToastType.ERROR)
                        return@FileUpload
                    }
                    viewModel.updateFormField(FormField.FILE_URI, fileData.uri)
                    viewModel.uploadFile(
                        context = context,
                        uri = fileData.uri,
                        fileName = fileData.name,
                        fileSize = fileData.size,
                        mimeType = fileData.mimeType
                    )
                }
            },
            isUploading = uploadState.isUploading,
            uploadProgress = uploadState.progress,
            errorMessage = formState.fileError
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            color = Grey200,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ambil Surat di Tempat",
                fontSize = 14.sp,
                color = if (isFormDisabled) Grey400 else Grey900,
                fontFamily = PlusJakartaSans
            )
            Switch(
                checked = formState.isPrinted,
                onCheckedChange = { viewModel.updateFormField(FormField.IS_PRINTED, it) },
                enabled = !isFormDisabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Blue800,
                    checkedTrackColor = Blue800.copy(alpha = 0.5f),
                    uncheckedThumbColor = Grey300,
                    uncheckedTrackColor = Grey200,
                    disabledCheckedThumbColor = Grey400,
                    disabledCheckedTrackColor = Grey300,
                    disabledUncheckedThumbColor = Grey300,
                    disabledUncheckedTrackColor = Grey200
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ButtonCustom(
            value = if (postState.isLoading) "Mengirim..." else "Buat Surat",
            fontSize = 14,
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.FILLED,
            backgroundColor = if (isFormDisabled) Grey400 else Blue800,
            textColor = White,
            onClick = {
                if (!isFormDisabled) {
                    viewModel.submitLetter()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
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
    } catch (_: Exception) {
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
        "ACTIVE_STATEMENT" -> "Surat Keterangan Aktif"
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
