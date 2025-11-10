package com.fauzangifari.surata.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.usecase.GetDetailLetterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getDetailLetterUseCase: GetDetailLetterUseCase,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    val userNameState: StateFlow<String?> = authPreferences.userName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun getDetail(id: String) {
        viewModelScope.launch {
            getDetailLetterUseCase(id).collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is Resource.Success -> _state.value = _state.value.copy(data = result.data, isLoading = false)
                    is Resource.Error -> _state.value = _state.value.copy(error = result.message.toString(), isLoading = false)
                    else -> {}
                }
            }
        }
    }
}
