package com.ramotion.showroom.examples.dribbbleshots.domain.datasource

import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import io.reactivex.Completable
import io.reactivex.Observable

interface DribbbleShotsDataSource {

  fun getDribbbleShots(page: Int, perPage: Int): Observable<List<DribbbleShot>>

  fun getDribbbleShot(id: Int): Observable<DribbbleShot>

  fun saveDribbbleShot(dribbbleShot: DribbbleShot): Completable
}