package com.fauzangifari.surata.ui.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.fauzangifari.surata.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInput(
    modifier: Modifier = Modifier,
    context: Context,
    label: String = "Time",
    placeHolder: String = "Pilih Waktu",
    value: String = "",
    error: String? = null,
    onTimeSelected: (String) -> Unit = {}
) {
    var selectedTime by remember { mutableStateOf(value) }

    LaunchedEffect(value) {
        selectedTime = value
    }

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            selectedTime = String.format("%02d : %02d", selectedHour, selectedMinute)
            onTimeSelected(selectedTime)
        }, hour, minute, true
    )

    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        if (error != null) Color.Red
        else if (isFocused) Blue800
        else Grey900, label = "BorderColor"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier.padding(bottom = 7.dp),
            fontFamily = PlusJakartaSans
        )

        OutlinedTextField(
            value = selectedTime,
            onValueChange = {},
            readOnly = true,
            isError = error != null,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = PlusJakartaSans),
            trailingIcon = {
                IconButton(onClick = { timePickerDialog.show() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_access_time_24),
                        contentDescription = "Pilih Waktu"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { timePickerDialog.show() },
            placeholder = { Text(text = placeHolder, fontSize = 16.sp, color = Color.Gray, fontFamily = PlusJakartaSans) },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledIndicatorColor = borderColor,
                unfocusedIndicatorColor = borderColor,
                focusedIndicatorColor = borderColor,
                errorContainerColor = Color.White,
                errorIndicatorColor = Color.Red
            )
        )

        if (error != null) {
            Text(
                text = error,
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                fontFamily = PlusJakartaSans
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeInputPreview() {
    val context = LocalContext.current
    TimeInput(context = context)
}
