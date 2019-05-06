package com.ramotion.showroom.examples.dribbbleshots.data.remote.datasource

import com.ramotion.showroom.examples.dribbbleshots.data.remote.api.DribbbleApi
import com.ramotion.showroom.examples.dribbbleshots.data.remote.entity.DribbbleShotR
import com.ramotion.showroom.examples.dribbbleshots.domain.datasource.DribbbleShotsDataSource
import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import com.ramotion.showroom.examples.dribbbleshots.domain.repository.DribbbleUserRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class DribbbleShotsRemoteDataSource(
    private val dribbbleUserRepository: DribbbleUserRepository,
    private val dribbbleApi: DribbbleApi) : DribbbleShotsDataSource {

  override fun getDribbbleShots(page: Int, perPage: Int): Observable<List<DribbbleShot>> =
    dribbbleUserRepository.getDribbbleUser()
        .flatMap { user ->
          dribbbleApi.getDribbbleShots(page, perPage)
              .map { shots -> shots.filter { it.animated!! } }
              .map { shots -> shots.map { it.toDomain(user.id) } }
              .subscribeOn(Schedulers.io())
        }


  override fun getDribbbleShot(id: Int): Observable<DribbbleShot> =
    dribbbleUserRepository.getDribbbleUser()
        .flatMap { user ->
          dribbbleApi.getDribbbleShot(id).map { it.toDomain(user.id) }
              .subscribeOn(Schedulers.io())
        }

  override fun saveDribbbleShot(dribbbleShot: DribbbleShot): Completable =
    throw UnsupportedOperationException("DribbbleShotsRemoteDataSource can't save dribbble shots")

  private fun DribbbleShotR.toDomain(userId: Int) =
    DribbbleShot(
        title = title ?: "",
        htmlUrl = htmlUrl ?: "",
        imageTeaser = images?.teaser ?: "",
        imageNormal = images?.normal ?: "",
        id = id ?: 0,
        userId = userId,
        message = "")
}