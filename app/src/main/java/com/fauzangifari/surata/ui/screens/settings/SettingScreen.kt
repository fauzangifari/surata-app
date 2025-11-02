package com.fauzangifari.surata.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.CustomToast
import com.fauzangifari.surata.ui.components.ToastType
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.BackgroundLight
import com.fauzangifari.surata.ui.theme.Blue100
import com.fauzangifari.surata.ui.theme.Blue500
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Blue900
import com.fauzangifari.surata.ui.theme.Grey200
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey700
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.RedMedium
import com.fauzangifari.surata.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SettingViewModel = koinViewModel()
) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val logoutState by viewModel.logoutState.collectAsStateWithLifecycle()

    val userName by viewModel.userNameState.collectAsStateWithLifecycle()
    val userEmail by viewModel.userEmailState.collectAsStateWithLifecycle()

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf<ToastType>(ToastType.SUCCESS) }
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.logoutMessage.collectLatest { message ->
            toastMessage = message
            toastType = ToastType.SUCCESS
            toastVisible = true
        }
    }

    LaunchedEffect(toastVisible) {
        if (toastVisible) {
            delay(1000)
            toastVisible = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.logoutEvent.collectLatest { event ->
            when (event) {
                is SettingViewModel.LogoutEvent.NavigateToLogin -> {
                    showLogoutDialog = false
                    viewModel.resetLogoutState()
                    delay(1100)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SettingScreenContent(
            modifier = modifier,
            navController = navController,
            showLogoutDialog = showLogoutDialog,
            onShowLogoutDialogChange = { visible ->
                showLogoutDialog = visible
                if (!visible) {
                    viewModel.resetLogoutState()
                }
            },
            onConfirmLogout = { viewModel.logout() },
            logoutState = logoutState,
            userName = userName.toString(),
            userEmail = userEmail.toString()
        )

        // CustomToast overlay at top of the screen
        AnimatedVisibility(
            visible = toastVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            toastMessage?.let { msg ->
                CustomToast(
                    message = msg,
                    type = toastType,
                    visible = true,
                    modifier = Modifier.padding(top = 72.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showLogoutDialog: Boolean,
    onShowLogoutDialogChange: (Boolean) -> Unit,
    onConfirmLogout: () -> Unit,
    logoutState: Resource<Boolean>,
    userEmail: String,
    userName: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pengaturan",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PlusJakartaSans,
                        color = Grey900,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = "Kembali",
                            tint = Grey900
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        }
    ) { innerPadding ->
        // Logout Dialog
        if (showLogoutDialog) {
            val isLogoutLoading = logoutState is Resource.Loading

            AlertDialog(
                onDismissRequest = { if (!isLogoutLoading) onShowLogoutDialogChange(false) },
                containerColor = White,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier.size(60.dp),
                            shape = CircleShape,
                            color = RedMedium.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_logout_24),
                                    contentDescription = null,
                                    tint = RedMedium,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Keluar dari Akun",
                            fontWeight = FontWeight.Bold,
                            fontFamily = PlusJakartaSans,
                            color = Grey900,
                            fontSize = 18.sp
                        )
                    }
                },
                text = {
                    Text(
                        text = "Apakah Anda yakin ingin keluar dari akun ini?",
                        fontFamily = PlusJakartaSans,
                        color = Grey700,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = onConfirmLogout,
                        enabled = !isLogoutLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLogoutLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = RedMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = if (isLogoutLoading) "Memproses..." else "Ya, Keluar",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            color = RedMedium,
                            fontSize = 15.sp
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { if (!isLogoutLoading) onShowLogoutDialogChange(false) },
                        enabled = !isLogoutLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Batal",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Medium,
                            color = Grey700,
                            fontSize = 15.sp
                        )
                    }
                }
            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        color = Blue900
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            AsyncImage(
                                model = "https://avatars.githubusercontent.com/u/77602702?v=4",
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Grey900,
                        fontFamily = PlusJakartaSans
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Blue100
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_email_24),
                                contentDescription = null,
                                tint = Blue800,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = userEmail,
                                color = Blue800,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = PlusJakartaSans
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Section
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                // Account Section
                Text(
                    text = "Akun",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans,
                    color = Grey500,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    MenuItem(
                        icon = R.drawable.ic_outline_person_24,
                        title = "Data Pribadi",
                        subtitle = "Lihat dan edit profil Anda",
                        iconBgColor = Blue100,
                        iconTint = Blue500,
                        onClick = { /* TODO */ }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Support Section
                Text(
                    text = "Bantuan & Dukungan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans,
                    color = Grey500,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    MenuItem(
                        icon = R.drawable.ic_question_answer_24,
                        title = "FAQ",
                        subtitle = "Pertanyaan yang sering ditanyakan",
                        iconBgColor = Blue100,
                        iconTint = Blue500,
                        onClick = { navController.navigate("faq") }
                    )

                    HorizontalDivider(
                        color = Grey200,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    MenuItem(
                        icon = R.drawable.ic_info_outline,
                        title = "Tentang Aplikasi",
                        subtitle = "Informasi versi dan developer",
                        iconBgColor = Blue100,
                        iconTint = Blue500,
                        onClick = { navController.navigate("about") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    MenuItem(
                        icon = R.drawable.ic_logout_24,
                        title = "Keluar",
                        subtitle = "Keluar dari akun Anda",
                        iconBgColor = RedMedium.copy(alpha = 0.1f),
                        iconTint = RedMedium,
                        showArrow = false,
                        onClick = { onShowLogoutDialogChange(true) }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // App Version Footer
                Text(
                    text = "Versi Aplikasi 1.0.0",
                    fontSize = 12.sp,
                    fontFamily = PlusJakartaSans,
                    color = Grey500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun MenuItem(
    icon: Int,
    title: String,
    subtitle: String? = null,
    iconBgColor: androidx.compose.ui.graphics.Color = Blue100,
    iconTint: androidx.compose.ui.graphics.Color = Blue900,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = iconBgColor,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                fontFamily = PlusJakartaSans
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Grey500,
                    fontFamily = PlusJakartaSans
                )
            }
        }

        if (showArrow) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Next",
                tint = Grey500,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreenContent(
        navController = NavHostController(LocalContext.current),
        showLogoutDialog = false,
        onShowLogoutDialogChange = {},
        onConfirmLogout = {},
        logoutState = Resource.Idle(),
        userName = "Muhammad Fauzan Gifari",
        userEmail = "fauzan@example.com"
    )
}
