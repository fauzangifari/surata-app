package com.fauzangifari.surata.ui.screens.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.fauzangifari.data.utils.openPdfUrl
import com.fauzangifari.data.utils.downloadPdfFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import com.fauzangifari.domain.model.History
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.surata.ui.theme.BackgroundLight
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue900
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey700
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.GreenDark
import com.fauzangifari.surata.ui.theme.GreenLight
import com.fauzangifari.surata.ui.theme.Grey200
import com.fauzangifari.surata.ui.theme.Grey300
import com.fauzangifari.surata.ui.theme.Grey50
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
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var isLoadingPdf by remember { mutableStateOf(false) }
    var pdfUrl by remember { mutableStateOf<String?>(null) }

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.SUCCESS) }
    var toastVisible by remember { mutableStateOf(false) }

    val viewModel: DetailViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val userName by viewModel.userNameState.collectAsStateWithLifecycle()

    LaunchedEffect(letterId) {
        viewModel.getDetail(letterId)
    }

    LaunchedEffect(state.data) {
        val data = state.data
        if (data != null) {
            val isApproved = data.status.lowercase() == "approved"

            if (isApproved && data.letterContent.isNotBlank()) {
                pdfUrl = data.letterContent
                isLoadingPdf = true

                withContext(Dispatchers.IO) {
                    try {
                        val file = downloadPdfFromUrl(context, data.letterContent, "letter_${data.id}.pdf")
                        if (file != null) {
                            bitmap = renderFirstPdfPage(context, file)
                            pdfFile = file
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        isLoadingPdf = false
                    }
                }
            }
        } else {
            // Ketika data surat tidak tersedia
            bitmap = null
            pdfFile = null
            pdfUrl = null
            isLoadingPdf = false
        }
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
            val data = state.data
            val isRevision = data?.status?.lowercase() == "revision"

            BottomAppBar(containerColor = White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isRevision) {
                        ButtonCustom(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            value = "Perbaiki Surat",
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_edit),
                                    contentDescription = "Tombol Perbaiki Surat",
                                    tint = White
                                )
                            },
                            buttonType = ButtonType.REGULAR,
                            buttonStyle = ButtonStyle.FILLED,
                            textColor = White,
                            fontSize = 16,
                            onClick = { viewModel.showRevisionDialog() }
                        )
                    } else {
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
                            onClick = {
                                val isApproved = data?.status?.lowercase() == "approved"

                                if (isApproved && !pdfUrl.isNullOrBlank()) {
                                    coroutineScope.launch {
                                        try {
                                            Toast.makeText(
                                                context,
                                                "Menyiapkan file untuk dibagikan...",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            withContext(Dispatchers.IO) {
                                                val file = if (pdfFile != null) {
                                                    pdfFile
                                                } else {
                                                    downloadPdfFromUrl(context, pdfUrl!!, "letter_${data?.id}.pdf")
                                                }

                                                if (file != null) {
                                                    withContext(Dispatchers.Main) {
                                                        sharePdfFile(context, file, data)
                                                    }
                                                } else {
                                                    withContext(Dispatchers.Main) {
                                                        Toast.makeText(
                                                            context,
                                                            "Gagal menyiapkan file untuk dibagikan",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                "Terjadi kesalahan saat membagikan file",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    // Share info surat untuk surat yang belum approved
                                    shareLetterInfo(context, data)
                                }
                            }
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
                                val isApproved = data?.status?.lowercase() == "approved"

                                if (isApproved && !pdfUrl.isNullOrBlank()) {
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "Mengunduh PDF...",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        openPdfUrl(context, pdfUrl!!, "letter_${data?.id}.pdf")
                                    }
                                } else if (pdfFile != null) {
                                    openPdfWithIntent(context, pdfFile!!)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "File PDF belum tersedia",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }
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
                reason = data?.reason,
                viewModel = viewModel
            )

            Spacer(Modifier.height(20.dp))

            if (data?.history?.isNotEmpty() == true) {
                StatusTimeline(historyList = data.history)
                Spacer(Modifier.height(20.dp))
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Pratinjau Surat",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            fontFamily = PlusJakartaSans
                        )

                        val isApproved = data?.status?.lowercase() == "approved"
                        if (isApproved && !pdfUrl.isNullOrBlank()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GreenLight.copy(alpha = 0.3f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check_circle_24),
                                        contentDescription = null,
                                        tint = GreenDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "PDF Resmi",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = PlusJakartaSans,
                                        color = GreenDark
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            isLoadingPdf -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Blue900,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        "Memuat PDF...",
                                        fontSize = 13.sp,
                                        fontFamily = PlusJakartaSans,
                                        color = Grey700
                                    )
                                }
                            }
                            bitmap != null -> {
                                Image(
                                    bitmap = bitmap!!.asImageBitmap(),
                                    contentDescription = "Preview PDF",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            data == null -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_pdf),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        "Surat belum tersedia",
                                        fontSize = 13.sp,
                                        fontFamily = PlusJakartaSans,
                                        color = Grey700
                                    )
                                }
                            }
                            else -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_pdf),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        "Surat belum tersedia",
                                        fontSize = 13.sp,
                                        fontFamily = PlusJakartaSans,
                                        color = Grey700
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    CustomToast(
        message = toastMessage ?: "",
        type = toastType,
        visible = toastVisible,
    )

    if (state.showRevisionDialog) {
        RevisionDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.dismissRevisionDialog() },
            onShowToast = { message, type ->
                toastMessage = message
                toastType = type
                toastVisible = true
            }
        )
    }

    LaunchedEffect(state.revisionSuccess) {
        if (state.revisionSuccess) {
            toastMessage = "Surat berhasil diperbaiki dan dikirim ulang"
            toastType = ToastType.SUCCESS
            toastVisible = true
            viewModel.clearRevisionSuccess()
            kotlinx.coroutines.delay(1500)
            navController.popBackStack()
        }
    }

    LaunchedEffect(state.revisionError) {
        state.revisionError?.let { error ->
            toastMessage = error
            toastType = ToastType.ERROR
            toastVisible = true
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
    reason: String?,
    viewModel: DetailViewModel
) {
    val userName by viewModel.userNameState.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileAvatar(
                    name = userName ?: "User",
                    photoUrl = profile.photoUrl,
                    modifier = Modifier.size(48.dp)
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
                            modifier = Modifier.size(24.dp)
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

fun letterMapper(letterType: String): String  = when (letterType.lowercase()) {
    "recommendation" -> "Surat Rekomendasi"
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

@Composable
fun StatusTimeline(historyList: List<History>) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_history),
                    contentDescription = null,
                    tint = Blue900,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Riwayat Status Surat",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = PlusJakartaSans,
                    color = Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            historyList.forEachIndexed { index, history ->
                // Animasi dengan delay berbeda untuk setiap item
                var visible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(index * 150L) // Delay 150ms per item
                    visible = true
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideInVertically(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        ),
                        initialOffsetY = { it / 3 }
                    )
                ) {
                    TimelineItem(
                        history = history,
                        isFirst = index == 0,
                        isLast = index == historyList.size - 1
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineItem(
    history: History,
    isFirst: Boolean,
    isLast: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Timeline indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            // Top line
            if (!isFirst) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(12.dp)
                        .background(Grey200)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            Box(contentAlignment = Alignment.Center) {
                if (isFirst) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .alpha(alpha)
                            .background(getStatusColor(history.status ?: ""))
                    )
                }

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(getStatusColor(history.status ?: ""))
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(Grey200)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (isLast) 0.dp else 16.dp)
        ) {
            // Status badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = getStatusBackgroundColor(history.status ?: "").copy(alpha = 0.15f)
            ) {
                Text(
                    text = letterStatusToIndonesian(history.status ?: "-"),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = getStatusColor(history.status ?: ""),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontFamily = PlusJakartaSans
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Actor name
            if (!history.actorName.isNullOrBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null,
                        tint = Grey700,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = history.actorName ?: "",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Black,
                        fontFamily = PlusJakartaSans
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Timestamp
            if (!history.timestamp.isNullOrBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        tint = Grey700,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = formatDateTime(history.timestamp ?: ""),
                        fontSize = 12.sp,
                        color = Grey700,
                        fontFamily = PlusJakartaSans
                    )
                }
            }

            // Note
            if (!history.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_note),
                            contentDescription = null,
                            tint = Grey700,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = history.note ?: "",
                            fontSize = 12.sp,
                            color = Black,
                            fontFamily = PlusJakartaSans,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionDialog(
    viewModel: DetailViewModel,
    onDismiss: () -> Unit,
    onShowToast: (String, ToastType) -> Unit
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val letter = state.data

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(state.revisionSuccess) {
        if (state.revisionSuccess) {
            onShowToast("Revisi surat berhasil dikirim!", ToastType.SUCCESS)
            kotlinx.coroutines.delay(300)
            onDismiss()
            viewModel.clearRevisionSuccess()
        }
    }

    LaunchedEffect(state.revisionError) {
        state.revisionError?.let { error ->
            onShowToast(error, ToastType.ERROR)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    tint = Blue900,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Perbaiki Surat",
                    fontWeight = FontWeight.Bold,
                    fontFamily = PlusJakartaSans,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Silakan perbaiki data surat sesuai catatan revisi",
                fontSize = 13.sp,
                fontFamily = PlusJakartaSans,
                color = Grey700,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Revision Note
            letter?.reason?.let { reason ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = YellowLight.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_warning_24),
                            contentDescription = null,
                            tint = YellowDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Catatan Revisi:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PlusJakartaSans,
                                color = YellowDark
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = reason,
                                fontSize = 12.sp,
                                fontFamily = PlusJakartaSans,
                                color = Black,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Section: Informasi Surat
            SectionTitle("Informasi Surat")

            // Jenis Surat - Locked
            Text(
                text = "Jenis Surat",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PlusJakartaSans,
                color = Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = formState.selectedLetter,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Grey200,
                    disabledTextColor = Grey700,
                    disabledContainerColor = Grey50
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp
                )
            )

            Spacer(Modifier.height(12.dp))

            // Subject Field
            TextInput(
                label = "Subjek",
                placeholder = "Masukkan judul surat",
                value = formState.subject,
                onValueChange = { viewModel.updateFormField(FormField.SUBJECT, it) },
                singleLine = true,
                isError = formState.subjectError != null,
                supportingText = if (formState.subjectError != null) {
                    { Text(formState.subjectError!!, color = com.fauzangifari.surata.ui.theme.RedDark) }
                } else null
            )

            // Dispensasi-specific fields
            if (formState.selectedLetter == "Surat Dispensasi") {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("Pilih Siswa (Opsional)")

                MultiPickedField(
                    students = userState.data,
                    selectedStudentIds = formState.selectedStudentIds,
                    isLoading = userState.isLoading,
                    onSelectedChange = { viewModel.updateFormField(FormField.SELECTED_STUDENTS, it) }
                )

                if (formState.selectedStudentIds.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    val selectedNames = userState.data
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

            // Date & Time fields for Dispensasi and Tugas
            if (formState.selectedLetter in listOf("Surat Dispensasi", "Surat Tugas")) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("Tanggal & Waktu")

                DateInput(
                    label = "Tanggal Mulai",
                    value = formState.beginDate,
                    error = formState.beginDateError,
                    onDateSelected = { viewModel.updateFormField(FormField.BEGIN_DATE, it) },
                    placeholder = "Tanggal Mulai"
                )

                Spacer(modifier = Modifier.height(12.dp))

                TimeInput(
                    context = context,
                    label = "Waktu Mulai",
                    value = formState.beginTime,
                    error = formState.beginTimeError,
                    placeHolder = "Waktu Mulai",
                    onTimeSelected = { viewModel.updateFormField(FormField.BEGIN_TIME, it) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DateInput(
                    label = "Tanggal Berakhir",
                    value = formState.endDate,
                    error = formState.endDateError,
                    onDateSelected = { viewModel.updateFormField(FormField.END_DATE, it) },
                    placeholder = "Tanggal Berakhir"
                )

                Spacer(modifier = Modifier.height(12.dp))

                TimeInput(
                    context = context,
                    label = "Waktu Selesai",
                    value = formState.endTime,
                    error = formState.endTimeError,
                    placeHolder = "Waktu Selesai",
                    onTimeSelected = { viewModel.updateFormField(FormField.END_TIME, it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            SectionTitle("Keterangan")
            DescriptionInput(
                label = "Keterangan (Opsional)",
                placeholder = "Masukkan keterangan surat",
                value = formState.description,
                onValueChange = { viewModel.updateFormField(FormField.DESCRIPTION, it) },
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // File Upload
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

            // Is Printed Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ambil Surat di Tempat",
                    fontSize = 14.sp,
                    color = Grey900,
                    fontFamily = PlusJakartaSans
                )

                Switch(
                    checked = formState.isPrinted,
                    onCheckedChange = { viewModel.updateFormField(FormField.IS_PRINTED, it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = White,
                        checkedTrackColor = Blue900,
                        uncheckedThumbColor = White,
                        uncheckedTrackColor = Grey300
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ButtonCustom(
                    value = "Batal",
                    buttonType = ButtonType.REGULAR,
                    buttonStyle = ButtonStyle.OUTLINED,
                    textColor = Blue900,
                    fontSize = 14,
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                )

                ButtonCustom(
                    value = if (state.isSubmittingRevision) "Mengirim..." else "Kirim Ulang",
                    buttonType = ButtonType.REGULAR,
                    buttonStyle = ButtonStyle.FILLED,
                    textColor = White,
                    fontSize = 14,
                    onClick = {
                        if (!state.isSubmittingRevision && !uploadState.isUploading) {
                            viewModel.submitRevision()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Blue900,
        fontFamily = PlusJakartaSans,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )
}

private fun sharePdfFile(context: android.content.Context, file: File, letter: Letter?) {
    try {
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)

            // Add text with letter details
            val shareText = buildString {
                append("📄 ${letterMapper(letter?.letterType ?: "")}\n\n")
                append("Nomor Surat: ${letter?.letterNumber ?: "-"}\n")
                append("Perihal: ${letter?.subject ?: "-"}\n")
                append("Status: ${letterStatusToIndonesian(letter?.status ?: "")}\n")
                append("Tanggal: ${formatDateTime(letter?.createdAt ?: "")}\n")
            }
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Surat - ${letter?.subject ?: "Dokumen"}")

            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Bagikan Surat Melalui")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(
            context,
            "Gagal membagikan file: ${e.message}",
            Toast.LENGTH_SHORT
        ).show()
    }
}

/**
 * Share letter information for non-approved letters
 */
private fun shareLetterInfo(context: android.content.Context, letter: Letter?) {
    try {
        val shareText = buildString {
            append("📄 Informasi Surat\n\n")
            append("━━━━━━━━━━━━━━━━━━━━\n\n")
            append("📋 Jenis Surat:\n${letterMapper(letter?.letterType ?: "-")}\n\n")
            append("🔢 Nomor Surat:\n${letter?.letterNumber ?: "-"}\n\n")
            append("📝 Perihal:\n${letter?.subject ?: "-"}\n\n")
            append("👤 Pembuat:\n${letter?.applicantName ?: "-"}\n\n")
            append("📅 Tanggal Dibuat:\n${formatDateTime(letter?.createdAt ?: "")}\n\n")
            append("📊 Status:\n${letterStatusToIndonesian(letter?.status ?: "")}\n\n")

            if (!letter?.reason.isNullOrBlank()) {
                append("💬 Catatan:\n${letter?.reason}\n\n")
            }

            append("━━━━━━━━━━━━━━━━━━━━\n")
            append("Dibagikan dari Aplikasi Surata")
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Info Surat - ${letter?.subject ?: "Dokumen"}")
        }

        val chooser = Intent.createChooser(shareIntent, "Bagikan Informasi Surat Melalui")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(
            context,
            "Gagal membagikan informasi: ${e.message}",
            Toast.LENGTH_SHORT
        ).show()
    }
}
