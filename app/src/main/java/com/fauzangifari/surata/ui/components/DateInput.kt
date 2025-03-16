package com.fauzangifari.surata.ui.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    modifier: Modifier = Modifier,
    label: String = "Date",
    placeholder: String = "MM/DD/YYYY",
    onDateSelected: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }

    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(if (isFocused) Blue800 else Grey900, label = "BorderColor")

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 7.dp),
            fontFamily = PlusJakartaSans
        )
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = PlusJakartaSans
            ),
            placeholder = { Text(text = placeholder, fontSize = 16.sp, color = Color.Gray, fontFamily = PlusJakartaSans) },
            trailingIcon = {
                IconButton(onClick = { showDatePicker(context) { date ->
                    selectedDate = date
                    onDateSelected(date)
                } }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_edit_calendar_24),
                        contentDescription = "Pilih Tanggal"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
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

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            onDateSelected(dateFormat.format(selectedCalendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePicker.show()
}

@Preview(showBackground = true)
@Composable
fun DateInputPreview() {
    DateInput()
}
