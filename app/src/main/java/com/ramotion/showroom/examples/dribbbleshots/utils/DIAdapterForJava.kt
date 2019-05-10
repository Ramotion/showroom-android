package com.ramotion.showroom.examples.dribbbleshots.utils

import android.content.ComponentCallbacks
import android.content.Context
import com.ramotion.showroom.examples.dribbbleshots.data.remote.api.DribbbleApi
import com.ramotion.showroom.examples.dribbbleshots.presentation.di.dribbbleShotsModule
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin

object DIAdapterForJava {

  fun startDI(context: Context,
              componentCallbacks: ComponentCallbacks) =
      componentCallbacks.startKoin(context, listOf(dribbbleShotsModule))

  fun getDribbbleAuthApi(componentCallbacks: ComponentCallbacks) = componentCallbacks.get<DribbbleApi>("auth")
}