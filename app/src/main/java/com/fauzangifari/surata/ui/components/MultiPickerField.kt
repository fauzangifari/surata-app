package com.fauzangifari.surata.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.domain.model.Student
import com.fauzangifari.surata.ui.theme.SurataTheme
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.Grey100
import com.fauzangifari.surata.ui.theme.Grey700

@Composable
fun MultiPickedField(
    modifier: Modifier = Modifier,
    students: List<Student> = emptyList(),
    selectedStudentIds: List<String> = emptyList(),
    onSelectedChange: (List<String>) -> Unit = {},
    isLoading: Boolean = false
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val tempSelected = remember { mutableStateListOf<String>().apply {
        clear()
        addAll(selectedStudentIds)
    } }

    val filteredStudents = remember(searchQuery, students) {
        if (searchQuery.isBlank()) students
        else students.filter {
            it.name?.contains(searchQuery, ignoreCase = true) == true ||
            it.nisn?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    val selectedNames = students.filter { it.id in selectedStudentIds }.mapNotNull { it.name }

    Column(modifier) {
        OutlinedTextField(
            value = when {
                isLoading -> "Memuat data siswa..."
                selectedNames.isEmpty() -> "Pilih Siswa"
                else -> "${selectedNames.size} siswa dipilih"
            },
            onValueChange = {},
            readOnly = true,
            label = { Text("Daftar Siswa Dispensasi") },
            trailingIcon = {
                IconButton(
                    onClick = { if (!isLoading) showDialog = true },
                    enabled = !isLoading
                ) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Pilih siswa")
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }

    if (showDialog) {
        CustomDialog(
            title = "Pilih Siswa",
            onDismiss = {
                showDialog = false
                searchQuery = ""
                tempSelected.clear()
                tempSelected.addAll(selectedStudentIds)
            },
            onConfirm = {
                onSelectedChange(tempSelected.toList())
                showDialog = false
                searchQuery = ""
            }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Cari siswa...") },
                    placeholder = { Text("Nama atau NISN") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                if (tempSelected.isNotEmpty()) {
                    Text(
                        text = "Terpilih (${tempSelected.size}/5)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue800,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tempSelected.take(3).forEach { studentId ->
                            val student = students.find { it.id == studentId }
                            student?.let {
                                AssistChip(
                                    onClick = {
                                        tempSelected.remove(studentId)
                                    },
                                    label = {
                                        Text(
                                            text = it.name ?: "-",
                                            fontSize = 12.sp,
                                            maxLines = 1
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Hapus",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                        if (tempSelected.size > 3) {
                            AssistChip(
                                onClick = { },
                                label = {
                                    Text(
                                        text = "+${tempSelected.size - 3}",
                                        fontSize = 12.sp
                                    )
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                Text(
                    text = "Daftar Siswa",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue800,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (filteredStudents.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isBlank()) "Tidak ada data siswa" else "Tidak ditemukan",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredStudents) { student ->
                            val studentId = student.id ?: return@items
                            val isSelected = tempSelected.contains(studentId)
                            val enabled = isSelected || tempSelected.size < 5

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Blue800.copy(alpha = 0.08f) else Grey100.copy(alpha = 0.3f),
                                tonalElevation = if (isSelected) 2.dp else 0.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = {
                                            if (isSelected) tempSelected.remove(studentId)
                                            else if (enabled) tempSelected.add(studentId)
                                        },
                                        enabled = enabled,
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Blue800
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = student.name ?: "-",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                        if (!student.nisn.isNullOrBlank()) {
                                            Text(
                                                text = "NISN: ${student.nisn}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Grey700,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (tempSelected.size >= 5) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = "⚠️ Maksimal 5 siswa yang dapat dipilih",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultiPickerFieldPreview() {
    SurataTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            MultiPickedField(
                students = listOf(
                    Student(id = "1", name = "Ahmad Fauzan Gifari", nik = "0051234567"),
                    Student(id = "2", name = "Siti Nurhaliza", nik = "0051234568"),
                    Student(id = "3", name = "Budi Santoso", nik = "0051234569"),
                    Student(id = "4", name = "Dewi Lestari", nik = "0051234570")
                ),
                selectedStudentIds = listOf("1", "2"),
                onSelectedChange = {}
            )
        }
    }
}
