package com.fauzangifari.surata.di

import com.fauzangifari.surata.ui.screens.detail.DetailViewModel
import com.fauzangifari.surata.ui.screens.faq.FAQViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import com.fauzangifari.surata.ui.screens.home.HomeViewModel
import com.fauzangifari.surata.ui.screens.login.LoginViewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        DetailViewModel(get())
    }

    viewModel {
        LoginViewModel()
    }

    viewModel {
        FAQViewModel()
    }
}