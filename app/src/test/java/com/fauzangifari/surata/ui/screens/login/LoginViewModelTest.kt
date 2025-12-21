package com.fauzangifari.surata.ui.screens.login

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Auth
import com.fauzangifari.domain.model.User
import com.fauzangifari.domain.model.UserMe
import com.fauzangifari.domain.model.UserRole
import com.fauzangifari.domain.repository.AuthRepository
import com.fauzangifari.domain.repository.FCMRepository
import com.fauzangifari.domain.repository.UserRepository
import com.fauzangifari.domain.usecase.GetUsersMeUseCase
import com.fauzangifari.domain.usecase.PostSignInUseCase
import com.fauzangifari.domain.usecase.SaveFCMTokenUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    private fun createMockUserRepository(userMeResult: Resource<UserMe>): UserRepository {
        return object : UserRepository {
            override suspend fun getUsersMe(): Resource<UserMe> = userMeResult
            override suspend fun getAllUsers(): Resource<List<User>> = Resource.Error("Not implemented")
            override suspend fun updateUser(
                usersId: String,
                name: String?,
                secondaryEmail: String?
            ): Resource<UserMe> {
                return Resource.Error("Not implemented")
            }
        }
    }

    private fun createMockFCMRepository(): FCMRepository {
        return object : FCMRepository {
            override suspend fun saveToken(token: String): Resource<String> = Resource.Success(token)
            override suspend fun deleteToken(tokenId: String): Resource<Unit> = Resource.Success(Unit)
        }
    }

    private fun createMockAuthPreferences(): AuthPreferences {
        return AuthPreferences(mockk<DataStore<Preferences>>(relaxed = true))
    }

    // Test Case 1: Email valid → Password valid → API Call → Success
    @Test
    fun `test path 1 - valid email and password should login successfully`() = runTest {
        // Arrange
        val mockUser = User(
            id = "123",
            email = "user@test.com",
            image = null,
            emailVerified = true,
            secondaryEmail = null,
            name = "Test User"
        )
        val mockAuth = Auth(
            token = "mock_token",
            user = mockUser
        )
        val mockUserMe = UserMe(
            id = "123",
            email = "user@test.com",
            name = "Test User",
            image = null,
            role = UserRole.STUDENT,
            studentId = "S123",
            teacherId = null,
            student = null,
            teacher = null,
            secondaryEmail = null
        )

        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Success(mockAuth)
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Success(mockUserMe))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act
        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("Password123!")

        // Assert validasi berhasil
        assertNull(viewModel.emailError.value)
        assertNull(viewModel.passwordError.value)

        // Act - panggil login
        viewModel.login()
        advanceUntilIdle()

        // Assert - login berhasil
        assertTrue("Login state should be Success", viewModel.loginState.value is Resource.Success)
        assertTrue("User should be logged in", viewModel.isLoggedIn.value)
        println("✅ Test Case 1 PASSED: Valid credentials → API Success → Login successfully")
    }

    // Test Case 2: Email valid → Password valid → API Call → Error (Invalid Credentials)
    @Test
    fun `test path 2 - valid email but invalid password should return error`() = runTest {
        // Arrange
        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Error("Invalid credentials")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Error("Not called"))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act
        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("wrongpassword")

        // Assert - validasi input berhasil (format benar)
        assertNull("Email should be valid", viewModel.emailError.value)
        assertNull("Password should be valid (not empty)", viewModel.passwordError.value)

        // Act - panggil login
        viewModel.login()
        advanceUntilIdle()

        // Assert - API mengembalikan error
        assertFalse("User should not be logged in", viewModel.isLoggedIn.value)
        assertTrue("Login state should be Error", viewModel.loginState.value is Resource.Error)
        assertEquals("Invalid credentials", (viewModel.loginState.value as Resource.Error).message)
        println("✅ Test Case 2 PASSED: Valid input → API Error → Invalid credentials")
    }

    // Test Case 3: Email invalid format → Validation error → Stop (No API Call)
    @Test
    fun `test path 3 - invalid email format should show validation error`() = runTest {
        // Arrange
        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Success(Auth("", User(null, null, null, null, null, null)))
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Error("Not called"))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act
        viewModel.onEmailChange("invalid-email")
        viewModel.onPasswordChange("Password123!")

        // Assert - validasi email gagal
        assertEquals("Format email tidak valid", viewModel.emailError.value)
        assertNull("Password should be valid", viewModel.passwordError.value)

        // Act - panggil login
        viewModel.login()
        advanceUntilIdle()

        // Assert - tidak ada perubahan state, API tidak dipanggil
        assertFalse("User should not be logged in", viewModel.isLoggedIn.value)
        assertTrue("Login state should remain Idle", viewModel.loginState.value is Resource.Idle)
        println("✅ Test Case 3 PASSED: Invalid email format → Validation error → No API call")
    }

    // Test Case 4: Email empty → Validation error
    @Test
    fun `test path 4 - empty email should show validation error`() = runTest {
        // Arrange
        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Success(Auth("", User(null, null, null, null, null, null)))
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Error("Not called"))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("Password123!")

        // Assert validasi
        assertEquals("Email tidak boleh kosong", viewModel.emailError.value)
        assertNull(viewModel.passwordError.value)

        viewModel.login()
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.isLoggedIn.value)
        println("✅ Test Case 4 PASSED: Empty email shows validation error")
    }

    // Test Case 5: Password empty → Validation error → Stop (No API Call)
    @Test
    fun `test path 5 - empty password should show validation error`() = runTest {
        // Arrange
        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Success(Auth("", User(null, null, null, null, null, null)))
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Error("Not called"))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act
        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("")

        // Assert - validasi password gagal
        assertNull("Email should be valid", viewModel.emailError.value)
        assertEquals("Password tidak boleh kosong", viewModel.passwordError.value)

        // Act - panggil login
        viewModel.login()
        advanceUntilIdle()

        // Assert - API tidak dipanggil
        assertFalse("User should not be logged in", viewModel.isLoggedIn.value)
        assertTrue("Login state should remain Idle", viewModel.loginState.value is Resource.Idle)
        println("✅ Test Case 5 PASSED: Empty password → Validation error → No API call")
    }

    // Test Case 6: Both empty → Validation error
    @Test
    fun `test path 6 - empty email and password should show validation errors`() = runTest {
        // Arrange
        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Success(Auth("", User(null, null, null, null, null, null)))
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Error("Not called"))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")
        viewModel.login()
        advanceUntilIdle()

        // Assert
        assertEquals("Email tidak boleh kosong", viewModel.emailError.value)
        assertEquals("Password tidak boleh kosong", viewModel.passwordError.value)
        assertFalse(viewModel.isLoggedIn.value)
        println("✅ Test Case 6 PASSED: Empty email and password show validation errors")
    }

    // Test Case 7: Email valid → Password valid → API Call → Network Error
    @Test
    fun `test path 7 - network error should return error message`() = runTest {
        // Arrange
        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Error("Network error")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Error("Not called"))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act
        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("Password123!")

        // Assert - validasi berhasil
        assertNull("Email should be valid", viewModel.emailError.value)
        assertNull("Password should be valid", viewModel.passwordError.value)

        // Act - panggil login
        viewModel.login()
        advanceUntilIdle()

        // Assert - API dipanggil tapi gagal karena network
        assertFalse("User should not be logged in", viewModel.isLoggedIn.value)
        assertTrue("Login state should be Error", viewModel.loginState.value is Resource.Error)
        assertEquals("Network error", (viewModel.loginState.value as Resource.Error).message)
        println("✅ Test Case 7 PASSED: Valid input → API Call → Network error")
    }

    // Test Case 8: Toggle password visibility
    @Test
    fun `test path 8 - toggle password visibility should change state`() = runTest {
        // Arrange
        val mockSignInUseCase = PostSignInUseCase(
            authRepository = object : AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<Auth> {
                    return Resource.Success(Auth("", User(null, null, null, null, null, null)))
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        )

        viewModel = LoginViewModel(
            mockSignInUseCase,
            GetUsersMeUseCase(createMockUserRepository(Resource.Error("Not called"))),
            createMockAuthPreferences(),
            SaveFCMTokenUseCase(createMockFCMRepository())
        )

        // Act & Assert
        assertFalse(viewModel.passwordVisible.value)
        viewModel.togglePasswordVisibility()
        assertTrue(viewModel.passwordVisible.value)
        viewModel.togglePasswordVisibility()
        assertFalse(viewModel.passwordVisible.value)
        println("✅ Test Case 8 PASSED: Toggle password visibility works correctly")
    }
}
