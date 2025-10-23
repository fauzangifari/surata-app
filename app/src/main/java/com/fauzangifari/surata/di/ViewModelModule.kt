package com.fauzangifari.surata.di

import com.fauzangifari.surata.ui.screens.detail.DetailViewModel
import com.fauzangifari.surata.ui.screens.faq.FAQViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import com.fauzangifari.surata.ui.screens.home.HomeViewModel
import com.fauzangifari.surata.ui.screens.login.LoginViewModel
import com.fauzangifari.surata.ui.screens.settings.SettingViewModel
import com.fauzangifari.surata.ui.screens.splash.SplashViewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel(get(), get(), get())
    }

    viewModel {
        DetailViewModel(get())
    }

    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        FAQViewModel()
    }

    viewModel {
        SplashViewModel(get(), get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }
}
