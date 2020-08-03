package com.avi5hek.surveys.core.base.interactor

import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableSingleObserver

abstract class SingleUseCase<Result, in Params>(private val schedulerProvider: SchedulerProvider) :
  BaseReactiveUseCase() {

  protected abstract fun buildUseCaseSingle(params: Params): Single<Result>

  fun execute(@NonNull observer: DisposableSingleObserver<Result>, params: Params) {
    val single: Single<Result> = this.buildUseCaseSingle(params)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
    addDisposable(single.subscribeWith(observer))
  }
}
