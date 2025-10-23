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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
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

    init {
        observeUserIdAndFetchLetters()
    }

    private fun observeUserIdAndFetchLetters() {
        viewModelScope.launch {
            authPreferences.userId.collectLatest { id ->

                if (!id.isNullOrBlank()) {
                    Log.d("HomeViewModel", "Fetching letters for userId: $id")
                    Log.d("HomeViewModel", "isLoaded: $isLoaded")
                    getLettersByUserId(id)
                } else {
                    Log.w("HomeViewModel", "UserId is null or blank, skip fetching letters.")
                }
            }
        }
    }

    private fun getLettersByUserId(userId: String, forceRefresh: Boolean = false) {
        if (isLoaded && !forceRefresh) return

        viewModelScope.launch {
            getLetterByUserIdUseCase(userId).collect { result ->
                when (result) {
                    is Resource.Loading -> _letterState.update { it.copy(isLoading = true) }

                    is Resource.Success -> {
                        isLoaded = true

                        Log.d("HomeViewModel", "getLettersByUserId success: ${result.data}")
                        _letterState.update {
                            it.copy(
                                isLoading = false,
                                data = result.data ?: emptyList(),
                                error = ""
                            )
                        }
                    }

                    is Resource.Error -> _letterState.update {
                        Log.e("HomeViewModel", "getLettersByUserId error: ${result.message}")

                        it.copy(
                            isLoading = false,
                            error = "Terjadi kesalahan: ${result.message}"
                        )
                    }

                    else -> {}
                }
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
