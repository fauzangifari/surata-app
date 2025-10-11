package com.fauzangifari.surata.ui.screens.settings

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.fauzangifari.surata.ui.navigation.Screen
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue100
import com.fauzangifari.surata.ui.theme.Blue900
import com.fauzangifari.surata.ui.theme.Grey200
import com.fauzangifari.surata.ui.theme.Grey300
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White
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

    LaunchedEffect(Unit) {
        viewModel.logoutMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.logoutEvent.collectLatest { event ->
            when (event) {
                is SettingViewModel.LogoutEvent.NavigateToLogin -> {
                    showLogoutDialog = false
                    viewModel.resetLogoutState()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

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
        logoutState = logoutState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showLogoutDialog: Boolean,
    onShowLogoutDialogChange: (Boolean) -> Unit,
    onConfirmLogout: () -> Unit,
    logoutState: Resource<Boolean>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pengaturan",
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
        if (showLogoutDialog) {
            val isLogoutLoading = logoutState is Resource.Loading

            AlertDialog(
                onDismissRequest = { onShowLogoutDialogChange(false) },
                containerColor = White,
                title = {
                    Text(
                        text = "Keluar dari Akun",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PlusJakartaSans,
                        color = Black
                    )
                },
                text = {
                    Text(
                        text = "Apakah Anda yakin ingin keluar?",
                        fontFamily = PlusJakartaSans,
                        color = Grey900
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = onConfirmLogout,
                        enabled = !isLogoutLoading
                    ) {
                        if (isLogoutLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(
                            text = "Keluar",
                            fontFamily = PlusJakartaSans,
                            color = Blue900
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onShowLogoutDialogChange(false) }) {
                        Text(
                            text = "Batal",
                            fontFamily = PlusJakartaSans,
                            color = Grey900
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = "https://avatars.githubusercontent.com/u/77602702?v=4",
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Muhammad Fauzan Gifari",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Black,
                            fontFamily = PlusJakartaSans
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "fauzan@sman1samarinda.sch.id",
                            color = Blue900,
                            fontSize = 12.sp,
                            fontFamily = PlusJakartaSans,
                            modifier = Modifier
                                .background(
                                    color = Grey200,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    MenuItem(
                        icon = R.drawable.ic_outline_person_24,
                        title = "Data Pribadi",
                        onClick = {}
                    )

                    HorizontalDivider(
                        color = Grey300,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                    )

                    MenuItem(
                        icon = R.drawable.ic_question_answer_24,
                        title = "FAQs",
                        onClick = { navController.navigate("faq") }
                    )

                    HorizontalDivider(
                        color = Grey300,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                    )

                    MenuItem(
                        icon = R.drawable.ic_info_outline,
                        title = "Tentang Aplikasi",
                        onClick = { navController.navigate("about") }
                    )

                    HorizontalDivider(
                        color = Grey300,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                    )

                    MenuItem(
                        icon = R.drawable.ic_logout_24,
                        title = "Keluar",
                        onClick = { onShowLogoutDialogChange(true) }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Blue100,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    tint = Blue900,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            color = Black,
            fontFamily = PlusJakartaSans,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = "Next",
            tint = Black
        )
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
        logoutState = Resource.Idle()
    )
}
