package com.ramotion.showroom.examples.dribbbleshots.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

interface BaseView<State> {

  fun initIntents()

  fun handleStates()

  fun render(state: State)
}


abstract class BaseViewModel<State> : ViewModel() {

  private val states = MutableLiveData<State>()
  private val viewIntentsConsumer: PublishRelay<Any> = PublishRelay.create()
  private var intentsDisposable: Disposable? = null

  protected abstract fun initState(): State

  private fun handleIntents() {
    intentsDisposable = Observable.merge(vmIntents(), viewIntents(viewIntentsConsumer))
      .scan(initState()) { previousState, stateChanges ->
        reduceState(previousState, stateChanges)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { state -> states.value = state }
  }

  protected open fun vmIntents(): Observable<Any> = Observable.never()

  protected abstract fun viewIntents(intentStream: Observable<*>): Observable<Any>

  protected abstract fun reduceState(previousState: State, stateChange: Any): State

  fun viewIntentsConsumer() = viewIntentsConsumer.also {
    if (intentsDisposable == null)
      handleIntents()
  }

  fun stateReceived(): LiveData<State> = states

  override fun onCleared() {
    intentsDisposable?.dispose()
  }
}


interface BackPressHandler {

  fun onBackPress(): Boolean
}