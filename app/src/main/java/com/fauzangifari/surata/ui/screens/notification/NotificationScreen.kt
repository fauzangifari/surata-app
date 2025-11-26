package com.fauzangifari.surata.ui.screens.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fauzangifari.surata.ui.components.NotificationCard
import com.fauzangifari.surata.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController = rememberNavController(),
    viewModel: NotificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    var showMarkAllDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar for success/error messages
    LaunchedEffect(state.successMessage, state.error) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    val sdf = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val todayFormatted = sdf.format(today.time)
    val yesterdayFormatted = sdf.format(yesterday.time)

    // Group notifications by date
    val groupedNotifications = remember(state.notifications) {
        state.notifications.groupBy { notification ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = notification.timestamp
            val notifDate = calendar.get(Calendar.DAY_OF_YEAR)
            val notifYear = calendar.get(Calendar.YEAR)

            val todayCalendar = Calendar.getInstance()
            val todayDate = todayCalendar.get(Calendar.DAY_OF_YEAR)
            val todayYear = todayCalendar.get(Calendar.YEAR)

            val yesterdayCalendar = Calendar.getInstance()
            yesterdayCalendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayDate = yesterdayCalendar.get(Calendar.DAY_OF_YEAR)
            val yesterdayYear = yesterdayCalendar.get(Calendar.YEAR)

            when {
                notifDate == todayDate && notifYear == todayYear -> "today"
                notifDate == yesterdayDate && notifYear == yesterdayYear -> "yesterday"
                else -> "older"
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Snackbar host at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            SnackbarHost(snackbarHostState)
        }

        // Main content
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading && state.notifications.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.notifications.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Belum ada notifikasi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PlusJakartaSans,
                        color = Grey700,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Notifikasi akan muncul ketika ada perubahan status surat",
                        fontSize = 14.sp,
                        fontFamily = PlusJakartaSans,
                        color = Grey600,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // Today's notifications
                    if (groupedNotifications["today"]?.isNotEmpty() == true) {
                        item {
                            Text(
                                text = "Hari Ini, $todayFormatted",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PlusJakartaSans,
                                color = Grey700,
                                modifier = Modifier.padding(bottom = 4.dp, top = 4.dp)
                            )
                        }
                        items(
                            items = groupedNotifications["today"] ?: emptyList(),
                            key = { it.id }
                        ) { notification ->
                            NotificationCard(
                                message = notification.message,
                                time = formatTime(notification.timestamp),
                                isRead = notification.isRead,
                                onClick = {
                                    viewModel.markAsRead(notification.id)
                                }
                            )
                        }
                    }

                    // Yesterday's notifications
                    if (groupedNotifications["yesterday"]?.isNotEmpty() == true) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Kemarin, $yesterdayFormatted",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PlusJakartaSans,
                                color = Grey700,
                                modifier = Modifier.padding(bottom = 4.dp, top = 4.dp)
                            )
                        }
                        items(
                            items = groupedNotifications["yesterday"] ?: emptyList(),
                            key = { it.id }
                        ) { notification ->
                            NotificationCard(
                                message = notification.message,
                                time = formatTime(notification.timestamp),
                                isRead = notification.isRead,
                                onClick = {
                                    viewModel.markAsRead(notification.id)
                                }
                            )
                        }
                    }

                    // Older notifications
                    if (groupedNotifications["older"]?.isNotEmpty() == true) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Sebelumnya",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PlusJakartaSans,
                                color = Grey700,
                                modifier = Modifier.padding(bottom = 4.dp, top = 4.dp)
                            )
                        }
                        items(
                            items = groupedNotifications["older"] ?: emptyList(),
                            key = { it.id }
                        ) { notification ->
                            NotificationCard(
                                title = notification.title,
                                message = notification.message,
                                time = formatDateTime(notification.timestamp),
                                isRead = notification.isRead,
                                onClick = {
                                    viewModel.markAsRead(notification.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button untuk menu
        if (state.notifications.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Overflow menu
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Tandai Semua Dibaca",
                                fontFamily = PlusJakartaSans
                            )
                        },
                        onClick = {
                            showMenu = false
                            showMarkAllDialog = true
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Hapus Semua",
                                fontFamily = PlusJakartaSans,
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showMenu = false
                            showClearDialog = true
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }

        // Dialogs
        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = {
                    Text(
                        text = "Hapus Semua Notifikasi?",
                        fontWeight = FontWeight.Bold,
                        fontFamily = PlusJakartaSans
                    )
                },
                text = {
                    Text(
                        text = "Semua notifikasi akan dihapus secara permanen.",
                        fontFamily = PlusJakartaSans
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearAllNotifications()
                            showClearDialog = false
                        }
                    ) {
                        Text(
                            text = "Hapus",
                            fontWeight = FontWeight.Bold,
                            fontFamily = PlusJakartaSans,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showClearDialog = false }
                    ) {
                        Text(
                            text = "Batal",
                            fontFamily = PlusJakartaSans
                        )
                    }
                }
            )
        }

        if (showMarkAllDialog) {
            AlertDialog(
                onDismissRequest = { showMarkAllDialog = false },
                title = {
                    Text(
                        text = "Tandai Semua Dibaca?",
                        fontWeight = FontWeight.Bold,
                        fontFamily = PlusJakartaSans
                    )
                },
                text = {
                    Text(
                        text = "Semua notifikasi akan ditandai sebagai telah dibaca.",
                        fontFamily = PlusJakartaSans
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.markAllAsRead()
                            showMarkAllDialog = false
                        }
                    ) {
                        Text(
                            text = "Tandai Dibaca",
                            fontWeight = FontWeight.Bold,
                            fontFamily = PlusJakartaSans
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showMarkAllDialog = false }
                    ) {
                        Text(
                            text = "Batal",
                            fontFamily = PlusJakartaSans
                        )
                    }
                }
            )
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("d MMM yyyy, HH:mm", Locale("id", "ID"))
    return sdf.format(Date(timestamp))
}