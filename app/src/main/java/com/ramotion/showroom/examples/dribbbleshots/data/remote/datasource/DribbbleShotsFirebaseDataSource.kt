package com.ramotion.showroom.examples.dribbbleshots.data.remote.datasource

import com.google.firebase.database.*
import com.ramotion.showroom.examples.dribbbleshots.data.remote.entity.DribbbleShotFirebase
import com.ramotion.showroom.examples.dribbbleshots.domain.datasource.DribbbleShotsDataSource
import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import io.reactivex.Completable
import io.reactivex.Observable

class DribbbleShotsFirebaseDataSource : DribbbleShotsDataSource {

  private val db = FirebaseDatabase.getInstance().getReference("dribbbleShots")

  override fun getDribbbleShots(page: Int, perPage: Int): Observable<List<DribbbleShot>> =
      Observable.create { emitter ->
        db.addListenerForSingleValueEvent(object : ValueEventListener {
          override fun onCancelled(e: DatabaseError) {
            emitter.onError(e.toException().fillInStackTrace())
          }

          override fun onDataChange(data: DataSnapshot) {
            emitter.onNext((data.children).map { it.getValue(DribbbleShotFirebase::class.java) }.map { it!!.toDomain() })
          }
        })
      }

  override fun getDribbbleShot(id: Int): Observable<DribbbleShot> =
      Observable.create { emitter ->
        db.addListenerForSingleValueEvent(object : ValueEventListener {
          override fun onCancelled(e: DatabaseError) {
            emitter.onError(e.toException().fillInStackTrace())
          }

          override fun onDataChange(data: DataSnapshot) {
            val dribbbleShot = (data.children as List<DribbbleShotFirebase>).find { shot -> shot.id == id }
            emitter.onNext(dribbbleShot?.toDomain() ?: DribbbleShot.EMPTY)
          }
        })
      }

  override fun saveDribbbleShot(dribbbleShot: DribbbleShot): Completable =
      Completable.fromAction {
        val firebaseId = db.push().key
        db.child(firebaseId!!).setValue(dribbbleShot.toFirebase())
      }

  private fun DribbbleShotFirebase.toDomain() = DribbbleShot(
      title = title ?: "",
      htmlUrl = html_url ?: "",
      id = id ?: 0,
      userId = userId ?: 0,
      message = message ?: "",
      saved = true)

  private fun DribbbleShot.toFirebase() = DribbbleShotFirebase(title, htmlUrl, id, userId, message)
}