package com.fauzangifari.surata.ui.screens.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.ui.components.NotificationCard
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NotificationScreen() {

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))
    val todayFormatted = today.format(formatter)
    val yesterdayFormatted = yesterday.format(formatter)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom =  24.dp, start = 24.dp, end = 24.dp)
    ) {

        Text(
            text = "Notifikasi",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = PlusJakartaSans,
            color = Grey900,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Hari Ini, $todayFormatted",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(1) {
                NotificationCard()
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kemarin, $yesterdayFormatted",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(3) {
                NotificationCard()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen()
}
