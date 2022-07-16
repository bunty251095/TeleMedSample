package com.vivant.telemedicine.di

import android.content.Context
import android.content.SharedPreferences
import com.vivant.telemedicine.common.PreferenceUtils
import com.vivant.telemedicine.network.AppDispatchers
import com.vivant.telemedicine.network.domain.AppointmentDetailsUseCase
import com.vivant.telemedicine.network.domain.ConsultAndScheduleUseCase
import com.vivant.telemedicine.network.domain.HomeUseCase
import com.vivant.telemedicine.network.repository.*
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import com.vivant.telemedicine.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val telemedicineModule = module {

    single { PreferenceUtils(get()) }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("TeleMedPreferences",Context.MODE_PRIVATE)
    }
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }

    factory<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    factory<ConsultAndScheduleRepository> { ConsultAndScheduleRepositoryImpl(get(), get()) }
    factory<AppointmentDetailsRepository> { AppointmentDetailsRepositoryImpl(get(), get()) }
    //factory { AppointmentDetailsRepositoryImpl(get(), get()) } bind AppointmentDetailsRepository::class
    //factory { AppointmentDetailsRepositoryImpl(get(), get()) as AppointmentDetailsRepository }
    //factory { AppointmentDetailsRepositoryImpl(get(), get()) }

    factory { HomeUseCase(get()) }
    factory { ConsultAndScheduleUseCase(get()) }
    factory { AppointmentDetailsUseCase(get(),get(),get()) }

    viewModel { HomeViewModel(get(),get(),get(),get()) }
    viewModel { ConsultAndScheduleViewModel(get(),get(),get(),get(),get()) }
    viewModel { AppointmentDetailsViewModel(get(),get(),get(),get()) }

}

