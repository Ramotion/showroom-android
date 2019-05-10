package com.ramotion.showroom.examples.dribbbleshots.data.remote.api

import com.ramotion.showroom.examples.dribbbleshots.data.remote.entity.AuthResponse
import com.ramotion.showroom.examples.dribbbleshots.data.remote.entity.DribbbleShotR
import com.ramotion.showroom.examples.dribbbleshots.data.remote.entity.DribbbleUserR
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DribbbleApi {

  @POST("oauth/token")
  fun auth(@Query("client_id") clientId: String,
           @Query("client_secret") clientSecret: String,
           @Query("code") code: String,
           @Query("redirect_uri") redirectUri: String): Observable<AuthResponse>

  @GET("v2/user")
  fun getDribbbleUser(): Observable<DribbbleUserR>

  @GET("v2/user/shots")
  fun getDribbbleShots(@Query("page") page: Int,
                       @Query("perPage") perPage: Int): Observable<List<DribbbleShotR>>

  @GET("v2/user/shots/{id}")
  fun getDribbbleShot(@Path("id") id: Int): Observable<DribbbleShotR>
}