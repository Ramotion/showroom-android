package com.ramotion.showroom.examples.dribbbleshots.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ramotion.showroom.examples.dribbbleshots.data.remote.entity.DribbbleShotFirebase
import com.ramotion.showroom.examples.dribbbleshots.domain.datasource.DribbbleShotsDataSource
import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import io.reactivex.Completable
import io.reactivex.Observable

class DribbbleShotsFirebaseDataSource : DribbbleShotsDataSource {

  private val db = FirebaseFirestore.getInstance().collection("shots")
  private var firebaseAuth = FirebaseAuth.getInstance()

  override fun getDribbbleShots(page: Int, perPage: Int): Observable<List<DribbbleShot>> =
    authUserToFirebaseIfNeed().switchMap {
      Observable.create<List<DribbbleShot>> { emitter ->
        db.get()
            .addOnSuccessListener { snapshot ->
              emitter.onNext((snapshot.toObjects(DribbbleShotFirebase::class.java)).map { it.toDomain() })
            }
            .addOnFailureListener {
              emitter.onError(it)
            }
      }
    }

  override fun getDribbbleShot(id: Int): Observable<DribbbleShot> =
    authUserToFirebaseIfNeed().switchMap {
      Observable.create<DribbbleShot> { emitter ->
        db.get()
            .addOnSuccessListener { snapshot ->
              emitter.onNext(
                  (snapshot.toObjects(DribbbleShotFirebase::class.java))
                      .map { it.toDomain() }
                      .find { shot -> shot.id == id } ?: DribbbleShot.EMPTY)
            }
            .addOnFailureListener {
              emitter.onError(it)
            }
      }
    }

  override fun saveDribbbleShot(dribbbleShot: DribbbleShot): Completable =
    authUserToFirebaseIfNeed().switchMapCompletable {
      Completable.create { emitter ->
        val firebaseShot = dribbbleShot.toFirebase()
        db.add(firebaseShot)
            .addOnSuccessListener {
              emitter.onComplete()
            }
            .addOnFailureListener {
              emitter.onError(it)
            }
            .addOnCompleteListener {
              emitter.onComplete()
            }
            .addOnCanceledListener {
              emitter.onError(Throwable("Cancelled"))
            }
      }
    }


  private fun authUserToFirebaseIfNeed(): Observable<FirebaseUser> =
    if (firebaseAuth.currentUser == null) {
      Observable.create { emitter ->
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener {
              if (it.isSuccessful) {
                emitter.onNext(firebaseAuth.currentUser!!)
              } else {
                emitter.onError(Throwable("Error while auth to Firebase"))
              }
            }
      }
    } else {
      Observable.just(firebaseAuth.currentUser)
    }

  private fun DribbbleShotFirebase.toDomain() = DribbbleShot(
      title = title ?: "",
      htmlUrl = html_url ?: "",
      id = id ?: 0,
      userId = userId ?: 0,
      message = message ?: "",
      saved = true)

  private fun DribbbleShot.toFirebase() = DribbbleShotFirebase(
      date = System.currentTimeMillis(),
      title = title,
      html_url = htmlUrl,
      id = id,
      userId = userId,
      message = message)
}