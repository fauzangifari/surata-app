package com.fauzangifari.surata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fauzangifari.surata.ui.theme.*

@Composable
fun CustomDialog(
    title: String,
    message: String = "",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = White,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Blue900
                )
                if (message.isNotBlank()) {
                    Text(
                        text = message,
                        style = Typography.bodyMedium,
                        color = Blue900
                    )
                }

                content()

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = Blue900)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onConfirm) {
                        Text("Simpan", color = Blue900)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomDialogPreview() {
    SurataTheme {
        CustomDialog(
            title = "Hapus Surat",
            message = "Apakah Anda yakin ingin menghapus surat ini?",
            onDismiss = {},
            onConfirm = {}
        ){}
    }
}
