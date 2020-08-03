package com.avi5hek.surveys.core.base.interactor

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseReactiveUseCase {

  private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

  protected fun addDisposable(disposable: Disposable) {
    compositeDisposable.add(disposable)
  }

  fun dispose() = if (!compositeDisposable.isDisposed) compositeDisposable.dispose() else Unit
}
