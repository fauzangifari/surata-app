package com.fauzangifari.surata.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.usecase.GetLetterByUserIdUseCase
import com.fauzangifari.domain.usecase.PostLetterUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getLetterByUserIdUseCase: GetLetterByUserIdUseCase,
    private val postLetterUseCase: PostLetterUseCase,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private var isLoaded = false

    private val _letterState = MutableStateFlow(LetterState())
    val letterState: StateFlow<LetterState> = _letterState

    private val _postLetterState = MutableStateFlow(PostLetterState())
    val postLetterState: StateFlow<PostLetterState> = _postLetterState

    val userNameState: StateFlow<String?> = authPreferences.userName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    val userEmailState: StateFlow<String?> = authPreferences.userEmail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    init {
        observeUserIdAndFetchLetters()
    }

    private fun observeUserIdAndFetchLetters() {
        viewModelScope.launch {
            authPreferences.userId.collectLatest { id ->

                if (!id.isNullOrBlank()) {
                    getLettersByUserId(id)
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
                    is Resource.Loading -> _letterState.update {
                        if (forceRefresh) it.copy(isRefreshing = true, error = null) else it.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        isLoaded = true

                        Log.d("HomeViewModel", "getLettersByUserId success: ${result.data}")
                        _letterState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                data = result.data ?: emptyList(),
                                error = ""
                            )
                        }
                    }

                    is Resource.Error -> _letterState.update {
                        Log.e("HomeViewModel", "getLettersByUserId error: ${result.message}")

                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = "Terjadi kesalahan: ${result.message}"
                        )
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

                        delay(300)
                        resetPostState()
                    }

                    is Resource.Error -> {
                        _postLetterState.update {
                            it.copy(isLoading = false, isSuccess = false, error = result.message)
                        }

                        Log.e("HomeViewModel", "postLetter error: ${result.message}")

                        kotlinx.coroutines.delay(300)
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

    /**
     * Validasi input surat sebelum dikirim
     */
    private fun validateLetterInput(reqLetter: ReqLetter): String? {
        return when {
            reqLetter.letterType.isBlank() -> "Jenis surat harus dipilih."
            reqLetter.subject.isNullOrBlank() -> "Perihal surat harus diisi."

            reqLetter.letterType in listOf("DISPENSATION", "ASSIGNMENT") -> when {
                reqLetter.beginDate.isNullOrBlank() -> "Tanggal mulai harus diisi."
                reqLetter.endDate.isNullOrBlank() -> "Tanggal selesai harus diisi."
                else -> null
            }

            else -> null
        }
    }


    /**
     * Validasi urutan tanggal (beginDate <= endDate)
     */
//    private fun isValidDateOrder(beginDate: String, endDate: String): Boolean {
//        return try {
//            val begin = Instant.parse(beginDate)
//            val end = Instant.parse(endDate)
//            !end.isBefore(begin)
//        } catch (e: DateTimeParseException) {
//            false
//        }
//    }
}
