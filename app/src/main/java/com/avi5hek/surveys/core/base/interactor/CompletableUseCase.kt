package com.avi5hek.surveys.core.base.interactor

import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableCompletableObserver

abstract class CompletableUseCase<in Params>(private val schedulerProvider: SchedulerProvider) :
  BaseReactiveUseCase() {

  protected abstract fun buildUseCaseCompletable(params: Params): Completable

  fun execute(@NonNull observer: DisposableCompletableObserver, params: Params) {
    val completable: Completable = this.buildUseCaseCompletable(params)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
    addDisposable(completable.subscribeWith(observer))
  }
}
