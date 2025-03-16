package com.fauzangifari.surata.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.BottomBar
import com.fauzangifari.surata.ui.components.ButtonCustom
import com.fauzangifari.surata.ui.components.ButtonStyle
import com.fauzangifari.surata.ui.components.ButtonType
import com.fauzangifari.surata.ui.components.CardSurat
import com.fauzangifari.surata.ui.components.DateInput
import com.fauzangifari.surata.ui.components.DescriptionInput
import com.fauzangifari.surata.ui.components.DropdownField
import com.fauzangifari.surata.ui.components.FileUpload
import com.fauzangifari.surata.ui.components.TimeInput
import com.fauzangifari.surata.ui.components.TopBar
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.screens.notification.NotificationScreen
import com.fauzangifari.surata.ui.screens.profile.ProfileScreen
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeContent()
            }
            composable(Screen.Notification.route) {
                NotificationScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.dp, end = 32.dp, start = 32.dp, top = 16.dp)
    ) {
        Column {
            Text(
                text = "Halo,",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PlusJakartaSans,
                color = Grey900,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Muhammad Fauzan Gifari",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PlusJakartaSans,
                color = Grey900
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Blue800),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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
                    Text(text = "Siswa Aktif", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = PlusJakartaSans)
                    Text(text = "fauzan@sman1samarinda.sch.id", color = Color.White, fontSize = 12.sp, fontFamily = PlusJakartaSans)
                }
            }
        }

        // Riwayat Surat
        Text(
            text = "Riwayat Surat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = PlusJakartaSans,
            color = Grey900,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
        ) {
            val statusList = listOf("Disetujui", "Ditolak", "Diproses", "Belum diproses")

            items(10) { index ->
                CardSurat(status = statusList[index % statusList.size], onDetailClick = {})
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.dp, end = 16.dp, start = 16.dp, top = 16.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            onClick = { showSheet = true },
            containerColor = Blue800,
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Icon(modifier = Modifier.size(44.dp), painter = painterResource(id = R.drawable.ic_add_24), contentDescription = "Tambah Surat", tint = Color.White)
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            Form(onClose = { showSheet = false })
        }
    }
}

@Composable
fun Form(onClose: () -> Unit) {

    val context = LocalContext.current
    var description by remember { mutableStateOf("") }

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
            onItemSelected = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        DateInput(
            label = "Tanggal Mulai",
            onDateSelected = {

            },
            placeholder = "Pilih Tanggal Mulai",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DateInput(
            label = "Tanggal Berakhir",
            onDateSelected = {},
            placeholder = "Pilih Tanggal Berakhir",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TimeInput(
            context =  context,
            label = "Waktu Mulai",
            placeHolder = "Pilih Waktu Mulai",
            onTimeSelected = {},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TimeInput(
            context =  context,
            label = "Waktu Selesai",
            placeHolder = "Pilih Waktu Selesai",
            onTimeSelected = {},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionInput(
            label = "Keterangan (Opsional)",
            placeholder = "Masukkan keterangan surat",
            value = description,
            onValueChange = { description = it },
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        FileUpload (
            onFileSelected = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonCustom(
            value = "Buat Surat",
            fontSize = 14,
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.FILLED,
            backgroundColor = Blue800,
            textColor = White,
            onClick = {onClose},
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Form ( onClose = {} )
}