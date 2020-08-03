package com.avi5hek.surveys.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.avi5hek.surveys.core.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

  sealed class Progress {

    object Show : Progress()
    object Hide : Progress()
  }

  data class ErrorEvent(val throwable: Throwable, val onRetry: (() -> Unit)? = null)

  protected val compositeDisposable by lazy { CompositeDisposable() }
  private val progressLiveData = SingleLiveEvent<Progress>()

  private var progressCount = 0
  private val errorLiveData = SingleLiveEvent<ErrorEvent>()

  val progress: LiveData<Progress>
    get() = progressLiveData

  val error: LiveData<ErrorEvent>
    get() = errorLiveData

  fun showProgress() {
    if (++progressCount == 1) {
      progressLiveData.value = Progress.Show
    }
  }

  fun hideProgress() {
    if (--progressCount == 0) {
      progressLiveData.value = Progress.Hide
    }

    if (progressCount < 0) {
      progressCount = 0
    }
  }

  fun showError(throwable: Throwable, onRetry: (() -> Unit)? = null) {
    errorLiveData.value = ErrorEvent(throwable, onRetry)
  }

  override fun onCleared() {
    Timber.d("onCleared() called")
    onClear()
    super.onCleared()
  }

  abstract fun onClear()
}
