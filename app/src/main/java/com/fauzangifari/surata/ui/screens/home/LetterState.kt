package com.fauzangifari.surata.ui.screens.home

import com.fauzangifari.domain.model.Letter

data class LetterState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val data: List<Letter> = emptyList(),
    val error: String? = null
) {
    val letters: List<Letter> get() = data
}
