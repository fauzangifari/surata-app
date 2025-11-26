package com.fauzangifari.surata.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.usecase.SaveFCMTokenUseCase
import com.fauzangifari.surata.utils.FCMTokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FCMViewModel(
    private val saveFCMTokenUseCase: SaveFCMTokenUseCase,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private val _tokenState = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val tokenState: StateFlow<Resource<Unit>> = _tokenState.asStateFlow()

    fun initializeFCMToken() {
        viewModelScope.launch {
            try {
                val token = FCMTokenManager.getToken()
                if (token != null) {
                    saveFCMToken(token)
                } else {
                    _tokenState.value = Resource.Error("Failed to get FCM token")
                }
            } catch (e: Exception) {
                _tokenState.value = Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    /**
     * Save FCM token to backend
     */
    private fun saveFCMToken(token: String) {
        viewModelScope.launch {
            _tokenState.value = Resource.Loading()
            when (val result = saveFCMTokenUseCase(token)) {
                is Resource.Success -> {
                    _tokenState.value = Resource.Success(Unit)
                }
                is Resource.Error -> {
                    _tokenState.value = Resource.Error(result.message ?: "Failed to save token")
                }
                is Resource.Loading -> {
                    _tokenState.value = Resource.Loading()
                }
                is Resource.Idle -> {
                    _tokenState.value = Resource.Idle()
                }
            }
        }
    }

    companion object {
        private const val TAG = "FCMViewModel"
    }
}

