package com.fauzangifari.surata.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.components.ButtonCustom
import com.fauzangifari.surata.ui.components.ButtonStyle
import com.fauzangifari.surata.ui.components.ButtonType
import com.fauzangifari.surata.ui.components.TextInput
import com.fauzangifari.surata.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.surata.ui.components.CustomToast
import com.fauzangifari.surata.ui.components.ToastType
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val headerHeight = (screenHeight * 0.40f).coerceAtMost(400.dp)

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf<ToastType>(ToastType.SUCCESS) }
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            delay(1100)
            onLoginSuccess.invoke()
        }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is Resource.Success -> {
                toastMessage = "Login Berhasil"
                toastType = ToastType.SUCCESS
                toastVisible = true

                delay(1000)
                toastVisible = false
            }
            is Resource.Error -> {
                val message = (loginState as Resource.Error).message ?: "Login gagal"
                toastMessage = message
                toastType = ToastType.ERROR
                toastVisible = true

                delay(1000)
                toastVisible = false
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_header),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Blue800.copy(alpha = 0.8f))
                    .background(Black.copy(alpha = 0.1f))
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.surata_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(80.dp)
                )
                Text(
                    text = "Selamat Datang",
                    fontSize = 22.sp,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                Text(
                    text = "Silahkan masuk untuk melanjutkan",
                    fontSize = 14.sp,
                    fontFamily = PlusJakartaSans,
                    color = White
                )
            }
        }

        // Body (Form)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.70f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(White)
                .padding(horizontal = 32.dp, vertical = 48.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 24.sp,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    color = Grey900,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email input
                TextInput(
                    label = "Email Address",
                    value = email,
                    placeholder = "Masukkan Email Sekolah",
                    onValueChange = viewModel::onEmailChange,
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = {
                        emailError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_email_24),
                            contentDescription = "Email Icon"
                        )
                    },
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password input
                TextInput(
                    label = "Password",
                    value = password,
                    placeholder = "Masukkan Password",
                    onValueChange = viewModel::onPasswordChange,
                    singleLine = true,
                    isError = passwordError != null,
                    supportingText = {
                        passwordError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_lock_24),
                            contentDescription = "Password Icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = viewModel::togglePasswordVisibility) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible)
                                        R.drawable.ic_outline_visibility_off_24
                                    else
                                        R.drawable.ic_outline_visibility_24
                                ),
                                contentDescription = "Toggle Password"
                            )
                        }
                    },
                    isPassword = !passwordVisible,
                )

                Spacer(modifier = Modifier.height(24.dp))

                ButtonCustom(
                    value = if (loginState is Resource.Loading) "Memproses..." else "Masuk",
                    onClick = {
                        if (loginState !is Resource.Loading) {
                            viewModel.login()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    fontSize = 16,
                    buttonType = ButtonType.REGULAR,
                )

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(24.dp))

                ButtonCustom(
                    value = "Masuk dengan Google",
                    onClick = { /* TODO: Google Sign-In */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    textColor = Blue900,
                    fontSize = 16,
                    buttonType = ButtonType.REGULAR,
                    buttonStyle = ButtonStyle.OUTLINED,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(23.dp),
                            tint = Color.Unspecified
                        )
                    }
                )
            }
        }

        if (loginState is Resource.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Blue700)
            }
        }

        toastMessage?.let { msg ->
            Box(modifier = Modifier.fillMaxSize()) {
                CustomToast(
                    message = msg,
                    type = toastType,
                    visible = toastVisible,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        modifier = Modifier,
        onLoginSuccess = {}
    )
}
