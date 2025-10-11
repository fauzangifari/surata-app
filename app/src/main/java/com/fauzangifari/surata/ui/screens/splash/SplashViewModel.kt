package com.fauzangifari.surata.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Session
import com.fauzangifari.domain.usecase.GetSessionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authPreferences: AuthPreferences,
    private val getSessionUseCase: GetSessionUseCase
) : ViewModel() {

    private val _sessionState = MutableStateFlow<Resource<Session>>(Resource.Loading())
    val sessionState: StateFlow<Resource<Session>> = _sessionState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun checkSession() {
        viewModelScope.launch {
            val token = authPreferences.token.firstOrNull()

            if (token.isNullOrBlank()) {
                _isLoggedIn.value = false
                _sessionState.value = Resource.Error("Token not found")
                return@launch
            }

            Log.d("SplashViewModel", "Token found: $token")
            _sessionState.value = Resource.Loading()

            when (val result = getSessionUseCase()) {
                is Resource.Success -> {
                    _sessionState.value = result
                    _isLoggedIn.value = true
                }

                is Resource.Error -> {
                    _sessionState.value = Resource.Error(
                        result.message ?: "Failed to fetch session",
                        result.data
                    )
                    _isLoggedIn.value = false
                }

                is Resource.Loading -> {
                    _sessionState.value = result
                }

                else -> {
                    _sessionState.value = result
                    _isLoggedIn.value = false
                }
            }
        }
    }
}