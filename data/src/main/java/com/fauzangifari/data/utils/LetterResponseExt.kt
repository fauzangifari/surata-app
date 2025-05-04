package com.fauzangifari.data.utils

import com.fauzangifari.data.source.remote.response.LetterResponse

fun LetterResponse.getErrorMessage(): String {
    return message ?: errors?.joinToString("\n") {
        "Kesalahan ${it?.code} : ${it?.message}"
    } ?: "Terjadi kesalahan"
}
