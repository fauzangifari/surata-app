package com.fauzangifari.surata.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.focus.onFocusChanged
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.Grey500

@Composable
fun TextInput(
    label: String,
    value: String,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    trailingIconClick: (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(if (isFocused) Blue800 else Grey900, label = "BorderColor")

    Column(modifier = Modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 7.dp),
            fontFamily = PlusJakartaSans
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = isEnabled,
            singleLine = singleLine,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = PlusJakartaSans
            ),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_outline_visibility_off_24 else R.drawable.ic_outline_visibility_24
                            ),
                            contentDescription = "Toggle Password",
                            tint = Grey500
                        )
                    }
                } else if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_24),
                            contentDescription = "Clear Text",
                            tint = Grey500
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            placeholder = { Text(text = placeholder, fontSize = 16.sp, color = Color.Gray, fontFamily = PlusJakartaSans) },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledIndicatorColor = borderColor,
                unfocusedIndicatorColor = borderColor,
                focusedIndicatorColor = borderColor
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordInputPreview() {
    var password by remember { mutableStateOf("") }
    TextInput(
        label = "Password",
        value = password,
        placeholder = "Masukkan Password",
        onValueChange = { password = it },
        isPassword = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_lock_24),
                contentDescription = "Lock Icon",
                tint = Grey500
            )
        }
    )
}
