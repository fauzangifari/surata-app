package com.fauzangifari.surata.ui.screens.faq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fauzangifari.surata.R
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
                        text = "FAQ",
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = Blue100
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_question_answer_24),
                            contentDescription = "FAQ Icon",
                            tint = Blue500,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pertanyaan Umum",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PlusJakartaSans,
                    color = Grey900
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Temukan jawaban dari pertanyaan yang sering diajukan",
                    fontSize = 14.sp,
                    fontFamily = PlusJakartaSans,
                    color = Grey700,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(faqList) { faq ->
                    FAQItem(
                        question = faq.question,
                        answer = faq.answer
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Masih ada pertanyaan?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PlusJakartaSans,
                        color = Grey900
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { /* TODO: Implement contact support */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue800
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_email_24),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hubungi Kami",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PlusJakartaSans,
                            color = White
                        )
                    }
                }
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

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "rotation"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = Blue100
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Q",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PlusJakartaSans,
                            color = Blue800
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = question,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Grey900,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(32.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (expanded) Blue100 else Grey200
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = if (expanded) "Tutup" else "Buka",
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(rotation),
                                tint = if (expanded) Blue800 else Grey700
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        color = Grey200,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = GreenLight.copy(alpha = 0.3f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "A",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = PlusJakartaSans,
                                    color = GreenDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = answer,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            color = Grey700,
                            lineHeight = 22.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FAQItemPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight)
            .padding(16.dp)
    ) {
        FAQItem(
            question = "Apa itu Surata?",
            answer = "Surata adalah aplikasi persuratan berbasis android untuk SMA Negeri 1 Samarinda yang memudahkan siswa dalam mengajukan dan mengelola surat secara digital."
        )
    }
}
