package com.avi5hek.surveys.core.base.interactor

import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver

abstract class ObservableUseCase<Result, in Params>(private val schedulerProvider: SchedulerProvider) :
  BaseReactiveUseCase() {

  protected abstract fun buildUseCaseObservable(params: Params): Observable<Result>

  fun execute(@NonNull observer: DisposableObserver<Result>, params: Params) {
    val observable: Observable<Result> = this.buildUseCaseObservable(params)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
    addDisposable(observable.subscribeWith(observer))
  }
}
