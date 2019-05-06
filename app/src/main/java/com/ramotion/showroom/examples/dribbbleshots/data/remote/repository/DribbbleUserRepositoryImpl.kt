package com.ramotion.showroom.examples.dribbbleshots.data.remote.repository

import com.ramotion.showroom.examples.dribbbleshots.data.remote.api.DribbbleApi
import com.ramotion.showroom.examples.dribbbleshots.data.remote.entity.DribbbleUserR
import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleUser
import com.ramotion.showroom.examples.dribbbleshots.domain.repository.DribbbleUserRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class DribbbleUserRepositoryImpl(private val dribbbleApi: DribbbleApi) : DribbbleUserRepository {

  private val userPublisher = BehaviorSubject.create<DribbbleUser>()

  override fun getDribbbleUser(): Observable<DribbbleUser> =
      if (userPublisher.value == null) {
        dribbbleApi.getDribbbleUser()
            .doOnNext { userPublisher.onNext(it.toDomain()) }
            .switchMap { userPublisher }
            .subscribeOn(Schedulers.io())
      } else {
        userPublisher
      }

  private fun DribbbleUserR.toDomain() = DribbbleUser(
      id = id ?: 0,
      name = name ?: "",
      avatarUrl = avatarUrl ?: ""
  )
}