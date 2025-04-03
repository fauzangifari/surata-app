package com.fauzangifari.surata.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.Blue100
import com.fauzangifari.surata.ui.theme.Blue900
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey700
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White

@Composable
fun Collapse(
    modifier: Modifier = Modifier,
    label: String?,
    namaPembuat: String,
    tanggalSurat: String,
    jenisSurat: String,
    dokumenNama: String,
    dokumenTipe: String = "PDF",
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PlusJakartaSans
                )
                Icon(
                    painter = painterResource(id = if (expanded) R.drawable.ic_keyboard_arrow_up_24 else R.drawable.ic_keyboard_arrow_down_24),
                    contentDescription = "Expand Icon"
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Card(
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRow(iconRes = R.drawable.ic_outline_person_24, label = "Nama Pembuat", value = namaPembuat)
                    InfoRow(iconRes = R.drawable.ic_calendar_month_24, label = "Tanggal Surat Dibuat", value = tanggalSurat)
                    InfoRow(iconRes = R.drawable.ic_outline_email_24, label = "Jenis Surat", value = jenisSurat)

                    Text(
                        text = "Dokumen Pendukung",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PlusJakartaSans,
                        color = Grey700,
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .border(
                                width = 1.dp,
                                color = Grey500,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE57373), shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                                    .padding(horizontal = 12.dp, vertical = 18.dp)
                            ) {
                                Text(dokumenTipe, color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            Text(
                                text = dokumenNama,
                                fontSize = 12.sp,
                                color = Grey900,
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun InfoRow(iconRes: Int, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Blue100, shape = RoundedCornerShape(8.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Blue900,
                modifier = Modifier
                    .size(50.dp)
                    .padding(4.dp)
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Grey700,
                fontFamily = PlusJakartaSans,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PlusJakartaSans
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CollapsePreview() {
    Collapse(
        label = "Data Surat",
        namaPembuat = "Muhammad Fauzan Gifari Dzul Fahmi",
        tanggalSurat = "15 Maret 2025",
        jenisSurat = "Surat Dispensasi",
        dokumenNama = "SP Lomba HUT SMA Negeri 3 Samarinda 2024.pdf",
        dokumenTipe = "PDF"
    )
}



