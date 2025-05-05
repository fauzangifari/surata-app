package com.fauzangifari.surata.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.usecase.GetLetterUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val getLetterUseCase: GetLetterUseCase
) : ViewModel() {

    private var isLoaded = false

    private val _state = MutableStateFlow(LetterState())
    val state: StateFlow<LetterState> = _state

    init {
        getLetters()
    }

    fun getLetters() {
        if (isLoaded) return

        viewModelScope.launch {
            getLetterUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        isLoaded = true
                        _state.update {
                            it.copy(
                                isLoading = false,
                                data = result.data ?: emptyList(),
                                error = ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Terjadi kesalahan"
                            )
                        }
                    }
                }
            }
        }
    }
}

