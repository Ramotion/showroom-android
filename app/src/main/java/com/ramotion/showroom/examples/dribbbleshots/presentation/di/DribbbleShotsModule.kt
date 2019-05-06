package com.ramotion.showroom.examples.dribbbleshots.presentation.di

import com.ramotion.showroom.examples.dribbbleshots.data.GlideImageLoader
import com.ramotion.showroom.examples.dribbbleshots.data.remote.api.DribbbleApi
import com.ramotion.showroom.examples.dribbbleshots.data.remote.api.DribbbleApiProvider
import com.ramotion.showroom.examples.dribbbleshots.data.remote.datasource.DribbbleShotsFirebaseDataSource
import com.ramotion.showroom.examples.dribbbleshots.data.remote.datasource.DribbbleShotsRemoteDataSource
import com.ramotion.showroom.examples.dribbbleshots.data.remote.repository.DribbbleShotsRepositoryImpl
import com.ramotion.showroom.examples.dribbbleshots.data.remote.repository.DribbbleUserRepositoryImpl
import com.ramotion.showroom.examples.dribbbleshots.domain.ImageLoader
import com.ramotion.showroom.examples.dribbbleshots.domain.datasource.DribbbleShotsDataSource
import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.GetDribbbleShotUseCase
import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.LoadNextDribbbleShotsPageUseCase
import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.ObserveDribbbleShotsUseCase
import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.SaveDribbbleShotUseCase
import com.ramotion.showroom.examples.dribbbleshots.domain.repository.DribbbleShotsRepository
import com.ramotion.showroom.examples.dribbbleshots.domain.repository.DribbbleUserRepository
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots.DribbbleShotsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dribbbleShotsModule = module {

  single<ImageLoader> { GlideImageLoader }

  single("main") { DribbbleApiProvider.getApi(androidContext()) }
  single("auth") { DribbbleApiProvider.getAuthApi() }

  single<DribbbleUserRepository> { DribbbleUserRepositoryImpl(get("main")) }

  single<DribbbleShotsDataSource>("firebase") { DribbbleShotsFirebaseDataSource() }
  single<DribbbleShotsDataSource>("remote") { DribbbleShotsRemoteDataSource(get(), get("main")) }
  single<DribbbleShotsRepository> { DribbbleShotsRepositoryImpl(get("firebase"), get("remote")) }

  factory { ObserveDribbbleShotsUseCase(get()) }
  factory { LoadNextDribbbleShotsPageUseCase(get()) }
  factory { GetDribbbleShotUseCase(get()) }
  factory { SaveDribbbleShotUseCase(get()) }

  viewModel { DribbbleShotsViewModel(get(), get()) }
}