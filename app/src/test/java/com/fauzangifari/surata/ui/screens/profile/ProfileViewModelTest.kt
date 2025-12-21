package com.fauzangifari.surata.ui.screens.profile

import android.util.Log
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.*
import com.fauzangifari.domain.repository.UserRepository
import com.fauzangifari.domain.usecase.GetUsersMeUseCase
import com.fauzangifari.domain.usecase.UpdateUserUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var getUsersMeUseCase: GetUsersMeUseCase
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var authPreferences: AuthPreferences
    private lateinit var mockUserRepository: UserRepository
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


        authPreferences = mockk(relaxed = true)
        mockUserRepository = mockk(relaxed = true)
        getUsersMeUseCase = GetUsersMeUseCase(mockUserRepository)
        updateUserUseCase = UpdateUserUseCase(mockUserRepository)

        coEvery { authPreferences.getUserId() } returns "default-user-id"
        coEvery { authPreferences.saveUserName(any()) } just Runs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
        clearAllMocks()
    }

    // Test Case 1: init → loadUserProfile → getUsersMeUseCase → Success (STUDENT role)
    @Test
    fun `test path 1 - init should load student profile successfully`() = runTest {
        // Arrange
        val mockUserMe = UserMe(
            id = "123",
            email = "student@school.com",
            name = "Test Student",
            image = null,
            role = UserRole.STUDENT,
            studentId = "S123",
            teacherId = null,
            student = Student(
                id = "S123",
                nisn = "1234567890",
                nipd = "0987654321",
                name = "Test Student",
                birthPlace = "Jakarta",
                birthDate = "2000-01-01",
                phoneNumber = "081234567890",
                userId = "123"
            ),
            teacher = null,
            secondaryEmail = "student@personal.com"
        )
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(mockUserMe)

        // Act
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val profile = viewModel.profile.value
        assertEquals("Test Student", profile.name)
        assertEquals("student@school.com", profile.schoolEmail)
        assertEquals("student@personal.com", profile.personalEmail)
        assertEquals("Jakarta", profile.placeOfBirth)
        assertEquals("081234567890", profile.phone)
        assertEquals("1234567890", profile.idNumber)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)
    }

    // Test Case 2: init → loadUserProfile → getUsersMeUseCase → Success (TEACHER role)
    @Test
    fun `test path 2 - init should load teacher profile successfully`() = runTest {
        // Arrange
        val mockUserMe = UserMe(
            id = "456",
            email = "teacher@school.com",
            name = "Test Teacher",
            image = null,
            role = UserRole.TEACHER,
            studentId = null,
            teacherId = "T456",
            student = null,
            teacher = Teacher(
                id = "T456",
                nip = "9876543210",
                birthPlace = "Bandung",
                birthDate = "1980-05-15",
                phone = "082345678901",
                userId = "456"
            ),
            secondaryEmail = "teacher@personal.com"
        )
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(mockUserMe)

        // Act
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val profile = viewModel.profile.value
        assertEquals("Test Teacher", profile.name)
        assertEquals("teacher@school.com", profile.schoolEmail)
        assertEquals("teacher@personal.com", profile.personalEmail)
        assertEquals("Bandung", profile.placeOfBirth)
        assertEquals("082345678901", profile.phone)
        assertEquals("9876543210", profile.idNumber)
        assertFalse(viewModel.isLoading.value)
    }

    // Test Case 3: init → loadUserProfile → getUsersMeUseCase → Success (OTHER role)
    @Test
    fun `test path 3 - init should load profile with other role successfully`() = runTest {
        // Arrange
        val mockUserMe = UserMe(
            id = "789",
            email = "admin@school.com",
            name = "Admin User",
            image = null,
            role = UserRole.UNKNOWN,
            studentId = null,
            teacherId = null,
            student = null,
            teacher = null,
            secondaryEmail = "admin@personal.com"
        )
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(mockUserMe)

        // Act
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val profile = viewModel.profile.value
        assertEquals("Admin User", profile.name)
        assertEquals("admin@school.com", profile.schoolEmail)
        assertEquals("admin@personal.com", profile.personalEmail)
        assertEquals("", profile.placeOfBirth)
        assertEquals("", profile.phone)
        assertEquals("", profile.idNumber)
    }

    // Test Case 4: init → loadUserProfile → getUsersMeUseCase → Error
    @Test
    fun `test path 4 - init should handle error when loading profile`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Error(errorMessage)

        // Act
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(errorMessage, viewModel.errorMessage.value)
        assertFalse(viewModel.isLoading.value)
    }

    // Test Case 5: onEditModeToggle(true) → Should enable edit mode
    @Test
    fun `test path 5 - onEditModeToggle should enable edit mode`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.onEditModeToggle(true)

        // Assert
        assertTrue(viewModel.isEditMode.value)
        assertNull(viewModel.nameError.value)
        assertNull(viewModel.emailError.value)
    }

    // Test Case 6: onEditModeToggle(false) → Should disable edit mode
    @Test
    fun `test path 6 - onEditModeToggle should disable edit mode and reset values`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Modified Name")
        viewModel.onPersonalEmailChange("modified@email.com")

        // Act
        viewModel.onEditModeToggle(false)

        // Assert
        assertFalse(viewModel.isEditMode.value)
        assertEquals(viewModel.profile.value.name, viewModel.editedName.value)
        assertEquals(viewModel.profile.value.personalEmail, viewModel.editedPersonalEmail.value)
    }

    // Test Case 7: onSaveClick → validateName (empty) → Should show error
    @Test
    fun `test path 7 - onSaveClick should show error for empty name`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("")

        // Act
        viewModel.onSaveClick()

        // Assert
        assertEquals("Nama tidak boleh kosong", viewModel.nameError.value)
        assertFalse(viewModel.showSaveDialog.value)
    }

    // Test Case 8: onSaveClick → validateName (less than 3) → Should show error
    @Test
    fun `test path 8 - onSaveClick should show error for name less than 3 characters`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Ab")

        // Act
        viewModel.onSaveClick()

        // Assert
        assertEquals("Nama minimal 3 karakter", viewModel.nameError.value)
        assertFalse(viewModel.showSaveDialog.value)
    }

    // Test Case 9: onSaveClick → validateName (more than 100) → Should show error
    @Test
    fun `test path 9 - onSaveClick should show error for name more than 100 characters`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("A".repeat(101))

        // Act
        viewModel.onSaveClick()

        // Assert
        assertEquals("Nama maksimal 100 karakter", viewModel.nameError.value)
        assertFalse(viewModel.showSaveDialog.value)
    }

    // Test Case 10: onSaveClick → validateEmail (invalid format) → Should show error
    @Test
    fun `test path 10 - onSaveClick should show error for invalid email format`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Valid Name")
        viewModel.onPersonalEmailChange("invalid-email")

        // Act
        viewModel.onSaveClick()

        // Assert
        assertEquals("Format email tidak valid", viewModel.emailError.value)
        assertFalse(viewModel.showSaveDialog.value)
    }

    // Test Case 11: onSaveClick → validateName & validateEmail (valid) → Should show save dialog
    @Test
    fun `test path 11 - onSaveClick should show save dialog for valid inputs`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Valid Name")
        viewModel.onPersonalEmailChange("valid@email.com")

        // Act
        viewModel.onSaveClick()

        // Assert
        assertNull(viewModel.nameError.value)
        assertNull(viewModel.emailError.value)
        assertTrue(viewModel.showSaveDialog.value)
    }

    // Test Case 12: onSaveClick → validateEmail (empty) → Should be valid
    @Test
    fun `test path 12 - onSaveClick should accept empty email as valid`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(createMockUserMe())
        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Valid Name")
        viewModel.onPersonalEmailChange("")

        // Act
        viewModel.onSaveClick()

        // Assert
        assertNull(viewModel.emailError.value)
        assertTrue(viewModel.showSaveDialog.value)
    }

    // Test Case 13: saveProfile → updateUserUseCase → Success
    @Test
    fun `test path 13 - saveProfile should update profile successfully`() = runTest {
        // Arrange
        val mockUserMe = createMockUserMe()
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(mockUserMe)
        coEvery { authPreferences.getUserId() } returns "123"
        coEvery { mockUserRepository.updateUser(any(), any(), any()) } returns Resource.Success(
            mockUserMe.copy(
                name = "Updated Name",
                secondaryEmail = "updated@email.com"
            )
        )
        coEvery { authPreferences.saveUserName(any()) } just Runs

        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Updated Name")
        viewModel.onPersonalEmailChange("updated@email.com")

        // Act
        var updatedProfile: UserProfile? = null
        viewModel.saveProfile { profile -> updatedProfile = profile }
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Updated Name", viewModel.profile.value.name)
        assertEquals("updated@email.com", viewModel.profile.value.personalEmail)
        assertFalse(viewModel.isEditMode.value)
        assertEquals("Profil berhasil diperbarui", viewModel.toastMessage.value)
        assertTrue(viewModel.isSuccess.value)
        assertNotNull(updatedProfile)
    }

    // Test Case 14: saveProfile → updateUserUseCase → Error
    @Test
    fun `test path 14 - saveProfile should handle error when updating profile`() = runTest {
        // Arrange
        val mockUserMe = createMockUserMe()
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(mockUserMe)
        coEvery { authPreferences.getUserId() } returns "123"
        coEvery { mockUserRepository.updateUser(any(), any(), any()) } returns Resource.Error("Update failed")

        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Updated Name")

        // Act
        viewModel.saveProfile { }
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Update failed", viewModel.toastMessage.value)
        assertFalse(viewModel.isSuccess.value)
    }

    // Test Case 15: saveProfile → getUserId returns null → Should show error
    @Test
    fun `test path 15 - saveProfile should handle missing user ID`() = runTest {
        // Arrange
        val mockUserMe = createMockUserMe()
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Success(mockUserMe)
        coEvery { authPreferences.getUserId() } returns null

        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEditModeToggle(true)
        viewModel.onNameChange("Updated Name")

        // Act
        viewModel.saveProfile { }
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(viewModel.toastMessage.value.contains("User ID tidak ditemukan"))
        assertFalse(viewModel.isSuccess.value)
    }

    // Test Case 16: retryLoadProfile → Should reload profile
    @Test
    fun `test path 16 - retryLoadProfile should reload user profile`() = runTest {
        // Arrange
        coEvery { mockUserRepository.getUsersMe() } returns Resource.Error("Initial error") andThen
                Resource.Success(createMockUserMe())

        viewModel = ProfileViewModel(getUsersMeUseCase, updateUserUseCase, authPreferences)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify initial error
        assertNotNull(viewModel.errorMessage.value)

        // Act
        viewModel.retryLoadProfile()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertNull(viewModel.errorMessage.value)
        assertEquals("Test Student", viewModel.profile.value.name)
    }

    // Helper function to create mock UserMe
    private fun createMockUserMe(): UserMe {
        return UserMe(
            id = "123",
            email = "student@school.com",
            name = "Test Student",
            image = null,
            role = UserRole.STUDENT,
            studentId = "S123",
            teacherId = null,
            student = Student(
                id = "S123",
                nisn = "1234567890",
                nipd = "0987654321",
                name = "Test Student",
                birthPlace = "Jakarta",
                birthDate = "2000-01-01",
                phoneNumber = "081234567890",
                userId = "123"
            ),
            teacher = null,
            secondaryEmail = "student@personal.com"
        )
    }
}

