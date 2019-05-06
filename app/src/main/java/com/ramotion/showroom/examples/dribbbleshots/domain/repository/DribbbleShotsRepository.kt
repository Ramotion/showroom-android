package com.ramotion.showroom.examples.dribbbleshots.domain.repository

import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import io.reactivex.Completable
import io.reactivex.Observable

interface DribbbleShotsRepository {

  fun observeDribbbleShots(): Observable<List<DribbbleShot>>

  fun loadNextDribbbleShotsPage(page: Int, perPage: Int): Completable

  fun getDribbbleShot(id: Int): Observable<DribbbleShot>

  fun saveDribbbleShot(dribbbleShot: DribbbleShot): Completable
}