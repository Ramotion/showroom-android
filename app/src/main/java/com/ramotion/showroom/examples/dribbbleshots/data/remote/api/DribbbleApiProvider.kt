package com.ramotion.showroom.examples.dribbbleshots.data.remote.api

import android.content.Context
import com.github.aurae.retrofit2.LoganSquareConverterFactory
import com.ramotion.showroom.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object DribbbleApiProvider {

  private lateinit var authApi: DribbbleApi
  private lateinit var api: DribbbleApi

  fun getApi(context: Context): DribbbleApi {
    if (!this::api.isInitialized) {
      val client = OkHttpClient.Builder()
          .addInterceptor((Interceptor { chain ->
            val original = chain.request()
            val dribbbleToken = context.getSharedPreferences("Showroom", Context.MODE_PRIVATE).getString("dribbbleToken", "")
            val interceptedRequestBuilder = original.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .method(original.method(), original.body())
            if (dribbbleToken?.isNotBlank() == true) {
              interceptedRequestBuilder.addHeader("Authorization", "Bearer $dribbbleToken")
            }
            val interceptedRequest = interceptedRequestBuilder.build()

            return@Interceptor chain.proceed(interceptedRequest)
          }))
          .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
          })
          .build()

      api = Retrofit.Builder()
          .baseUrl("https://api.dribbble.com/")
          .client(client)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(LoganSquareConverterFactory.create())
          .build()
          .create(DribbbleApi::class.java)
    }

    return api
  }

  fun getAuthApi(): DribbbleApi {
    if (!this::authApi.isInitialized) {
      val client = OkHttpClient.Builder()
          .addInterceptor((Interceptor { chain ->
            val original = chain.request()
            val interceptedRequest = original.newBuilder()
                .header("Content-Type", "application/json")
                .method(original.method(), original.body())
                .build()

            return@Interceptor chain.proceed(interceptedRequest)
          }))
          .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
          })
          .build()

      authApi = Retrofit.Builder()
          .baseUrl("https://dribbble.com/")
          .client(client)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(LoganSquareConverterFactory.create())
          .build()
          .create(DribbbleApi::class.java)
    }

    return authApi
  }
}