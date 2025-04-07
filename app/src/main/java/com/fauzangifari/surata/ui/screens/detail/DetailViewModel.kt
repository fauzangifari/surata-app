package com.fauzangifari.surata.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.surata.common.Resource
import com.fauzangifari.surata.domain.usecase.GetDetailLetterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailLetterUseCase: GetDetailLetterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    fun getDetail(id: String) {
        viewModelScope.launch {
            getDetailLetterUseCase(id).collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is Resource.Success -> _state.value = _state.value.copy(data = result.data, isLoading = false)
                    is Resource.Error -> _state.value = _state.value.copy(error = result.message.toString(), isLoading = false)
                }
            }
        }
    }
}

