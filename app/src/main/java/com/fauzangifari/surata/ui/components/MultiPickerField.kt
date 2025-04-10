package com.fauzangifari.surata.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fauzangifari.surata.ui.theme.SurataTheme

@Composable
fun MultiPickedField(
    modifier: Modifier = Modifier,
    selectedStudents: List<String> = emptyList(),
    onSelectedChange: (List<String>) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val tempSelected = remember { mutableStateListOf<String>().apply { addAll(selectedStudents) } }

    val filteredStudents = remember(searchQuery) {
        if (searchQuery.isBlank()) multiPickerField
        else multiPickerField.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier) {
        OutlinedTextField(
            value = if (selectedStudents.isEmpty()) "" else "${selectedStudents.size} siswa dipilih",
            onValueChange = {},
            readOnly = true,
            label = { Text("Pilih Siswa (Maksimal 5)") },
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Pilih siswa")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }

    if (showDialog) {
        CustomDialog(
            title = "Pilih Siswa",
            onDismiss = { showDialog = false },
            onConfirm = {
                onSelectedChange(tempSelected.toList())
                showDialog = false
            }
        ) {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Cari siswa...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Divider()

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(filteredStudents) { student ->
                        val isSelected = tempSelected.contains(student)
                        val enabled = isSelected || tempSelected.size < 5
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (isSelected) tempSelected.remove(student)
                                    else if (enabled) tempSelected.add(student)
                                },
                                enabled = enabled
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = student,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

private val multiPickerField = listOf(
    "Ahmad Fauzan",
    "Rizky Maulana",
    "Fitri Aulia",
    "Budi Santoso",
    "Siti Rohmah",
    "Andi Prasetyo",
    "Maya Putri",
    "Hafiz Alamsyah",
    "Nadia Rahma",
    "Dian Sastro"
)

@Preview(showBackground = true)
@Composable
fun MultiPickerFieldPreview() {
    SurataTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            MultiPickedField(
                selectedStudents = listOf("Ahmad Fauzan", "Fitri Aulia"),
                onSelectedChange = {}
            )
        }
    }
}
