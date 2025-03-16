package com.fauzangifari.surata.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.ui.theme.Black
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey900
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    modifier: Modifier = Modifier,
    label: String = "Dropdown Field",
    placeHolder: String = "Pilih Jenis Surat",
    items: List<String> = emptyList(),
    onItemSelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(if (isFocused) Blue800 else Grey900, label = "BorderColor")

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier.padding(bottom = 7.dp),
            fontFamily = PlusJakartaSans
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Black,
                    fontFamily = PlusJakartaSans
                ),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = !expanded },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledIndicatorColor = borderColor,
                    unfocusedIndicatorColor = borderColor,
                    focusedIndicatorColor = borderColor
                ),
                placeholder = { Text(text = placeHolder, fontSize = 16.sp, color = Color.Gray, fontFamily = PlusJakartaSans) },
                
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(White),
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item,
                            fontFamily = PlusJakartaSans,
                            fontSize = 16.sp,
                            color = Grey900
                        ) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            onItemSelected(item)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropdownFieldPreview() {
    DropdownField(
        items = listOf("Surat Izin", "Surat Sakit", "Surat Keterangan"),
        onItemSelected = {}
    )
}
