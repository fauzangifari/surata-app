package com.fauzangifari.surata.ui.components

import android.app.TimePickerDialog
import android.content.Context
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
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import java.util.*

@Composable
fun TimeInput(
    modifier: Modifier = Modifier,
    context: Context,
    label: String = "Time",
    placeHolder: String = "Pilih Waktu",
    onTimeSelected: (String) -> Unit = {}
) {
    var selectedTime by remember { mutableStateOf("") }

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
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeInputPreview() {
    val context = LocalContext.current
    TimeInput(context = context)
}
