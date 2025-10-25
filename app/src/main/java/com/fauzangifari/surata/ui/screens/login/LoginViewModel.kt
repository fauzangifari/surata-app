package com.fauzangifari.surata.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.data.source.local.datastore.AuthPreferences
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.Auth
import com.fauzangifari.domain.usecase.PostSignInUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: PostSignInUseCase,
) : ViewModel() {

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

    private val _loginState = MutableStateFlow<Resource<Auth>>(Resource.Idle())
    val loginState: StateFlow<Resource<Auth>> = _loginState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn


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
        val email = _email.value
        val password = _password.value

        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        if (!isEmailValid || !isPasswordValid) return

        viewModelScope.launch {
            _loginState.value = Resource.Loading()

            when (val result = signInUseCase(email, password)) {
                is Resource.Success -> {
                    _isLoggedIn.value = true
                    _loginState.value = result
                }

                is Resource.Error -> {
                    _isLoggedIn.value = false
                    _loginState.value = result
                }

                else -> Unit
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
