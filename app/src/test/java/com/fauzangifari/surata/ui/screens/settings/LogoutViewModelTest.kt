package com.fauzangifari.surata.ui.screens.settings

import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.usecase.PostSignOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutViewModelTest {

    private lateinit var mockSignOutUseCase: PostSignOutUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test Case 1: Logout API Success
    @Test
    fun `test path 1 - logout successfully from server`() = runTest {
        // Arrange
        mockSignOutUseCase = object : PostSignOutUseCase(
            authRepository = object : com.fauzangifari.domain.repository.AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<com.fauzangifari.domain.model.Auth> {
                    return Resource.Error("Not implemented")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Success(true)
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        ) {}

        val result = mockSignOutUseCase(true)

        assertTrue(result is Resource.Success)
        assertEquals(true, (result as Resource.Success).data)
        println("✅ Test Case 1 PASSED: Logout successfully from server")
    }

    // Test Case 2: Logout API Error - Network Error
    @Test
    fun `test path 2 - logout fails with network error`() = runTest {
        // Arrange
        mockSignOutUseCase = object : PostSignOutUseCase(
            authRepository = object : com.fauzangifari.domain.repository.AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<com.fauzangifari.domain.model.Auth> {
                    return Resource.Error("Not implemented")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Error("Network error")
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        ) {}

        val result = mockSignOutUseCase(true)

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
        println("✅ Test Case 2 PASSED: Logout fails with network error")
    }

    // Test Case 3: Logout API Error - Server Error
    @Test
    fun `test path 3 - logout fails with server error`() = runTest {
        // Arrange
        mockSignOutUseCase = object : PostSignOutUseCase(
            authRepository = object : com.fauzangifari.domain.repository.AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<com.fauzangifari.domain.model.Auth> {
                    return Resource.Error("Not implemented")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Error("Server error: Internal server error")
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        ) {}

        val result = mockSignOutUseCase(true)

        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message?.contains("Server error") == true)
        println("✅ Test Case 3 PASSED: Logout fails with server error")
    }

    // Test Case 4: Logout with success parameter false
    @Test
    fun `test path 4 - logout with success false parameter`() = runTest {
        // Arrange
        mockSignOutUseCase = object : PostSignOutUseCase(
            authRepository = object : com.fauzangifari.domain.repository.AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<com.fauzangifari.domain.model.Auth> {
                    return Resource.Error("Not implemented")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return if (success) Resource.Success(true) else Resource.Error("Logout not successful")
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        ) {}

        val result = mockSignOutUseCase(false)

        assertTrue(result is Resource.Error)
        println("✅ Test Case 4 PASSED: Logout with success false parameter")
    }

    // Test Case 5: Logout with exception handling
    @Test
    fun `test path 5 - logout handles exception correctly`() = runTest {
        // Arrange
        mockSignOutUseCase = object : PostSignOutUseCase(
            authRepository = object : com.fauzangifari.domain.repository.AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<com.fauzangifari.domain.model.Auth> {
                    return Resource.Error("Not implemented")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    throw Exception("Unexpected error occurred")
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        ) {}

        val result = mockSignOutUseCase(true)

        assertTrue(result is Resource.Error)
        assertEquals("Unexpected error occurred", (result as Resource.Error).message)
        println("✅ Test Case 5 PASSED: Logout handles exception correctly")
    }

    // Test Case 6: Logout with timeout simulation
    @Test
    fun `test path 6 - logout handles timeout error`() = runTest {
        // Arrange
        mockSignOutUseCase = object : PostSignOutUseCase(
            authRepository = object : com.fauzangifari.domain.repository.AuthRepository {
                override suspend fun signIn(email: String, password: String): Resource<com.fauzangifari.domain.model.Auth> {
                    return Resource.Error("Not implemented")
                }
                override suspend fun signOut(success: Boolean): Resource<Boolean> {
                    return Resource.Error("Request timeout")
                }
                override suspend fun getSession(): Resource<com.fauzangifari.domain.model.Session> {
                    return Resource.Error("Not implemented")
                }
            }
        ) {}

        val result = mockSignOutUseCase(true)

        assertTrue(result is Resource.Error)
        assertEquals("Request timeout", (result as Resource.Error).message)
        println("✅ Test Case 6 PASSED: Logout handles timeout error")
    }
}

