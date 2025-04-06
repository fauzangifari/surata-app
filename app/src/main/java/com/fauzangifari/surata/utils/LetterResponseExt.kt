package com.fauzangifari.surata.utils

import com.fauzangifari.surata.data.source.remote.response.LetterResponse

fun LetterResponse.getErrorMessage(): String {
    return message ?: errors?.joinToString("\n") {
        "Kesalahan ${it?.code} : ${it?.message}"
    } ?: "Terjadi kesalahan"
}
