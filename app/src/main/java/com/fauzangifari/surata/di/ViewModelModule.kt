package com.fauzangifari.surata.di

import com.fauzangifari.surata.ui.screens.detail.DetailViewModel
import com.fauzangifari.surata.ui.screens.faq.FAQViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import com.fauzangifari.surata.ui.screens.home.HomeViewModel
import com.fauzangifari.surata.ui.screens.login.LoginViewModel
import com.fauzangifari.surata.ui.screens.notification.NotificationViewModel
import com.fauzangifari.surata.ui.screens.profile.ProfileViewModel
import com.fauzangifari.surata.ui.screens.settings.SettingViewModel
import com.fauzangifari.surata.ui.screens.splash.SplashViewModel
import com.fauzangifari.surata.viewmodel.FCMViewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel {
        DetailViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        LoginViewModel(get(), get(), get(), get())
    }

    viewModel {
        FAQViewModel()
    }

    viewModel {
        SplashViewModel(get(), get())
    }

    viewModel {
        SettingViewModel(get(), get(), get())
    }

    viewModel {
        ProfileViewModel(get())
    }

    viewModel {
        FCMViewModel(get(), get())
    }

    viewModel {
        NotificationViewModel(get(), get(), get(), get(), get())
    }
}
