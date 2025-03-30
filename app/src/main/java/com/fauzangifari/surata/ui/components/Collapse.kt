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
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White

@Composable
fun Collapse(
    modifier: Modifier = Modifier,
    label: String?,
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
                    fontWeight = FontWeight.Bold
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

                    InfoRow(iconRes = R.drawable.ic_outline_person_24, label = "Nama Pembuat", value = "Muhammad Fauzan Gifari Dzul Fahmi")
                    InfoRow(iconRes = R.drawable.ic_calendar_month_24, label = "Tanggal Surat Dibuat", value = "15 Maret 2025")
                    InfoRow(iconRes = R.drawable.ic_outline_email_24, label = "Jenis Surat", value = "Surat Dispensasi")

                    Text("Dokumen Pendukung", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray, modifier = Modifier.padding(top = 12.dp))

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
                                Text("PDF", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            Text(
                                text = "SP Lomba HUT SMA Negeri 3 Samarinda 2024.pdf",
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
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(painter = painterResource(id = iconRes), contentDescription = null)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollapsePreview() {
    Collapse(
        label = "Data Surat"
    )
}


