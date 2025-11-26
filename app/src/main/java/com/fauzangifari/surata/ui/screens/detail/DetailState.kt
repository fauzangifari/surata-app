package com.fauzangifari.surata.ui.screens.detail

import android.net.Uri
import com.fauzangifari.domain.model.Letter

data class DetailState(
    val isLoading: Boolean = false,
    val data: Letter? = null,
    val error: String = "",
    val showRevisionDialog: Boolean = false,
    val isSubmittingRevision: Boolean = false,
    val revisionSuccess: Boolean = false,
    val revisionError: String? = null
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

data class PostLetterState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)