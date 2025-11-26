package com.fauzangifari.surata.ui.screens.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.domain.model.Notification
import com.fauzangifari.domain.usecase.DeleteAllNotificationsUseCase
import com.fauzangifari.domain.usecase.GetAllNotificationsUseCase
import com.fauzangifari.domain.usecase.MarkAllNotificationsAsReadUseCase
import com.fauzangifari.domain.usecase.MarkNotificationAsReadUseCase
import com.fauzangifari.domain.usecase.SaveNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.UUID

data class NotificationState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class NotificationViewModel(
    private val getAllNotificationsUseCase: GetAllNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state: StateFlow<NotificationState> = _state.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            Log.d(TAG, "Loading notifications...")

            getAllNotificationsUseCase()
                .catch { e ->
                    Log.e(TAG, "Error loading notifications: ${e.message}", e)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { notifications ->
                    Log.d(TAG, "Received ${notifications.size} notifications from database")
                    notifications.forEach { notif ->
                        Log.d(TAG, "Notification: ${notif.title} - ${notif.message}")
                    }
                    _state.value = _state.value.copy(
                        notifications = notifications,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                markNotificationAsReadUseCase(notificationId)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to mark notification as read: ${e.message}", e)
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                markAllNotificationsAsReadUseCase()
                _state.value = _state.value.copy(successMessage = "Semua notifikasi telah ditandai sebagai dibaca")
                Log.d(TAG, "All notifications marked as read")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to mark all notifications as read: ${e.message}", e)
                _state.value = _state.value.copy(error = "Gagal menandai semua notifikasi")
            }
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            try {
                deleteAllNotificationsUseCase()
                _state.value = _state.value.copy(successMessage = "Semua notifikasi telah dihapus")
                Log.d(TAG, "All notifications cleared")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to clear all notifications: ${e.message}", e)
                _state.value = _state.value.copy(error = "Gagal menghapus semua notifikasi")
            }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(successMessage = null, error = null)
    }

    fun refresh() {
        loadNotifications()
    }

    companion object {
        private const val TAG = "NotificationViewModel"
    }
}
