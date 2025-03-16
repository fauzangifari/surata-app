package com.fauzangifari.surata.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey500
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans

@Composable
fun DescriptionInput(
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    isEnabled: Boolean = true,
    maxLines: Int = 5,
    maxChar: Int = 100
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(if (isFocused) Blue800 else Grey900, label = "BorderColor")
    val charCount = value.count { it.isLetterOrDigit() }

    Column(modifier = Modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier.padding(bottom = 7.dp),
            fontFamily = PlusJakartaSans
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.isBlank() || newValue.trim().split(Regex("\\s+")).size <= maxChar) {
                    onValueChange(newValue)
                }
            },
            enabled = isEnabled,
            singleLine = false,
            maxLines = maxLines,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Black,
                fontFamily = PlusJakartaSans
            ),
            trailingIcon = {
                if (value.isNotEmpty()) {
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
                .height(130.dp)
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

        Text(
            text = "$charCount / $maxChar kata",
            fontSize = 12.sp,
            color = if (charCount > maxChar) Color.Red else Grey500,
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = PlusJakartaSans
        )

    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionInputPreview() {
    var description by remember { mutableStateOf("") }
    DescriptionInput(
        label = "Deskripsi",
        value = description,
        placeholder = "Tulis deskripsi di sini...",
        onValueChange = { description = it }
    )
}