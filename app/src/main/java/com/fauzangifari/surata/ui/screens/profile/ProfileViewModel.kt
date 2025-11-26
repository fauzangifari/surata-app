package com.fauzangifari.surata.ui.screens.profile

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.UserProfile
import com.fauzangifari.domain.model.UserRole
import com.fauzangifari.domain.usecase.GetUsersMeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUsersMeUseCase: GetUsersMeUseCase
) : ViewModel() {

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _editedPhone = MutableStateFlow("")
    val editedPhone: StateFlow<String> = _editedPhone

    private val _editedPersonalEmail = MutableStateFlow("")
    val editedPersonalEmail: StateFlow<String> = _editedPersonalEmail

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _showSaveDialog = MutableStateFlow(false)
    val showSaveDialog: StateFlow<Boolean> = _showSaveDialog

    private val _showChangePhotoDialog = MutableStateFlow(false)
    val showChangePhotoDialog: StateFlow<Boolean> = _showChangePhotoDialog

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast

    private val _isSuccess = MutableStateFlow(true)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = getUsersMeUseCase()) {
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Success -> {
                    result.data?.let { userMe ->
                        val userProfile = when (userMe.role) {
                            UserRole.STUDENT -> {
                                // Map data student
                                val student = userMe.student
                                UserProfile(
                                    name = userMe.name,
                                    schoolEmail = userMe.email,
                                    personalEmail = userMe.secondaryEmail ?: "",
                                    placeOfBirth = student?.birthPlace ?: "",
                                    dateOfBirth = formatDate(student?.birthDate),
                                    phone = student?.phoneNumber ?: "",
                                    idNumber = student?.nisn ?: student?.nipd ?: "",
                                    photoUrl = userMe.image
                                )
                            }
                            UserRole.TEACHER -> {
                                // Map data teacher
                                val teacher = userMe.teacher
                                UserProfile(
                                    name = userMe.name,
                                    schoolEmail = userMe.email,
                                    personalEmail = userMe.secondaryEmail ?: "",
                                    placeOfBirth = teacher?.birthPlace ?: "",
                                    dateOfBirth = formatDate(teacher?.birthDate),
                                    phone = teacher?.phone ?: "",
                                    idNumber = teacher?.nip ?: "",
                                    photoUrl = userMe.image
                                )
                            }
                            else -> {
                                UserProfile(
                                    name = userMe.name,
                                    schoolEmail = userMe.email,
                                    personalEmail = userMe.secondaryEmail ?: "",
                                    placeOfBirth = "",
                                    dateOfBirth = "",
                                    phone = "",
                                    idNumber = "",
                                    photoUrl = userMe.image
                                )
                            }
                        }
                        _profile.value = userProfile
                        _editedPhone.value = userProfile.phone
                        _editedPersonalEmail.value = userProfile.personalEmail
                    }
                    _isLoading.value = false
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message ?: "Gagal memuat data profil"
                    _isLoading.value = false
                }
                else -> {
                    _isLoading.value = false
                }
            }
        }
    }

    fun loadProfile(profile: UserProfile) {
        _profile.value = profile
        _editedPhone.value = profile.phone
        _editedPersonalEmail.value = profile.personalEmail
    }

    fun onEditModeToggle(enable: Boolean) {
        _isEditMode.value = enable
        if (enable) {
            _editedPhone.value = _profile.value.phone
            _editedPersonalEmail.value = _profile.value.personalEmail
            _phoneError.value = null
            _emailError.value = null
        } else {
            _editedPhone.value = _profile.value.phone
            _editedPersonalEmail.value = _profile.value.personalEmail
            _phoneError.value = null
            _emailError.value = null
        }
    }

    fun onPhoneChange(phone: String) {
        _editedPhone.value = phone
        if (_phoneError.value != null) {
            validatePhone(phone)
        }
    }

    fun onPersonalEmailChange(email: String) {
        _editedPersonalEmail.value = email
        if (_emailError.value != null) {
            validateEmail(email)
        }
    }

    fun onSaveClick() {
        val isPhoneValid = validatePhone(_editedPhone.value)
        val isEmailValid = validateEmail(_editedPersonalEmail.value)

        if (isPhoneValid && isEmailValid) {
            _showSaveDialog.value = true
        }
    }

    fun onDismissSaveDialog() {
        _showSaveDialog.value = false
    }

    fun onShowChangePhotoDialog() {
        _showChangePhotoDialog.value = true
    }

    fun onDismissChangePhotoDialog() {
        _showChangePhotoDialog.value = false
    }

    fun saveProfile(onUpdateProfile: (UserProfile) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            val updatedProfile = _profile.value.copy(
                phone = _editedPhone.value,
                personalEmail = _editedPersonalEmail.value
            )

            try {
                onUpdateProfile(updatedProfile)

                _profile.value = updatedProfile
                _isEditMode.value = false
                _showSaveDialog.value = false
                _phoneError.value = null
                _emailError.value = null
                _isLoading.value = false
                _toastMessage.value = "Profil berhasil diperbarui"
                _showToast.value = true
                _isSuccess.value = true

                delay(3000)
                _showToast.value = false

            } catch (e: Exception) {
                _isLoading.value = false
                _toastMessage.value = "Gagal memperbarui profil: ${e.message}"
                _showToast.value = true
                _isSuccess.value = false

                delay(3000)
                _showToast.value = false
            }
        }
    }

    fun retryLoadProfile() {
        loadUserProfile()
    }

    private fun validatePhone(phone: String): Boolean {
        return when {
            phone.isBlank() -> {
                _phoneError.value = null
                true
            }
            phone.length < 10 -> {
                _phoneError.value = "Nomor telepon minimal 10 digit"
                false
            }
            phone.length > 15 -> {
                _phoneError.value = "Nomor telepon maksimal 15 digit"
                false
            }
            !phone.all { it.isDigit() || it == '+' || it == '-' || it == ' ' } -> {
                _phoneError.value = "Format nomor tidak valid"
                false
            }
            else -> {
                _phoneError.value = null
                true
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isBlank() -> {
                _emailError.value = null
                true
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _emailError.value = "Format email tidak valid"
                false
            }
            else -> {
                _emailError.value = null
                true
            }
        }
    }

    fun onDismissToast() {
        _showToast.value = false
    }

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrBlank()) return ""

        return try {
            val inputFormats = listOf(
                java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale("id", "ID")),
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale("id", "ID")),
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale("id", "ID"))
            )

            val outputFormat = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale("id", "ID"))

            var parsedDate: java.util.Date? = null
            for (format in inputFormats) {
                try {
                    parsedDate = format.parse(dateString)
                    break
                } catch (e: Exception) {
                    continue
                }
            }

            parsedDate?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }
}