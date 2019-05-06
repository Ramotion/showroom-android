package com.ramotion.showroom.examples.dribbbleshots.data.remote.repository

import com.ramotion.showroom.examples.dribbbleshots.domain.datasource.DribbbleShotsDataSource
import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import com.ramotion.showroom.examples.dribbbleshots.domain.repository.DribbbleShotsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class DribbbleShotsRepositoryImpl(
    private val dribbbleShotsFirebase: DribbbleShotsDataSource,
    private val dribbbleShotsRemote: DribbbleShotsDataSource
) : DribbbleShotsRepository {

  private lateinit var firebaseShots: List<DribbbleShot>
  private val dribbbleShotsPublisher = BehaviorSubject.create<List<DribbbleShot>>()

  private val defaultPerPage = 20

  override fun observeDribbbleShots(): Observable<List<DribbbleShot>> =
    dribbbleShotsFirebase.getDribbbleShots(1, defaultPerPage)
        .doOnNext { firebaseShots = it }
        .switchMap { firebaseShots ->
          dribbbleShotsRemote.getDribbbleShots(1, defaultPerPage)
              .map { remoteShots -> checkSavedShots(firebaseShots, remoteShots) }
              .doOnNext { dribbbleShotsPublisher.onNext(it) }
              .switchMap { dribbbleShotsPublisher }
        }

  override fun loadNextDribbbleShotsPage(page: Int, perPage: Int): Completable =
    Completable.fromObservable(
        dribbbleShotsRemote.getDribbbleShots(page, defaultPerPage)
            .map { remoteShots -> checkSavedShots(firebaseShots, remoteShots) }
            .doOnNext { dribbbleShotsPublisher.onNext(dribbbleShotsPublisher.value!! + it) }
            .switchMap { dribbbleShotsPublisher }
    )

  override fun getDribbbleShot(id: Int): Observable<DribbbleShot> =
    Observable.fromCallable { dribbbleShotsPublisher.value!!.find { it.id == id }!! }

  override fun saveDribbbleShot(dribbbleShot: DribbbleShot): Completable =
    dribbbleShotsFirebase.saveDribbbleShot(dribbbleShot)
        .doOnComplete {
          dribbbleShotsPublisher.onNext(
              dribbbleShotsPublisher.value!!.map { if (it.id == dribbbleShot.id) dribbbleShot.copy(saved = true) else it }
          )
        }

  private fun checkSavedShots(firebaseShots: List<DribbbleShot>, remoteShots: List<DribbbleShot>): List<DribbbleShot> {
    for (firebaseShot in firebaseShots) {
      remoteShots.map { if (it.id == firebaseShot.id) it.saved = true else it }
    }
    return remoteShots
  }
}