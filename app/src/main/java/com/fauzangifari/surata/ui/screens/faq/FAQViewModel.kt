package com.fauzangifari.surata.ui.screens.faq

import androidx.lifecycle.ViewModel
import com.fauzangifari.domain.model.FAQ
import kotlinx.coroutines.flow.MutableStateFlow

class FAQViewModel : ViewModel(){
    private val _faqList = MutableStateFlow<List<FAQ>>(emptyList())
    val faqList = _faqList

    init {
        _faqList.value = listOf(
            FAQ(
                question = "Apa itu Surata?",
                answer = "Surata adalah aplikasi persuratan berbasis android untuk SMA Negeri 1 Samarinda."
            ),
            FAQ(
                question = "Bagaimana cara mengajukan surat?",
                answer = "Masuk ke menu Buat Surat, pilih jenis surat, isi form, lalu ajukan."
            ),
            FAQ(
                question = "Apakah status pengajuan bisa dilacak?",
                answer = "Ya, kamu bisa melihatnya di menu Riwayat Surat."
            ),
            FAQ(
                question = "Siapa yang bisa menggunakan aplikasi Surata?",
                answer = "Aplikasi ini khusus untuk siswa dan staf SMA Negeri 1 Samarinda."
            )
        )
    }
}