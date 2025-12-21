package com.fauzangifari.surata.ui.screens.notification

import android.util.Log
import com.fauzangifari.domain.model.Notification
import com.fauzangifari.domain.repository.NotificationRepository
import com.fauzangifari.domain.usecase.DeleteAllNotificationsUseCase
import com.fauzangifari.domain.usecase.GetAllNotificationsUseCase
import com.fauzangifari.domain.usecase.MarkAllNotificationsAsReadUseCase
import com.fauzangifari.domain.usecase.MarkNotificationAsReadUseCase
import com.fauzangifari.domain.usecase.SaveNotificationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationViewModelTest {

    private lateinit var viewModel: NotificationViewModel
    private lateinit var mockNotificationRepository: NotificationRepository
    private lateinit var getAllNotificationsUseCase: GetAllNotificationsUseCase
    private lateinit var markNotificationAsReadUseCase: MarkNotificationAsReadUseCase
    private lateinit var markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase
    private lateinit var deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase
    private lateinit var saveNotificationUseCase: SaveNotificationUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mock Android Log class
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0

        mockNotificationRepository = mockk(relaxed = true)
        getAllNotificationsUseCase = GetAllNotificationsUseCase(mockNotificationRepository)
        markNotificationAsReadUseCase = MarkNotificationAsReadUseCase(mockNotificationRepository)
        markAllNotificationsAsReadUseCase = MarkAllNotificationsAsReadUseCase(mockNotificationRepository)
        deleteAllNotificationsUseCase = DeleteAllNotificationsUseCase(mockNotificationRepository)
        saveNotificationUseCase = SaveNotificationUseCase(mockNotificationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    // Test Case 1: init → loadNotifications → getAllNotificationsUseCase → Success with notifications
    @Test
    fun `test path 1 - init should load notifications successfully`() = runTest {
        // Arrange
        val mockNotifications = listOf(
            Notification(
                id = "1",
                title = "Test Notification",
                message = "Test Message",
                data = "{}",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
        )
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(mockNotifications)

        // Act
        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals(1, state.notifications.size)
        assertEquals("Test Notification", state.notifications[0].title)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    // Test Case 2: init → loadNotifications → getAllNotificationsUseCase → Success with empty list
    @Test
    fun `test path 2 - init should load empty notifications list successfully`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())

        // Act
        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state.notifications.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    // Test Case 3: init → loadNotifications → getAllNotificationsUseCase → Error
    @Test
    fun `test path 3 - init should handle error when loading notifications`() = runTest {
        // Arrange
        val errorMessage = "Database error"
        every { mockNotificationRepository.getAllNotifications() } returns flow {
            throw Exception(errorMessage)
        }

        // Act
        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state.notifications.isEmpty())
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    // Test Case 4: markAsRead → markNotificationAsReadUseCase → Success
    @Test
    fun `test path 4 - markAsRead should mark notification as read successfully`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())
        coEvery { mockNotificationRepository.markAsRead(any()) } returns Unit

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.markAsRead("notification-id")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { mockNotificationRepository.markAsRead("notification-id") }
    }

    // Test Case 5: markAsRead → markNotificationAsReadUseCase → Error
    @Test
    fun `test path 5 - markAsRead should handle error when marking as read`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())
        coEvery { mockNotificationRepository.markAsRead(any()) } throws Exception("Failed to mark as read")

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.markAsRead("notification-id")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { mockNotificationRepository.markAsRead("notification-id") }
        // No error should be propagated to state in this function
    }

    // Test Case 6: markAllAsRead → markAllNotificationsAsReadUseCase → Success
    @Test
    fun `test path 6 - markAllAsRead should mark all notifications as read successfully`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())
        coEvery { mockNotificationRepository.markAllAsRead() } returns Unit

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.markAllAsRead()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals("Semua notifikasi telah ditandai sebagai dibaca", state.successMessage)
        coVerify { mockNotificationRepository.markAllAsRead() }
    }

    // Test Case 7: markAllAsRead → markAllNotificationsAsReadUseCase → Error
    @Test
    fun `test path 7 - markAllAsRead should handle error when marking all as read`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())
        coEvery { mockNotificationRepository.markAllAsRead() } throws Exception("Failed to mark all")

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.markAllAsRead()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals("Gagal menandai semua notifikasi", state.error)
        coVerify { mockNotificationRepository.markAllAsRead() }
    }

    // Test Case 8: clearAllNotifications → deleteAllNotificationsUseCase → Success
    @Test
    fun `test path 8 - clearAllNotifications should delete all notifications successfully`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())
        coEvery { mockNotificationRepository.deleteAllNotifications() } returns Unit

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.clearAllNotifications()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals("Semua notifikasi telah dihapus", state.successMessage)
        coVerify { mockNotificationRepository.deleteAllNotifications() }
    }

    // Test Case 9: clearAllNotifications → deleteAllNotificationsUseCase → Error
    @Test
    fun `test path 9 - clearAllNotifications should handle error when deleting all`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())
        coEvery { mockNotificationRepository.deleteAllNotifications() } throws Exception("Failed to delete")

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.clearAllNotifications()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals("Gagal menghapus semua notifikasi", state.error)
        coVerify { mockNotificationRepository.deleteAllNotifications() }
    }

    // Test Case 10: clearMessages → Should clear success and error messages
    @Test
    fun `test path 10 - clearMessages should clear all messages`() = runTest {
        // Arrange
        every { mockNotificationRepository.getAllNotifications() } returns flowOf(emptyList())
        coEvery { mockNotificationRepository.deleteAllNotifications() } returns Unit

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Set a success message first
        viewModel.clearAllNotifications()
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.clearMessages()

        // Assert
        val state = viewModel.state.value
        assertNull(state.successMessage)
        assertNull(state.error)
    }

    // Test Case 11: refresh → loadNotifications → getAllNotificationsUseCase → Success
    @Test
    fun `test path 11 - refresh should reload notifications successfully`() = runTest {
        // Arrange
        val initialNotifications = listOf(
            Notification("1", "Initial", "Message", "{}", System.currentTimeMillis(), false)
        )
        val refreshedNotifications = listOf(
            Notification("1", "Initial", "Message", "{}", System.currentTimeMillis(), false),
            Notification("2", "New", "Message", "{}", System.currentTimeMillis(), false)
        )

        every { mockNotificationRepository.getAllNotifications() } returnsMany listOf(
            flowOf(initialNotifications),
            flowOf(refreshedNotifications)
        )

        viewModel = NotificationViewModel(
            getAllNotificationsUseCase,
            markNotificationAsReadUseCase,
            markAllNotificationsAsReadUseCase,
            deleteAllNotificationsUseCase,
            saveNotificationUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals(2, state.notifications.size)
        assertFalse(state.isLoading)
    }
}

