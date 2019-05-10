package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots

import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.LoadNextDribbbleShotsPageUseCase
import com.ramotion.showroom.examples.dribbbleshots.domain.interactors.ObserveDribbbleShotsUseCase
import com.ramotion.showroom.examples.dribbbleshots.utils.BaseViewModel
import io.reactivex.Observable
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots.DribbbleShotsIntent.*
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots.DribbbleShotsStateChange.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots.ShotsListItem.*

class DribbbleShotsViewModel(
    private val observeDribbbleShotsUseCase: ObserveDribbbleShotsUseCase,
    private val loadNextDribbbleShotsPageUseCase: LoadNextDribbbleShotsPageUseCase) : BaseViewModel<DribbbleShotsState>() {

  override fun initState(): DribbbleShotsState = DribbbleShotsState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(listOf(
        observeDribbbleShotsUseCase.execute()
            .map { DribbbleShotsReceived(it) }
            .cast(DribbbleShotsStateChange::class.java)
            .startWith(StartLoading)
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    ))

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(
        intentStream.ofType(GetNextDribbbleShotsPage::class.java)
            .switchMap { event ->
              loadNextDribbbleShotsPageUseCase.execute(event.page)
                  .toSingleDefault(Idle)
                  .toObservable()
                  .cast(DribbbleShotsStateChange::class.java)
                  .startWith(StartLoadingNextPage)
                  .onErrorResumeNext { e: Throwable -> handleError(e) }
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
            }
    ))

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: DribbbleShotsState, stateChange: Any): DribbbleShotsState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)

      is StartLoadingNextPage -> previousState.copy(
          shots =
          if (!previousState.shots.contains(ShotsLoadingItem)) previousState.shots + ShotsLoadingItem
          else previousState.shots
      )

      is DribbbleShotsReceived -> previousState.copy(
          loading = false,
          shots = if (stateChange.shots.isNotEmpty()) stateChange.shots.map { DribbbleShotItem(it) } else listOf(DribbblePlaceholder)
      )

      is Error -> previousState.copy(loading = false, error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }
}