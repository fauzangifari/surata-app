package com.fauzangifari.surata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.GreenDark
import com.fauzangifari.surata.ui.theme.GreenLight
import com.fauzangifari.surata.ui.theme.Grey300
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.RedDark
import com.fauzangifari.surata.ui.theme.RedLight
import com.fauzangifari.surata.ui.theme.White
import com.fauzangifari.surata.ui.theme.YellowDark
import com.fauzangifari.surata.ui.theme.YellowLight
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CardSurat(
    tipeSurat: String = "Default",
    status: String = "Menunggu",
    isoDateTime: String = "",
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusColor = getStatusColor(status)

    val (tanggal, waktu) = splitDateTime(isoDateTime)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tipeSurat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = PlusJakartaSans
                )
                ButtonCustom(
                    value = "Detail Surat",
                    buttonType = ButtonType.PILL,
                    onClick = onDetailClick,
                    fontSize = 12,
                    modifier = Modifier.height(36.dp).wrapContentWidth(),
                    buttonStyle = ButtonStyle.OUTLINED,
                    textColor = Blue800,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_email_24),
                            contentDescription = "Detail Surat",
                            tint = Blue800,
                        )
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Grey300)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Keterangan",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Grey500,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(statusColor)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .width(75.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = status,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = getStatusTextcolor(status),
                            fontFamily = PlusJakartaSans
                        )
                    }
                }

                VerticalDivider(
                    color = Grey300,
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Tanggal",
                        fontSize = 10.sp,
                        color = Grey500,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PlusJakartaSans,
                    )

                    Spacer(modifier = Modifier.padding(bottom = 4.dp))

                    Text(
                        text = "$waktu | $tanggal",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PlusJakartaSans
                    )
                }
            }
        }
    }
}


fun getStatusColor(status: String): Color {
    return when (status.lowercase(Locale.ROOT)) {
        "diproses" -> YellowLight
        "disetujui" -> GreenLight
        "ditolak" -> RedLight
        else -> Grey500
    }
}

fun getStatusTextcolor(status: String): Color {
    return when (status.lowercase(Locale.ROOT)) {
        "diproses" -> YellowDark
        "disetujui" -> GreenDark
        "ditolak" -> RedDark
        else -> Grey900
    }
}

fun splitDateTime(isoDateTime: String): Pair<String, String> {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = sdf.parse(isoDateTime)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))

        Pair(
            dateFormat.format(date ?: Date()),
            timeFormat.format(date ?: Date())
        )
    } catch (e: Exception) {
        Pair("-", "-")
    }
}


@Preview(showBackground = true)
@Composable
fun SuratDispensasiCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardSurat(tipeSurat = "Surat Izin Sakit", status = "Diproses", isoDateTime = "2025-03-30T12:04:16.780Z", onDetailClick = {})
        CardSurat(tipeSurat = "Surat Dispensasi", status = "Disetujui", isoDateTime = "2025-03-30T10:30:00.000Z", onDetailClick = {})
        CardSurat(tipeSurat = "Surat Rekomendasi", status = "Ditolak", isoDateTime = "2025-03-30T09:15:45.000Z", onDetailClick = {})
        CardSurat(tipeSurat = "Surat Tugas", status = "Belum diproses", isoDateTime = "2025-03-30T14:00:00.000Z", onDetailClick = {})
    }
}

