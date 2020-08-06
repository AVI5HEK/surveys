package com.avi5hek.surveys.presentation.feature.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.avi5hek.surveys.core.PagingFlowableFactory
import com.avi5hek.surveys.core.SingleLiveEvent
import com.avi5hek.surveys.core.ViewState
import com.avi5hek.surveys.core.base.BaseViewModel
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.presentation.model.Survey

/**
 * Created by "Avishek" on 8/3/2020.
 */
class MainViewModel
@ViewModelInject
constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val schedulerProvider: SchedulerProvider,
  private val pagingFlowableFactory: PagingFlowableFactory<Survey>
) :
  BaseViewModel() {

  val surveysLiveData by lazy { SingleLiveEvent<ViewState<PagingData<Survey>>>() }
  val retryLoadingLiveData by lazy { SingleLiveEvent<Unit>() }

  private val surveysFlowable by lazy { pagingFlowableFactory.create() }

  fun getData() {
    compositeDisposable.add(
      surveysFlowable
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe(
          {
            surveysLiveData.value = ViewState.Success(it)
          },
          {
            surveysLiveData.value = ViewState.Error(it) {
              getData()
            }
          }
        )
    )
  }

  fun retryLoading(throwable: Throwable) {
    showError(throwable) {
      retryLoadingLiveData.value = Unit
    }
  }

  override fun onClear() {
    pagingFlowableFactory.dispose()
  }
}
