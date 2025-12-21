package com.fauzangifari.surata.ui.screens.home

import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.Presigned
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.model.ReqPresigned
import com.fauzangifari.domain.model.UserMe
import com.fauzangifari.domain.repository.LetterRepository
import com.fauzangifari.domain.usecase.GetLetterByUserIdUseCase
import com.fauzangifari.domain.usecase.GetUsersMeUseCase
import com.fauzangifari.domain.usecase.GetAllUserUseCase
import com.fauzangifari.domain.usecase.PostLetterUseCase
import com.fauzangifari.domain.usecase.PostPresignedUrlUseCase
import com.fauzangifari.domain.model.UserRole
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * WhiteBox Testing untuk HomeViewModel
 * Menggunakan teknik Basic Path Testing
 *
 * Fokus pengujian:
 * 1. Pembuatan Surat (submitLetter)
 * 2. Riwayat Surat (getLettersByUserId)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: HomeViewModel
    private lateinit var mockGetLetterByUserIdUseCase: GetLetterByUserIdUseCase
    private lateinit var mockPostLetterUseCase: PostLetterUseCase
    private lateinit var mockGetUsersMeUseCase: GetUsersMeUseCase
    private lateinit var mockGetAllUserUseCase: GetAllUserUseCase
    private lateinit var mockPostPresignedUrlUseCase: PostPresignedUrlUseCase
    private lateinit var mockAuthPreferences: AuthPreferences

    private val mockLetter = Letter(
        id = "letter1",
        applicantName = "John Doe",
        applicantEmail = "john@example.com",
        subject = "Test Subject",
        endDate = "2025-01-20T10:00:00.000+08:00",
        letterContent = "Test Content",
        isPrinted = false,
        beginDate = "2025-01-15T08:00:00.000+08:00",
        createdAt = "2025-01-10T00:00:00.000+08:00",
        attachment = "",
        letterType = "DISPENSATION",
        status = "PENDING",
        letterNumber = "001/2025",
        cc = emptyList(),
        reason = "Test reason"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockAuthPreferences = createMockAuthPreferences()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createMockAuthPreferences(userId: String? = "user123"): AuthPreferences {
        val mock = mockk<AuthPreferences>(relaxed = true)
        every { mock.userName } returns flowOf("Test User")
        every { mock.userEmail } returns flowOf("test@example.com")
        every { mock.userRole } returns flowOf("student")
        every { mock.userId } returns flowOf(userId)
        every { mock.token } returns flowOf("mock_token")
        coEvery { mock.getUserId() } returns userId
        coEvery { mock.getToken() } returns "mock_token"
        return mock
    }

    private fun createMockUserMe() = UserMe(
        id = "user123",
        email = "test@example.com",
        name = "Test User",
        image = null,
        role = UserRole.STUDENT,
        studentId = "12345",
        teacherId = null,
        student = null,
        teacher = null,
        secondaryEmail = null
    )

    private fun setupViewModel(
        userId: String? = "user123",
        letters: List<Letter> = emptyList(),
        letterError: Boolean = false,
        letterErrorMessage: String = "Error",
        postSuccess: Boolean = true,
        postErrorMessage: String = "Error",
        emptyFlow: Boolean = false
    ) {
        mockAuthPreferences = createMockAuthPreferences(userId)
        mockPostLetterUseCase = MockPostLetterUseCase(postSuccess, postErrorMessage)
        mockGetLetterByUserIdUseCase = MockGetLetterByUserIdUseCase(letters, letterError, letterErrorMessage, emptyFlow)
        mockGetUsersMeUseCase = mockk<GetUsersMeUseCase>(relaxed = true)
        mockGetAllUserUseCase = mockk<GetAllUserUseCase>(relaxed = true)
        mockPostPresignedUrlUseCase = MockPostPresignedUrlUseCase()

        coEvery { mockGetUsersMeUseCase.invoke() } returns Resource.Success(createMockUserMe())
        coEvery { mockGetAllUserUseCase.invoke() } returns Resource.Success(emptyList())

        viewModel = HomeViewModel(
            mockGetLetterByUserIdUseCase,
            mockPostLetterUseCase,
            mockPostPresignedUrlUseCase,
            mockGetUsersMeUseCase,
            mockGetAllUserUseCase,
            mockAuthPreferences
        )
    }

    // =============================
    // PENGUJIAN PEMBUATAN SURAT
    // =============================

//    @Test
//    fun `test path 1 - empty letter type should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "")
//        viewModel.updateFormField(FormField.SUBJECT, "Test Subject")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        assertNotNull(formState.letterTypeError)
//        assertEquals("Jenis surat harus dipilih", formState.letterTypeError)
//    }
//
//    @Test
//    fun `test path 2 - empty subject should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Rekomendasi")
//        viewModel.updateFormField(FormField.SUBJECT, "")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        assertNotNull(formState.subjectError)
//        assertEquals("Judul surat harus diisi", formState.subjectError)
//    }
//
//    @Test
//    fun `test path 3 - empty begin date for dispensation should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Dispensasi")
//        viewModel.updateFormField(FormField.SUBJECT, "Izin Tidak Hadir")
//        viewModel.updateFormField(FormField.BEGIN_DATE, "")
//        viewModel.updateFormField(FormField.BEGIN_TIME, "08:00")
//        viewModel.updateFormField(FormField.END_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.END_TIME, "10:00")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        assertNotNull(formState.beginDateError)
//        assertEquals("Tanggal mulai harus diisi", formState.beginDateError)
//    }
//
//    @Test
//    fun `test path 4 - empty begin time for dispensation should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Dispensasi")
//        viewModel.updateFormField(FormField.SUBJECT, "Izin Tidak Hadir")
//        viewModel.updateFormField(FormField.BEGIN_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.BEGIN_TIME, "")
//        viewModel.updateFormField(FormField.END_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.END_TIME, "10:00")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        assertNotNull(formState.beginTimeError)
//        assertEquals("Waktu mulai harus diisi", formState.beginTimeError)
//    }
//
//    @Test
//    fun `test path 5 - empty end date for assignment should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Tugas")
//        viewModel.updateFormField(FormField.SUBJECT, "Tugas Mengajar")
//        viewModel.updateFormField(FormField.BEGIN_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.BEGIN_TIME, "08:00")
//        viewModel.updateFormField(FormField.END_DATE, "")
//        viewModel.updateFormField(FormField.END_TIME, "10:00")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        assertNotNull(formState.endDateError)
//        assertEquals("Tanggal selesai harus diisi", formState.endDateError)
//    }
//
//    @Test
//    fun `test path 6 - empty end time for assignment should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Tugas")
//        viewModel.updateFormField(FormField.SUBJECT, "Tugas Mengajar")
//        viewModel.updateFormField(FormField.BEGIN_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.BEGIN_TIME, "08:00")
//        viewModel.updateFormField(FormField.END_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.END_TIME, "")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        assertNotNull(formState.endTimeError)
//        assertEquals("Waktu selesai harus diisi", formState.endTimeError)
//    }
//
//    @Test
//    fun `test path 7 - same begin and end time should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Dispensasi")
//        viewModel.updateFormField(FormField.SUBJECT, "Izin Tidak Hadir")
//        viewModel.updateFormField(FormField.BEGIN_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.BEGIN_TIME, "08:00")
//        viewModel.updateFormField(FormField.END_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.END_TIME, "08:00")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        // Should have error on time fields when times are the same
//        assertNotNull("Expected beginTimeError when times are the same", formState.beginTimeError)
//        assertNotNull("Expected endTimeError when times are the same", formState.endTimeError)
//        assertTrue("Expected error message about same time",
//            formState.beginTimeError?.contains("tidak boleh sama") == true)
//    }
//
//    @Test
//    fun `test path 8 - end date before begin date should show validation error`() = runTest {
//        setupViewModel(emptyFlow = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Dispensasi")
//        viewModel.updateFormField(FormField.SUBJECT, "Izin Tidak Hadir")
//        viewModel.updateFormField(FormField.BEGIN_DATE, "20/01/2025")
//        viewModel.updateFormField(FormField.BEGIN_TIME, "08:00")
//        viewModel.updateFormField(FormField.END_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.END_TIME, "10:00")
//        viewModel.submitLetter()
//        advanceUntilIdle()
//
//        val formState = viewModel.formState.value
//        assertNotNull(formState.beginDateError)
//    }
//
//    @Test
//    fun `test path 9 - create recommendation letter successfully`() = runTest {
//        setupViewModel(emptyFlow = true, postSuccess = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Rekomendasi")
//        viewModel.updateFormField(FormField.SUBJECT, "Rekomendasi Beasiswa")
//        viewModel.submitLetter()
//
//        // Wait for the flow to emit loading and success
//        testScheduler.advanceTimeBy(100)
//        advanceUntilIdle()
//
//        val postState = viewModel.postLetterState.value
//        // Before reset happens, state should show success
//        assertTrue("Expected success but got: isLoading=${postState.isLoading}, isSuccess=${postState.isSuccess}, error=${postState.error}",
//            postState.isSuccess || (!postState.isLoading && postState.error == null))
//    }
//
//    @Test
//    fun `test path 10 - create active statement letter successfully`() = runTest {
//        setupViewModel(emptyFlow = true, postSuccess = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Keterangan Aktif")
//        viewModel.updateFormField(FormField.SUBJECT, "Keterangan Aktif Kuliah")
//        viewModel.submitLetter()
//
//        // Wait for the flow to emit loading and success
//        testScheduler.advanceTimeBy(100)
//        advanceUntilIdle()
//
//        val postState = viewModel.postLetterState.value
//        // Before reset happens, state should show success
//        assertTrue("Expected success but got: isLoading=${postState.isLoading}, isSuccess=${postState.isSuccess}, error=${postState.error}",
//            postState.isSuccess || (!postState.isLoading && postState.error == null))
//    }
//
//    @Test
//    fun `test path 11 - create dispensation letter successfully`() = runTest {
//        setupViewModel(emptyFlow = true, postSuccess = true)
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Dispensasi")
//        viewModel.updateFormField(FormField.SUBJECT, "Izin Tidak Hadir")
//        viewModel.updateFormField(FormField.BEGIN_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.BEGIN_TIME, "08:00")
//        viewModel.updateFormField(FormField.END_DATE, "15/01/2025")
//        viewModel.updateFormField(FormField.END_TIME, "10:00")
//        viewModel.updateFormField(FormField.DESCRIPTION, "Sakit")
//        viewModel.submitLetter()
//
//        testScheduler.advanceTimeBy(100)
//        advanceUntilIdle()
//
//        val postState = viewModel.postLetterState.value
//        assertTrue("Expected success but got: isLoading=${postState.isLoading}, isSuccess=${postState.isSuccess}, error=${postState.error}",
//            postState.isSuccess || (!postState.isLoading && postState.error == null))
//    }
//
//    @Test
//    fun `test path 12 - api error should show error message`() = runTest {
//        setupViewModel(emptyFlow = true, postSuccess = false, postErrorMessage = "Server error")
//
//        viewModel.updateFormField(FormField.SELECTED_LETTER, "Surat Rekomendasi")
//        viewModel.updateFormField(FormField.SUBJECT, "Rekomendasi Beasiswa")
//        viewModel.updateFormField(FormField.IS_PRINTED, true)
//        viewModel.submitLetter()
//
//        testScheduler.advanceTimeBy(100)
//        advanceUntilIdle()
//
//        val postState = viewModel.postLetterState.value
//        assertTrue("Expected error or reset state but got: isLoading=${postState.isLoading}, isSuccess=${postState.isSuccess}, error=${postState.error}",
//            !postState.isSuccess)
//    }


    @Test
    fun `test path 13 - get letters by user id successfully`() = runTest {
        val letters = listOf(mockLetter)
        setupViewModel(letters = letters)

        advanceUntilIdle()

        val letterState = viewModel.letterState.value
        assertEquals(1, letterState.data.size)
        assertEquals("Test Subject", letterState.data[0].subject)
    }

    @Test
    fun `test path 14 - empty user id should show error`() = runTest {
        setupViewModel(userId = null, emptyFlow = true)

        advanceUntilIdle()

        val letterState = viewModel.letterState.value
        assertEquals("User ID tidak ditemukan.", letterState.error)
    }

    @Test
    fun `test path 15 - api error should show error message when getting letters`() = runTest {
        setupViewModel(letterError = true, letterErrorMessage = "Network error")

        advanceUntilIdle()

        val letterState = viewModel.letterState.value
        assertEquals("Network error", letterState.error)
    }

    @Test
    fun `test path 16 - http 404 should show not found message`() = runTest {
        setupViewModel(letterError = true, letterErrorMessage = "HTTP 404 Not Found")

        advanceUntilIdle()

        val letterState = viewModel.letterState.value
        assertEquals("Data surat tidak ditemukan.", letterState.error)
    }

    @Test
    fun `test path 17 - refresh letters successfully`() = runTest {
        val letters = listOf(mockLetter)
        setupViewModel(letters = letters)

        advanceUntilIdle()
        viewModel.refreshLetters()
        advanceUntilIdle()

        val letterState = viewModel.letterState.value
        assertFalse(letterState.isRefreshing)
        assertEquals(1, letterState.data.size)
    }

    @Test
    fun `test path 18 - refresh with null user id should show error`() = runTest {
        setupViewModel(userId = null, emptyFlow = true)

        advanceUntilIdle()
        viewModel.refreshLetters()
        advanceUntilIdle()

        val letterState = viewModel.letterState.value
        assertEquals("User ID tidak ditemukan.", letterState.error)
    }

    // =============================
    // MOCK CLASSES
    // =============================

    private class MockPostLetterUseCase(
        private val success: Boolean = true,
        private val errorMessage: String = "Error"
    ) : PostLetterUseCase(MockLetterRepository()) {
        override fun invoke(reqLetter: ReqLetter) = flow {
            emit(Resource.Loading())
            if (success) {
                val letter = Letter(
                    id = "letter1",
                    applicantName = "John Doe",
                    applicantEmail = "john@example.com",
                    subject = reqLetter.subject ?: "",
                    endDate = reqLetter.endDate ?: "",
                    letterContent = "Content",
                    isPrinted = reqLetter.isPrinted,
                    beginDate = reqLetter.beginDate ?: "",
                    createdAt = "2025-01-10T00:00:00.000+08:00",
                    attachment = reqLetter.attachment ?: "",
                    letterType = reqLetter.letterType,
                    status = "PENDING",
                    letterNumber = "001/2025"
                )
                emit(Resource.Success(letter))
            } else {
                emit(Resource.Error(errorMessage))
            }
        }
    }

    private class MockGetLetterByUserIdUseCase(
        private val letters: List<Letter> = emptyList(),
        private val error: Boolean = false,
        private val errorMessage: String = "Error",
        private val emptyFlow: Boolean = false
    ) : GetLetterByUserIdUseCase(MockLetterRepository()) {
        override fun invoke(userId: String) = flow {
            if (emptyFlow) return@flow
            emit(Resource.Loading())
            if (error) {
                emit(Resource.Error(errorMessage))
            } else {
                emit(Resource.Success(letters))
            }
        }
    }

    private class MockPostPresignedUrlUseCase : PostPresignedUrlUseCase(MockLetterRepository())


    private class MockLetterRepository : LetterRepository {
        override suspend fun getLetters(): List<Letter> = emptyList()
        override suspend fun getLettersByUserId(userId: String): List<Letter> = emptyList()
        override suspend fun getLettersByUserIdFromLocal(): List<Letter> = emptyList()
        override suspend fun getLetterById(letterId: String): Letter {
            return Letter(
                id = letterId,
                applicantName = "Test",
                applicantEmail = "test@test.com",
                subject = "Test",
                endDate = "",
                letterContent = "",
                isPrinted = false,
                beginDate = "",
                createdAt = "",
                attachment = "",
                letterType = "DISPENSATION",
                status = "PENDING",
                letterNumber = "001"
            )
        }
        override suspend fun getLetterByIdFromLocal(letterId: String): Letter? = null
        override suspend fun postLetter(reqLetter: ReqLetter): Letter {
            return Letter(
                id = "letter1",
                applicantName = "John Doe",
                applicantEmail = "john@example.com",
                subject = reqLetter.subject ?: "",
                endDate = reqLetter.endDate ?: "",
                letterContent = "Content",
                isPrinted = reqLetter.isPrinted,
                beginDate = reqLetter.beginDate ?: "",
                createdAt = "2025-01-10T00:00:00.000+08:00",
                attachment = reqLetter.attachment ?: "",
                letterType = reqLetter.letterType,
                status = "PENDING",
                letterNumber = "001/2025"
            )
        }
        override suspend fun postPresignedUrl(reqPresigned: ReqPresigned): Presigned {
            return Presigned(url = "http://mock.url")
        }
        override suspend fun saveLettersToLocal(letters: List<Letter>) {}
        override suspend fun saveLetterToLocal(letter: Letter) {}
        override suspend fun clearLocalCache() {}
    }
}
