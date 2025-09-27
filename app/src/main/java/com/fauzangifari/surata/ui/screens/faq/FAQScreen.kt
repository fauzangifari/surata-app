package com.fauzangifari.surata.ui.screens.faq

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.ButtonCustom
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FAQViewModel
) {

    val faqList by viewModel.faqList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FAQs",
                        fontWeight = FontWeight.Medium,
                        fontFamily = PlusJakartaSans,
                        color = Grey900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = "Kembali",
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
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Judul
                Text(
                    text = "Pertanyaan yang sering ditanyakan:",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans,
                    color = Grey900
                )

                faqList.forEach { faq ->
                    FAQItem(
                        question = faq.question
                    )
                }

                Spacer(modifier = Modifier.weight(0.8f))

                ButtonCustom(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    value = "Ajukan Pertanyaan",
                    fontSize = 16,
                    backgroundColor = Blue800,
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun FAQItem(question: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Grey500),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PlusJakartaSans,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
            }
            if (expanded) {
                Text(
                    text = "Surata adalah aplikasi layanan surat menyurat digital.",
                    fontFamily = PlusJakartaSans,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
