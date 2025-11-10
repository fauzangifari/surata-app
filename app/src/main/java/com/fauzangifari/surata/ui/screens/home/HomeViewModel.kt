package com.fauzangifari.surata.ui.screens.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.model.ReqPresigned
import com.fauzangifari.domain.model.Student
import com.fauzangifari.domain.usecase.GetLetterByUserIdUseCase
import com.fauzangifari.domain.usecase.GetStudentUseCase
import com.fauzangifari.domain.usecase.PostLetterUseCase
import com.fauzangifari.domain.usecase.PostPresignedUrlUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class HomeViewModel(
    private val getLetterByUserIdUseCase: GetLetterByUserIdUseCase,
    private val postLetterUseCase: PostLetterUseCase,
    private val getStudentUseCase: GetStudentUseCase,
    private val postPresignedUrlUseCase: PostPresignedUrlUseCase,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    // region Constants & Properties
    private companion object {
        private const val DELAY_BEFORE_RESET = 300L
        private const val DEFAULT_CONTENT_TYPE = "application/octet-stream"
        private const val TAG_UPLOAD = "UploadS3"
    }

    private var isLoaded = false
    private val okHttpClient = OkHttpClient()
    // endregion

    // region State Flows
    private val _letterState = MutableStateFlow(LetterState())
    val letterState: StateFlow<LetterState> = _letterState.asStateFlow()

    private val _postLetterState = MutableStateFlow(PostLetterState())
    val postLetterState: StateFlow<PostLetterState> = _postLetterState.asStateFlow()

    private val _studentState = MutableStateFlow(StudentState())
    val studentState: StateFlow<StudentState> = _studentState.asStateFlow()

    private val _formState = MutableStateFlow(LetterFormState())
    val formState: StateFlow<LetterFormState> = _formState.asStateFlow()

    private val _uploadState = MutableStateFlow(UploadState())
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    val userNameState: StateFlow<String?> = authPreferences.userName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val userEmailState: StateFlow<String?> = authPreferences.userEmail
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    // endregion

    init {
        observeUserIdAndFetchLetters()
        getStudents()
    }

    // region Student Management
    private fun getStudents() {
        viewModelScope.launch {
            getStudentUseCase().collect { result ->
                _studentState.update {
                    when (result) {
                        is Resource.Loading -> it.copy(isLoading = true, error = null)
                        is Resource.Success -> it.copy(
                            isLoading = false,
                            data = result.data.orEmpty(),
                            error = null
                        )

                        is Resource.Error -> it.copy(
                            isLoading = false,
                            error = result.message
                        )
                        else -> {}
                    } as StudentState
                }
            }
        }
    }
    // endregion

    // region Letter Management
    private fun observeUserIdAndFetchLetters() {
        viewModelScope.launch {
            authPreferences.userId.collectLatest { userId ->
                if (!userId.isNullOrBlank()) {
                    getLettersByUserId(userId)
                } else {
                    _letterState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            data = emptyList(),
                            error = "User ID tidak ditemukan."
                        )
                    }
                }
            }
        }
    }

    private fun getLettersByUserId(userId: String, forceRefresh: Boolean = false) {
        if (isLoaded && !forceRefresh) return

        viewModelScope.launch {
            getLetterByUserIdUseCase(userId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _letterState.update {
                            if (forceRefresh) {
                                it.copy(isRefreshing = true, error = null)
                            } else {
                                it.copy(isLoading = true, error = null)
                            }
                        }
                    }

                    is Resource.Success -> {
                        isLoaded = true
                        _letterState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                data = result.data.orEmpty(),
                                error = ""
                            )
                        }
                    }

                    is Resource.Error -> {
                        _letterState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = "Terjadi kesalahan: ${result.message}"
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    fun refreshLetters() {
        viewModelScope.launch {
            val userId = authPreferences.getUserId()
            if (!userId.isNullOrBlank()) {
                getLettersByUserId(userId, forceRefresh = true)
            } else {
                _letterState.update { it.copy(error = "User ID tidak ditemukan.") }
            }
        }
    }
    // endregion

    // region Form Management
    fun updateFormField(field: FormField, value: Any) {
        _formState.update { currentState ->
            when (field) {
                FormField.DESCRIPTION -> currentState.copy(
                    description = value as String,
                    descriptionError = null
                )
                FormField.SELECTED_LETTER -> currentState.copy(
                    selectedLetter = value as String,
                    letterTypeError = null
                )
                FormField.SUBJECT -> currentState.copy(
                    subject = value as String,
                    subjectError = null
                )
                FormField.BEGIN_DATE -> currentState.copy(
                    beginDate = value as String,
                    beginDateError = null
                )
                FormField.END_DATE -> currentState.copy(
                    endDate = value as String,
                    endDateError = null
                )
                FormField.BEGIN_TIME -> currentState.copy(
                    beginTime = value as String,
                    beginTimeError = null
                )
                FormField.END_TIME -> currentState.copy(
                    endTime = value as String,
                    endTimeError = null
                )
                FormField.IS_PRINTED -> currentState.copy(isPrinted = value as Boolean)
                FormField.SELECTED_STUDENTS -> currentState.copy(
                    selectedStudentIds = value as List<String>,
                    studentsError = null
                )
                FormField.FILE_URI -> currentState.copy(
                    fileUri = value as Uri?,
                    fileError = null
                )
            }
        }
    }

    fun resetForm() {
        _formState.value = LetterFormState()
        _uploadState.value = UploadState()
    }

    private fun validateForm(state: LetterFormState): Boolean {
        var isValid = true
        val updatedState = state.copy(
            letterTypeError = null,
            subjectError = null,
            beginDateError = null,
            endDateError = null,
            beginTimeError = null,
            endTimeError = null,
            studentsError = null,
            fileError = null
        )

        when {
            state.selectedLetter.isBlank() -> {
                _formState.value = updatedState.copy(
                    letterTypeError = "Jenis surat harus dipilih"
                )
                isValid = false
            }

            state.subject.isBlank() -> {
                _formState.value = updatedState.copy(
                    subjectError = "Judul surat harus diisi"
                )
                isValid = false
            }

            state.selectedLetter in listOf("Surat Dispensasi", "Surat Tugas") -> {
                when {
                    state.beginDate.isBlank() -> {
                        _formState.value = updatedState.copy(
                            beginDateError = "Tanggal mulai harus diisi"
                        )
                        isValid = false
                    }

                    state.beginTime.isBlank() -> {
                        _formState.value = updatedState.copy(
                            beginTimeError = "Waktu mulai harus diisi"
                        )
                        isValid = false
                    }

                    state.endDate.isBlank() -> {
                        _formState.value = updatedState.copy(
                            endDateError = "Tanggal selesai harus diisi"
                        )
                        isValid = false
                    }

                    state.endTime.isBlank() -> {
                        _formState.value = updatedState.copy(
                            endTimeError = "Waktu selesai harus diisi"
                        )
                        isValid = false
                    }

                    !isValidDateTimeRange(state.beginDate, state.beginTime, state.endDate, state.endTime) -> {
                        val validationResult = getDateTimeValidationError(
                            state.beginDate, state.beginTime,
                            state.endDate, state.endTime
                        )
                        _formState.value = updatedState.copy(
                            beginDateError = validationResult.beginDateError,
                            endDateError = validationResult.endDateError,
                            beginTimeError = validationResult.beginTimeError,
                            endTimeError = validationResult.endTimeError
                        )
                        isValid = false
                    }
                }
            }
        }

        return isValid
    }
    // endregion

    // region File Upload
    fun uploadFile(
        context: Context,
        uri: Uri,
        fileName: String,
        fileSize: Long,
        mimeType: String
    ) {
        viewModelScope.launch {
            _uploadState.update {
                it.copy(isUploading = true, error = null, progress = 10)
            }

            try {
                val reqPresigned = ReqPresigned(
                    fileName = fileName,
                    fileSize = fileSize.toString(),
                    fileType = mimeType,
                    type = "LETTER-ATTACHMENT"
                )

                postPresignedUrlUseCase(reqPresigned).collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uploadState.update { it.copy(progress = 20) }
                        }

                        is Resource.Success -> {
                            handleUploadSuccess(context, uri, result.data?.url, mimeType)
                        }

                        is Resource.Error -> {
                            _uploadState.update {
                                it.copy(
                                    isUploading = false,
                                    error = result.message ?: "Gagal upload file"
                                )
                            }
                        }

                        else -> {}
                    }
                }
            } catch (e: Exception) {
                _uploadState.update {
                    it.copy(
                        isUploading = false,
                        error = "Terjadi kesalahan: ${e.message}"
                    )
                }
            }
        }
    }

    private suspend fun handleUploadSuccess(
        context: Context,
        uri: Uri,
        presignedUrl: String?,
        mimeType: String
    ) {
        if (presignedUrl == null) {
            _uploadState.update {
                it.copy(isUploading = false, error = "Gagal mendapatkan URL upload")
            }
            return
        }

        _uploadState.update { it.copy(progress = 30) }

        val uploadSuccess = uploadFileToS3(context, uri, presignedUrl, mimeType)

        if (uploadSuccess) {
            val fileUrl = presignedUrl.split("?").firstOrNull() ?: presignedUrl
            _uploadState.update {
                it.copy(
                    isUploading = false,
                    uploadedUrl = fileUrl,
                    progress = 100,
                    error = null
                )
            }
        } else {
            _uploadState.update {
                it.copy(isUploading = false, error = "Gagal upload file ke server")
            }
        }
    }

    private suspend fun uploadFileToS3(
        context: Context,
        uri: Uri,
        presignedUrl: String,
        mimeType: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val tempFile = createTempFileFromUri(context, uri)
            updateProgress(50)

            val contentType = mimeType.ifEmpty { DEFAULT_CONTENT_TYPE }
            val requestBody = tempFile.asRequestBody(contentType.toMediaTypeOrNull())

            val request = Request.Builder()
                .url(presignedUrl)
                .put(requestBody)
                .addHeader("Content-Type", contentType)
                .addHeader("x-amz-acl", "public-read")
                .build()

            updateProgress(70)

            val response = okHttpClient.newCall(request).execute()

            updateProgress(90)

            tempFile.delete()

            if (!response.isSuccessful) {
                logUploadError(response.code, response.message, response.body?.string())
            }

            response.isSuccessful
        } catch (e: Exception) {
            android.util.Log.e(TAG_UPLOAD, "Upload exception", e)
            false
        }
    }

    private fun createTempFileFromUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Cannot open input stream")

        val tempFile = File(context.cacheDir, "temp_upload_${System.currentTimeMillis()}")

        inputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    private suspend fun updateProgress(progress: Int) {
        withContext(Dispatchers.Main) {
            _uploadState.update { it.copy(progress = progress) }
        }
    }

    private fun logUploadError(code: Int, message: String, errorBody: String?) {
        android.util.Log.e(TAG_UPLOAD, "Upload failed: $code - $message")
        errorBody?.let {
            android.util.Log.e(TAG_UPLOAD, "Error body: $it")
        }
    }
    // endregion

    // region Letter Submission
    fun submitLetter() {
        val state = _formState.value

        if (!validateForm(state)) return

        val beginIso = buildIsoDate(state.beginDate, state.beginTime)
        val endIso = buildIsoDate(state.endDate, state.endTime)
        val mappedLetterType = mapLetterTypeToEnum(state.selectedLetter)
        val userIds = extractUserIds(state.selectedStudentIds, _studentState.value.data)

        val req = ReqLetter(
            letterType = mappedLetterType,
            subject = state.subject,
            beginDate = beginIso.takeIf { it.isNotBlank() },
            endDate = endIso.takeIf { it.isNotBlank() },
            reason = state.description.takeIf { it.isNotBlank() },
            isPrinted = state.isPrinted,
            cc = userIds,
            attachment = _uploadState.value.uploadedUrl
        )

        postLetter(req)
    }

    fun postLetter(reqLetter: ReqLetter) {
        val validationError = validateLetterInput(reqLetter)
        if (validationError != null) {
            _postLetterState.update {
                it.copy(isLoading = false, isSuccess = false, error = validationError)
            }
            return
        }

        viewModelScope.launch {
            postLetterUseCase(reqLetter).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _postLetterState.update {
                            it.copy(isLoading = true, isSuccess = false, error = null)
                        }
                    }

                    is Resource.Success -> {
                        _postLetterState.update {
                            it.copy(isLoading = false, isSuccess = true, error = null)
                        }

                        authPreferences.getUserId()?.let { userId ->
                            getLettersByUserId(userId, forceRefresh = true)
                        }

                        delay(DELAY_BEFORE_RESET)
                        resetPostState()
                    }

                    is Resource.Error -> {
                        _postLetterState.update {
                            it.copy(isLoading = false, isSuccess = false, error = result.message)
                        }

                        delay(DELAY_BEFORE_RESET)
                        resetPostState()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun resetPostState() {
        _postLetterState.value = PostLetterState()
    }

    private fun validateLetterInput(reqLetter: ReqLetter): String? {
        return when {
            reqLetter.letterType.isBlank() -> "Jenis surat harus dipilih"
            reqLetter.subject.isNullOrBlank() -> "Judul surat harus diisi"
            reqLetter.letterType in listOf("DISPENSATION", "ASSIGNMENT") -> when {
                reqLetter.beginDate.isNullOrBlank() -> "Tanggal mulai harus diisi"
                reqLetter.endDate.isNullOrBlank() -> "Tanggal selesai harus diisi"
                else -> null
            }
            else -> null
        }
    }
    // endregion

    // region Helper Functions
    private fun extractUserIds(selectedIds: List<String>, students: List<Student>): List<String> {
        return selectedIds.mapNotNull { selectedId ->
            students.find { it.id == selectedId }?.userId?.takeIf { it.isNotBlank() }
        }
    }

    private fun mapLetterTypeToEnum(displayName: String): String {
        return when (displayName) {
            "Surat Dispensasi" -> "DISPENSATION"
            "Surat Rekomendasi" -> "RECOMMENDATION"
            "Surat Keterangan Aktif" -> "ACTIVE_STATEMENT"
            "Surat Tugas" -> "ASSIGNMENT"
            else -> displayName
        }
    }

    private fun buildIsoDate(date: String, time: String): String {
        if (date.isEmpty() || time.isEmpty()) return ""

        return try {
            val parts = date.split("/")
            val day = parts[0].padStart(2, '0')
            val month = parts[1].padStart(2, '0')
            val year = parts[2]
            val cleanTime = time.trim().replace(" ", "")
            "${year}-${day}-${month}T${cleanTime}:00.000+08:00"
        } catch (_: Exception) {
            ""
        }
    }

    private fun isValidDateTimeRange(
        beginDate: String,
        beginTime: String,
        endDate: String,
        endTime: String
    ): Boolean {
        return try {
            // Parse tanggal format: DD/MM/YYYY
            val beginParts = beginDate.split("/")
            val endParts = endDate.split("/")

            val beginDay = beginParts[0].toIntOrNull() ?: return false
            val beginMonth = beginParts[1].toIntOrNull() ?: return false
            val beginYear = beginParts[2].toIntOrNull() ?: return false

            val endDay = endParts[0].toIntOrNull() ?: return false
            val endMonth = endParts[1].toIntOrNull() ?: return false
            val endYear = endParts[2].toIntOrNull() ?: return false

            // Parse waktu format: HH : MM atau HH:MM
            val beginTimeClean = beginTime.trim().replace(" ", "")
            val endTimeClean = endTime.trim().replace(" ", "")

            val beginTimeParts = beginTimeClean.split(":")
            val endTimeParts = endTimeClean.split(":")

            val beginHour = beginTimeParts[0].toIntOrNull() ?: return false
            val beginMinute = beginTimeParts.getOrNull(1)?.toIntOrNull() ?: 0

            val endHour = endTimeParts[0].toIntOrNull() ?: return false
            val endMinute = endTimeParts.getOrNull(1)?.toIntOrNull() ?: 0

            // Buat timestamp untuk perbandingan (YYYYMMDDHHNN)
            val beginTimestamp = String.format(
                "%04d%02d%02d%02d%02d",
                beginYear, beginMonth, beginDay, beginHour, beginMinute
            ).toLong()

            val endTimestamp = String.format(
                "%04d%02d%02d%02d%02d",
                endYear, endMonth, endDay, endHour, endMinute
            ).toLong()

            // Waktu selesai harus lebih besar atau sama dengan waktu mulai
            endTimestamp >= beginTimestamp
        } catch (e: Exception) {
            android.util.Log.e("DateValidation", "Error validating date range", e)
            true // Return true jika parsing gagal untuk tidak memblokir form
        }
    }

    private fun getDateTimeValidationError(
        beginDate: String,
        beginTime: String,
        endDate: String,
        endTime: String
    ): ValidationResult {
        var beginDateError: String? = null
        var endDateError: String? = null
        var beginTimeError: String? = null
        var endTimeError: String? = null

        try {
            val beginDateTime = parseDateTime(beginDate, beginTime)
            val endDateTime = parseDateTime(endDate, endTime)

            if (beginDateTime != null && endDateTime != null) {
                if (beginDate > endDate) {
                    beginDateError = "Tanggal dan waktu selesai harus setelah tanggal dan waktu mulai."
                } else if (beginDate == endDate && beginTime > endTime) {
                    beginTimeError = "Waktu mulai harus lebih awal dari waktu selesai."
                    endTimeError = "Waktu selesai harus lebih lambat dari waktu mulai."
                } else if (beginDate == endDate && beginTime == endTime){
                    beginTimeError = "Waktu mulai dan selesai tidak boleh sama."
                    endTimeError = "Waktu mulai dan selesai tidak boleh sama."
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("DateValidation", "Error validating date/time", e)
        }

        return ValidationResult(
            beginDateError = beginDateError,
            endDateError = endDateError,
            beginTimeError = beginTimeError,
            endTimeError = endTimeError
        )
    }

    private fun parseDateTime(date: String, time: String): Long? {
        return try {
            val parts = date.split("/")
            val day = parts[0].toIntOrNull() ?: return null
            val month = parts[1].toIntOrNull() ?: return null
            val year = parts[2].toIntOrNull() ?: return null

            val cleanTime = time.trim().replace(" ", "")
            val timeParts = cleanTime.split(":")
            val hour = timeParts[0].toIntOrNull() ?: return null
            val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

            // Buat timestamp untuk tanggal dan waktu
            String.format(
                "%04d%02d%02d%02d%02d",
                year, month, day, hour, minute
            ).toLong()
        } catch (e: Exception) {
            android.util.Log.e("DateParsing", "Error parsing date/time", e)
            null
        }
    }
    // endregion
}

enum class FormField {
    DESCRIPTION,
    SELECTED_LETTER,
    SUBJECT,
    BEGIN_DATE,
    END_DATE,
    BEGIN_TIME,
    END_TIME,
    IS_PRINTED,
    SELECTED_STUDENTS,
    FILE_URI
}

data class LetterFormState(
    val description: String = "",
    val selectedLetter: String = "",
    val subject: String = "",
    val beginDate: String = "",
    val endDate: String = "",
    val beginTime: String = "",
    val endTime: String = "",
    val isPrinted: Boolean = false,
    val selectedStudentIds: List<String> = emptyList(),
    val fileUri: Uri? = null,
    val letterTypeError: String? = null,
    val subjectError: String? = null,
    val beginDateError: String? = null,
    val endDateError: String? = null,
    val beginTimeError: String? = null,
    val endTimeError: String? = null,
    val studentsError: String? = null,
    val descriptionError: String? = null,
    val fileError: String? = null
)

data class UploadState(
    val isUploading: Boolean = false,
    val uploadedUrl: String? = null,
    val progress: Int = 0,
    val error: String? = null
)

data class ValidationResult(
    val beginDateError: String? = null,
    val endDateError: String? = null,
    val beginTimeError: String? = null,
    val endTimeError: String? = null
)
