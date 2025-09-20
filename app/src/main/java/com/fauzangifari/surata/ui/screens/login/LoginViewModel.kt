package com.fauzangifari.surata.ui.screens.login

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private suspend fun showToast(message: String) {
        _toastMessage.emit(message)
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        validateEmail(newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        validatePassword(newPassword)
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun login() {
        val isEmailValid = validateEmail(_email.value)
        val isPasswordValid = validatePassword(_password.value)

        viewModelScope.launch {
            if (isEmailValid && isPasswordValid) {
                if (_email.value == "admin@gmail.com" && _password.value == "123") {
                    _isLoggedIn.value = true
                    showToast(message = "Berhasil Login")
                } else {
                    _isLoggedIn.value = false
                    showToast(message = "Email atau password salah")
                }
            }
        }
    }


    private fun validateEmail(email: String): Boolean {
        return if (email.isBlank()) {
            _emailError.value = "Email tidak boleh kosong"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Format email tidak valid"
            false
        } else {
            _emailError.value = null
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isBlank()) {
            _passwordError.value = "Password tidak boleh kosong"
            false
        } else {
            _passwordError.value = null
            true
        }
    }

}
