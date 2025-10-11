package com.fauzangifari.surata.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.usecase.PostSignOutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SettingViewModel(
    private val authPreferences: AuthPreferences,
    private val postSignOutUseCase: PostSignOutUseCase
) : ViewModel() {

    private val _logoutState = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val logoutState: StateFlow<Resource<Boolean>> = _logoutState

    private val _logoutMessage = MutableSharedFlow<String>()
    val logoutMessage = _logoutMessage.asSharedFlow()

    private val _logoutEvent = MutableSharedFlow<LogoutEvent>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    fun logout() {
        if (_logoutState.value is Resource.Loading) return

        viewModelScope.launch {
            _logoutState.value = Resource.Loading()

            when (val result = postSignOutUseCase(true)) {
                is Resource.Success -> {
                    runCatching { authPreferences.clear() }
                        .onFailure { error ->
                            Log.e("SettingViewModel", "Failed to clear session: ${error.localizedMessage}")
                            val message = error.localizedMessage ?: "Gagal menghapus sesi"
                            _logoutState.value = Resource.Error(message)
                            _logoutMessage.emit(message)
                            return@launch
                        }

                    _logoutState.value = Resource.Success(true)
                    _logoutMessage.emit("Berhasil keluar")
                    _logoutEvent.emit(LogoutEvent.NavigateToLogin)
                }

                is Resource.Error -> {
                    Log.e("SettingViewModel", "Logout error: ${result.message}")
                    val message = result.message ?: "Terjadi kesalahan saat keluar"
                    _logoutState.value = Resource.Error(message)
                    _logoutMessage.emit(message)
                }

                is Resource.Loading -> {
                    _logoutState.value = Resource.Loading()
                }

                is Resource.Idle -> {
                    _logoutState.value = Resource.Idle()
                }
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = Resource.Idle()
    }

    sealed class LogoutEvent {
        data object NavigateToLogin : LogoutEvent()
    }
}
