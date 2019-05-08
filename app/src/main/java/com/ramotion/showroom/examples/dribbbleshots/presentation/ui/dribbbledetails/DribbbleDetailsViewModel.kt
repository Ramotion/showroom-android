package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribbbledetails

import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.GetDribbbleShotUseCase
import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.SaveDribbbleShotUseCase
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribbbledetails.DribbbleDetailsIntent.*
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribbbledetails.DribbbleDetailsStateChange.*
import com.ramotion.showroom.examples.dribbbleshots.utils.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DribbbleDetailsViewModel(
    private val getDribbbleShotUseCase: GetDribbbleShotUseCase,
    private val saveDribbbleShotUseCase: SaveDribbbleShotUseCase
) : BaseViewModel<DribbbleDetailsState>() {

  override fun initState(): DribbbleDetailsState = DribbbleDetailsState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(
        intentStream.ofType(GetDribbbleShot::class.java)
            .switchMap { event ->
              getDribbbleShotUseCase.execute(event.id)
                  .map { DribbbleShotReceived(it) }
                  .cast(DribbbleDetailsStateChange::class.java)
                  .startWith(StartLoading)
                  .onErrorResumeNext { e: Throwable -> handleError(e) }
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
            },

        intentStream.ofType(SaveDribbbleShot::class.java)
            .switchMap { event ->
              saveDribbbleShotUseCase.execute(event.shot)
                  .toSingleDefault(DribbbleShotSaved)
                  .toObservable()
                  .cast(DribbbleDetailsStateChange::class.java)
                  .startWith(StartSaveLoading)
                  .onErrorResumeNext { e: Throwable -> handleError(e) }
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
            }
    ))

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: DribbbleDetailsState, stateChange: Any): DribbbleDetailsState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)
      is StartSaveLoading -> previousState.copy(saveLoading = true, error = null)
      is DribbbleShotReceived -> previousState.copy(loading = false, shot = stateChange.shot)
      is DribbbleShotSaved -> previousState.copy(saveLoading = false, shotSaved = true)
      is Error -> previousState.copy(loading = false, saveLoading = false, error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }
}