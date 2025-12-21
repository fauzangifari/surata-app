//package com.fauzangifari.surata.ui.screens.detail
//
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.emptyPreferences
//import com.fauzangifari.data.source.local.datastore.AuthPreferences
//import com.fauzangifari.domain.common.Resource
//import com.fauzangifari.domain.model.Letter
//import com.fauzangifari.domain.repository.LetterRepository
//import com.fauzangifari.domain.usecase.GetDetailLetterUseCase
//import io.mockk.coEvery
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Test
//
///**
// * WhiteBox Testing untuk DetailViewModel
// * Menggunakan teknik Basic Path Testing
// *
// * Fokus pengujian:
// * 1. Mengambil Detail Surat (getDetail)
// * 2. Error Handling
// */
//@OptIn(ExperimentalCoroutinesApi::class)
//class DetailViewModelTest {
//
//    private val testDispatcher = StandardTestDispatcher()
//
//    private lateinit var viewModel: DetailViewModel
//    private lateinit var mockGetDetailLetterUseCase: GetDetailLetterUseCase
//    private lateinit var mockAuthPreferences: AuthPreferences
//
//    private val mockLetterId = "letter123"
//    private val mockLetter = Letter(
//        id = mockLetterId,
//        applicantName = "John Doe",
//        applicantEmail = "john@example.com",
//        subject = "Izin Tidak Hadir",
//        endDate = "2025-01-20T10:00:00.000+08:00",
//        letterContent = "Saya tidak bisa hadir karena sakit",
//        isPrinted = false,
//        beginDate = "2025-01-15T08:00:00.000+08:00",
//        createdAt = "2025-01-10T00:00:00.000+08:00",
//        attachment = "https://example.com/file.pdf",
//        letterType = "DISPENSATION",
//        status = "APPROVED",
//        letterNumber = "001/2025/DISPENSATION",
//        cc = listOf("user1", "user2"),
//        reason = "Sakit"
//    )
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//        mockAuthPreferences = createMockAuthPreferences()
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    private fun createMockAuthPreferences(): AuthPreferences {
//        val mock = mockk<AuthPreferences>(relaxed = true)
//        every { mock.userName } returns flowOf("Test User")
//        every { mock.userEmail } returns flowOf("test@example.com")
//        every { mock.userRole } returns flowOf("student")
//        every { mock.userId } returns flowOf("user123")
//        every { mock.token } returns flowOf("mock_token")
//        coEvery { mock.getUserId() } returns "user123"
//        coEvery { mock.getToken() } returns "mock_token"
//        return mock
//    }
//
//    // =============================
//    // PENGUJIAN DETAIL SURAT
//    // =============================
//
//    /**
//     * PATH 1: Sukses - Mengambil Detail Surat dari Server
//     * Flow: letterId valid → API Success → Show Letter Detail
//     */
//    @Test
//    fun `test path 1 - get detail letter successfully`() = runTest {
//        // ARRANGE
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(letter = mockLetter)
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        viewModel.getDetail(mockLetterId)
//        advanceUntilIdle()
//
//        // ASSERT
//        val state = viewModel.state.value
//        assertFalse(state.isLoading)
//        assertNotNull(state.data)
//        assertEquals(mockLetterId, state.data?.id)
//        assertEquals("Izin Tidak Hadir", state.data?.subject)
//        assertEquals("APPROVED", state.data?.status)
//        assertNull(state.error)
//    }
//
//    /**
//     * PATH 2: Loading State - Saat Mengambil Data
//     * Flow: Call getDetail → Loading = true
//     */
//    @Test
//    fun `test path 2 - loading state should be true when fetching`() = runTest {
//        // ARRANGE
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(
//            letter = mockLetter,
//            delayLoading = true
//        )
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        viewModel.getDetail(mockLetterId)
//
//        // ASSERT - Check loading state before advanceUntilIdle
//        val stateBeforeComplete = viewModel.state.value
//        assertTrue(stateBeforeComplete.isLoading)
//
//        // Complete the operation
//        advanceUntilIdle()
//
//        // ASSERT - Check loading state after completion
//        val stateFinal = viewModel.state.value
//        assertFalse(stateFinal.isLoading)
//        assertNotNull(stateFinal.data)
//    }
//
//    /**
//     * PATH 3: Error - Gagal Mengambil Detail Surat (Network Error)
//     * Flow: letterId valid → API Error → Show Error Message
//     */
//    @Test
//    fun `test path 3 - network error should show error message`() = runTest {
//        // ARRANGE
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(
//            error = true,
//            errorMessage = "Tidak dapat menghubungi server"
//        )
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        viewModel.getDetail(mockLetterId)
//        advanceUntilIdle()
//
//        // ASSERT
//        val state = viewModel.state.value
//        assertFalse(state.isLoading)
//        assertNull(state.data)
//        assertNotNull(state.error)
//        assertEquals("Tidak dapat menghubungi server", state.error)
//    }
//
//    /**
//     * PATH 4: Error - Letter ID Tidak Ditemukan (404)
//     * Flow: letterId invalid → API 404 → Show Not Found Error
//     */
//    @Test
//    fun `test path 4 - letter not found should show error`() = runTest {
//        // ARRANGE
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(
//            error = true,
//            errorMessage = "Surat tidak ditemukan"
//        )
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        viewModel.getDetail("invalid_id")
//        advanceUntilIdle()
//
//        // ASSERT
//        val state = viewModel.state.value
//        assertFalse(state.isLoading)
//        assertNull(state.data)
//        assertEquals("Surat tidak ditemukan", state.error)
//    }
//
//    /**
//     * PATH 5: Sukses - Data dari Local Cache (Offline)
//     * Flow: letterId valid → Local Cache Hit → Show Cached Data → Fetch Remote
//     */
//    @Test
//    fun `test path 5 - should show cached data first then update with remote`() = runTest {
//        // ARRANGE
//        val cachedLetter = mockLetter.copy(subject = "Cached Subject")
//        val remoteLetter = mockLetter.copy(subject = "Remote Subject")
//
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(
//            localLetter = cachedLetter,
//            letter = remoteLetter,
//            emitLocalFirst = true
//        )
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        viewModel.getDetail(mockLetterId)
//        advanceUntilIdle()
//
//        // ASSERT - Should have final remote data
//        val state = viewModel.state.value
//        assertFalse(state.isLoading)
//        assertNotNull(state.data)
//        assertEquals("Remote Subject", state.data?.subject)
//    }
//
//    /**
//     * PATH 6: Sukses - Menampilkan Semua Field Detail Surat
//     * Flow: getDetail → Verify all fields are properly mapped
//     */
//    @Test
//    fun `test path 6 - should map all letter fields correctly`() = runTest {
//        // ARRANGE
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(letter = mockLetter)
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        viewModel.getDetail(mockLetterId)
//        advanceUntilIdle()
//
//        // ASSERT - Verify all fields
//        val letter = viewModel.state.value.data
//        assertNotNull(letter)
//        assertEquals("letter123", letter?.id)
//        assertEquals("John Doe", letter?.applicantName)
//        assertEquals("john@example.com", letter?.applicantEmail)
//        assertEquals("Izin Tidak Hadir", letter?.subject)
//        assertEquals("2025-01-20T10:00:00.000+08:00", letter?.endDate)
//        assertEquals("Saya tidak bisa hadir karena sakit", letter?.letterContent)
//        assertEquals(false, letter?.isPrinted)
//        assertEquals("2025-01-15T08:00:00.000+08:00", letter?.beginDate)
//        assertEquals("https://example.com/file.pdf", letter?.attachment)
//        assertEquals("DISPENSATION", letter?.letterType)
//        assertEquals("APPROVED", letter?.status)
//        assertEquals("001/2025/DISPENSATION", letter?.letterNumber)
//        assertEquals(2, letter?.cc?.size)
//        assertEquals("Sakit", letter?.reason)
//    }
//
//    /**
//     * PATH 7: User Name State - Memastikan User Name Tersedia
//     * Flow: ViewModel init → userNameState should flow
//     */
//    @Test
//    fun `test path 7 - user name state should be available`() = runTest {
//        // ARRANGE
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(letter = mockLetter)
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        advanceUntilIdle()
//
//        // ASSERT
//        val userName = viewModel.userNameState.value
//        assertEquals("Test User", userName)
//    }
//
//    /**
//     * PATH 8: Error - Exception Tidak Terduga
//     * Flow: getDetail → Unexpected Exception → Generic Error Message
//     */
//    @Test
//    fun `test path 8 - unexpected exception should show generic error`() = runTest {
//        // ARRANGE
//        mockGetDetailLetterUseCase = MockGetDetailLetterUseCase(
//            error = true,
//            errorMessage = "Terjadi kesalahan yang tidak terduga"
//        )
//
//        viewModel = DetailViewModel(
//            mockGetDetailLetterUseCase,
//            mockAuthPreferences
//        )
//
//        // ACT
//        viewModel.getDetail(mockLetterId)
//        advanceUntilIdle()
//
//        // ASSERT
//        val state = viewModel.state.value
//        assertNotNull(state.error)
//        assertEquals("Terjadi kesalahan yang tidak terduga", state.error)
//    }
//
//    // =============================
//    // MOCK CLASSES
//    // =============================
//
//    private class MockDataStore : DataStore<Preferences> {
//        override val data: Flow<Preferences> = flowOf(emptyPreferences())
//
//        override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
//            return emptyPreferences()
//        }
//    }
//
//    private class MockGetDetailLetterUseCase(
//        private val letter: Letter? = null,
//        private val localLetter: Letter? = null,
//        private val error: Boolean = false,
//        private val errorMessage: String = "Error",
//        private val delayLoading: Boolean = false,
//        private val emitLocalFirst: Boolean = false
//    ) : GetDetailLetterUseCase(MockLetterRepository()) {
//        override fun invoke(letterId: String) = flow {
//            emit(Resource.Loading())
//
//            if (delayLoading) {
//                // Keep in loading state for testing
//                kotlinx.coroutines.delay(100)
//            }
//
//            if (error) {
//                emit(Resource.Error(errorMessage))
//            } else {
//                // Emit local data first if available
//                if (emitLocalFirst && localLetter != null) {
//                    emit(Resource.Success(localLetter))
//                }
//
//                // Then emit remote data
//                if (letter != null) {
//                    emit(Resource.Success(letter))
//                }
//            }
//        }
//    }
//
//    private class MockLetterRepository : LetterRepository {
//        override suspend fun getLetters(): List<Letter> = emptyList()
//        override suspend fun getLettersByUserId(userId: String): List<Letter> = emptyList()
//        override suspend fun getLettersByUserIdFromLocal(): List<Letter> = emptyList()
//        override suspend fun getLetterById(letterId: String): Letter {
//            return Letter(
//                id = letterId,
//                applicantName = "Test",
//                applicantEmail = "test@test.com",
//                subject = "Test",
//                endDate = "",
//                letterContent = "",
//                isPrinted = false,
//                beginDate = "",
//                createdAt = "",
//                attachment = "",
//                letterType = "DISPENSATION",
//                status = "PENDING",
//                letterNumber = "001"
//            )
//        }
//        override suspend fun getLetterByIdFromLocal(letterId: String): Letter? = null
//        override suspend fun postLetter(reqLetter: com.fauzangifari.domain.model.ReqLetter): Letter {
//            throw NotImplementedError()
//        }
//        override suspend fun postPresignedUrl(reqPresigned: com.fauzangifari.domain.model.ReqPresigned): com.fauzangifari.domain.model.Presigned {
//            throw NotImplementedError()
//        }
//        override suspend fun saveLettersToLocal(letters: List<Letter>) {}
//        override suspend fun saveLetterToLocal(letter: Letter) {}
//        override suspend fun clearLocalCache() {}
//    }
//}
