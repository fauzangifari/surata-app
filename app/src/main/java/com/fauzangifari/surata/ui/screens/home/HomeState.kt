package com.fauzangifari.surata.ui.screens.home

import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.Student

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

data class PostLetterState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
