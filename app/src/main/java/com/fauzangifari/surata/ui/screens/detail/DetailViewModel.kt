package com.fauzangifari.surata.ui.screens.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.model.ReqPresigned
import com.fauzangifari.domain.model.User
import com.fauzangifari.domain.model.UserProfile
import com.fauzangifari.domain.usecase.GetAllUserUseCase
import com.fauzangifari.domain.usecase.GetDetailLetterUseCase
import com.fauzangifari.domain.usecase.PostLetterUseCase
import com.fauzangifari.domain.usecase.PostPresignedUrlUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class DetailViewModel(
    private val getDetailLetterUseCase: GetDetailLetterUseCase,
    private val postLetterUseCase: PostLetterUseCase,
    private val postPresignedUrlUseCase: PostPresignedUrlUseCase,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private companion object {
        private const val DEFAULT_CONTENT_TYPE = "application/octet-stream"
        private const val TAG_UPLOAD = "UploadS3"
    }

    private val okHttpClient = OkHttpClient()

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile

    private val _formState = MutableStateFlow(LetterFormState())
    val formState: StateFlow<LetterFormState> = _formState

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _uploadState = MutableStateFlow(UploadState())
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    val userNameState: StateFlow<String?> = authPreferences.userName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        getAllUsers()
    }

    fun getDetail(id: String) {
        viewModelScope.launch {
            getDetailLetterUseCase(id).collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is Resource.Success -> {
                        _state.value = _state.value.copy(data = result.data, isLoading = false)
                        result.data?.let { letter ->
                            initializeFormWithLetter(letter)
                        }
                    }
                    is Resource.Error -> _state.value = _state.value.copy(error = result.message.toString(), isLoading = false)
                    else -> {}
                }
            }
        }
    }

    private fun postLetter(reqLetter: ReqLetter) {
        val validationError = validateLetterInput(reqLetter)
        if (validationError != null) {
            _state.update {
                it.copy(
                    isSubmittingRevision = false,
                    revisionError = validationError
                )
            }
            return
        }

        viewModelScope.launch {
            postLetterUseCase(reqLetter).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isSubmittingRevision = true, revisionError = null) }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isSubmittingRevision = false,
                                revisionSuccess = true,
                                showRevisionDialog = false,
                                revisionError = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isSubmittingRevision = false,
                                revisionError = result.message ?: "Gagal mengirim revisi"
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
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

    private fun initializeFormWithLetter(letter: com.fauzangifari.domain.model.Letter) {
        _formState.value = LetterFormState(
            selectedLetter = mapLetterTypeToDisplay(letter.letterType),
            subject = letter.subject,
            description = letter.reason ?: "",
            beginDate = extractDate(letter.beginDate),
            beginTime = extractTime(letter.beginDate),
            endDate = extractDate(letter.endDate),
            endTime = extractTime(letter.endDate),
            isPrinted = letter.isPrinted,
            selectedStudentIds = letter.cc
        )
    }

    private fun mapLetterTypeToDisplay(type: String): String {
        return when (type) {
            "DISPENSATION" -> "Surat Dispensasi"
            "RECOMMENDATION" -> "Surat Rekomendasi"
            "ACTIVE_STATEMENT" -> "Surat Keterangan Aktif"
            "ASSIGNMENT" -> "Surat Tugas"
            else -> type
        }
    }

    private fun extractDate(isoDateTime: String?): String {
        if (isoDateTime.isNullOrBlank()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(isoDateTime)
            date?.let { outputFormat.format(it) } ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    private fun extractTime(isoDateTime: String?): String {
        if (isoDateTime.isNullOrBlank()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = inputFormat.parse(isoDateTime)
            date?.let { outputFormat.format(it) } ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    fun showRevisionDialog() {
        _state.value = _state.value.copy(showRevisionDialog = true)
    }

    fun dismissRevisionDialog() {
        _state.value = _state.value.copy(
            showRevisionDialog = false,
            revisionError = null
        )
    }

    fun clearRevisionSuccess() {
        _state.value = _state.value.copy(revisionSuccess = false)
    }

    // Form Management
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

    private fun getAllUsers() {
        viewModelScope.launch {
            _userState.update { it.copy(isLoading = true) }
            when (val result = getAllUserUseCase()) {
                is Resource.Loading -> {
                    _userState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    _userState.update {
                        it.copy(
                            data = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _userState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }

                else -> {}
            }
        }
    }

    // File Upload
    fun uploadFile(context: Context, uri: Uri, fileName: String, fileSize: Long, mimeType: String?) {
        viewModelScope.launch {
            _uploadState.update { it.copy(isUploading = true, error = null, progress = 0) }

            try {
                val presignedResult = getPresignedUrl(fileName, fileSize, mimeType)

                if (presignedResult == null) {
                    _uploadState.update {
                        it.copy(isUploading = false, error = "Gagal mendapatkan URL upload")
                    }
                    return@launch
                }

                val uploadSuccess = uploadToS3(context, uri, presignedResult.uploadUrl, mimeType)

                if (uploadSuccess) {
                    _uploadState.update {
                        it.copy(
                            isUploading = false,
                            uploadedUrl = presignedResult.fileUrl,
                            progress = 100,
                            error = null
                        )
                    }
                } else {
                    _uploadState.update {
                        it.copy(isUploading = false, error = "Gagal mengunggah file ke server")
                    }
                }
            } catch (e: Exception) {
                _uploadState.update {
                    it.copy(isUploading = false, error = e.message ?: "Terjadi kesalahan saat upload")
                }
            }
        }
    }

    private suspend fun getPresignedUrl(fileName: String, fileSize: Long, mimeType: String?): PresignedResult? {
        return withContext(Dispatchers.IO) {
            try {
                updateProgress(10)
                val reqPresigned = ReqPresigned(
                    fileName = fileName,
                    fileSize = fileSize.toString(),
                    type = mimeType ?: DEFAULT_CONTENT_TYPE,
                    fileType = mimeType ?: DEFAULT_CONTENT_TYPE
                )

                var result: PresignedResult? = null

                postPresignedUrlUseCase(reqPresigned).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            updateProgress(30)
                            resource.data?.let { presignedUrl ->
                                result = PresignedResult(
                                    uploadUrl = presignedUrl.url ?: "",
                                    fileUrl = presignedUrl.url ?: ""
                                )
                            }
                        }
                        is Resource.Error -> {
                            android.util.Log.e(TAG_UPLOAD, "Presigned URL error: ${resource.message}")
                        }
                        else -> {}
                    }
                }
                result
            } catch (_: Exception) {
                android.util.Log.e(TAG_UPLOAD, "Exception getting presigned URL")
                null
            }
        }
    }

    private suspend fun uploadToS3(context: Context, uri: Uri, uploadUrl: String, mimeType: String?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                updateProgress(40)

                val tempFile = createTempFileFromUri(context, uri)

                updateProgress(50)

                val requestBody = tempFile.asRequestBody(
                    (mimeType ?: DEFAULT_CONTENT_TYPE).toMediaTypeOrNull()
                )

                val request = Request.Builder()
                    .url(uploadUrl)
                    .put(requestBody)
                    .build()

                updateProgress(70)

                val response = okHttpClient.newCall(request).execute()

                updateProgress(90)

                tempFile.delete()

                if (!response.isSuccessful) {
                    android.util.Log.e(TAG_UPLOAD, "Upload failed: ${response.code} - ${response.message}")
                }

                response.isSuccessful
            } catch (_: Exception) {
                android.util.Log.e(TAG_UPLOAD, "Upload exception")
                false
            }
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

    fun submitRevision() {
        val state = _formState.value

        if (!validateForm(state)) return

        _state.update { it.copy(isSubmittingRevision = true, revisionError = null) }

        val beginIso = buildIsoDate(state.beginDate, state.beginTime)
        val endIso = buildIsoDate(state.endDate, state.endTime)
        val mappedLetterType = mapLetterTypeToEnum(state.selectedLetter)
        val userIds = extractUserIds(state.selectedStudentIds, _userState.value.data)

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
                }
            }
        }

        return isValid
    }

    private fun extractUserIds(selectedIds: List<String>, user: List<User>): List<String> {
        return selectedIds.mapNotNull { selectedId ->
            user.find { it.id == selectedId }?.id?.takeIf { it.isNotBlank() }
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
        if (date.isBlank() || time.isBlank()) return ""
        return try {
            val dateTimeParts = date.split("/")
            val timeParts = time.split(":")
            if (dateTimeParts.size == 3 && timeParts.size == 2) {
                val day = dateTimeParts[0].padStart(2, '0')
                val month = dateTimeParts[1].padStart(2, '0')
                val year = dateTimeParts[2]
                val hour = timeParts[0].padStart(2, '0')
                val minute = timeParts[1].padStart(2, '0')
                "${year}-${month}-${day}T${hour}:${minute}:00.000Z"
            } else ""
        } catch (_: Exception) {
            ""
        }
    }

    fun resetForm() {
        _formState.value = LetterFormState()
        _uploadState.value = UploadState()
    }
}

data class PresignedResult(
    val uploadUrl: String,
    val fileUrl: String
)

data class UserState(
    val isLoading: Boolean = false,
    val data: List<User> = emptyList(),
    val error: String? = null
)

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
