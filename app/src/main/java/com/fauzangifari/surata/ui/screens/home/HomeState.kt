package com.fauzangifari.surata.ui.screens.home

import android.net.Uri
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.Student
import com.fauzangifari.domain.model.User

data class LetterState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val data: List<Letter> = emptyList(),
    val error: String? = null
)

data class StudentState(
    val isLoading: Boolean = false,
    val data: List<Student> = emptyList(),
    val error: String? = null
)

data class UserState(
    val isLoading: Boolean = false,
    val data: List<User> = emptyList(),
    val error: String? = null
)

data class PostLetterState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

data class LetterFormState(
    val description: String = "",
    val selectedLetter: String = "",
    val subject: String = "",
    val beginDate: String = "",
    val endDate: String = "",
    val beginTime: String = "",
    val endTime: String = "",
    val isPrinted: Boolean = false,
    val selectedStudentIds: List<String> = emptyList(),
    val fileUri: Uri? = null,
    val letterTypeError: String? = null,
    val subjectError: String? = null,
    val beginDateError: String? = null,
    val endDateError: String? = null,
    val beginTimeError: String? = null,
    val endTimeError: String? = null,
    val studentsError: String? = null,
    val descriptionError: String? = null,
    val fileError: String? = null
)

data class UploadState(
    val isUploading: Boolean = false,
    val uploadedUrl: String? = null,
    val progress: Int = 0,
    val error: String? = null
)

data class ValidationResult(
    val beginDateError: String? = null,
    val endDateError: String? = null,
    val beginTimeError: String? = null,
    val endTimeError: String? = null
)
