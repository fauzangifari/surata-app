package com.fauzangifari.surata.ui.screens.faq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.ButtonCustom
import com.fauzangifari.surata.ui.theme.*

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
                    IconButton(onClick = { navController.popBackStack() }) {
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
                Text(
                    text = "Pertanyaan yang sering ditanyakan:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = PlusJakartaSans,
                    color = Grey900
                )

                faqList.forEach { faq ->
                    FAQItem(
                        question = faq.question,
                        answer = faq.answer
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
fun FAQItem(
    question: String,
    answer: String
) {
    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Grey100),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = answer,
                        fontFamily = PlusJakartaSans,
                        fontSize = 14.sp,
                        color = Grey900
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FAQItemPreview(modifier: Modifier = Modifier) {
    FAQItem(
        question = "Apa itu Surata?",
        answer = "Surata adalah aplikasi persuratan berbasis android untuk SMA Negeri 1 Samarinda."
    )
}
