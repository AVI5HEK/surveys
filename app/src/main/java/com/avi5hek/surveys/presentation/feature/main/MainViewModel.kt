package com.avi5hek.surveys.presentation.feature.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.avi5hek.surveys.core.PagingLiveDataFactory
import com.avi5hek.surveys.core.SingleLiveEvent
import com.avi5hek.surveys.core.base.BaseViewModel
import com.avi5hek.surveys.presentation.model.SurveyUiModel

/**
 * Created by "Avishek" on 8/3/2020.
 */
class MainViewModel
@ViewModelInject
constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val pagingLiveDataFactory: PagingLiveDataFactory<SurveyUiModel>
) :
  BaseViewModel() {

  val surveysLiveData by lazy { pagingLiveDataFactory.create() }
  val retryLoadingLiveData by lazy { SingleLiveEvent<Unit>() }

  fun retryLoading(throwable: Throwable) {
    showError(throwable) {
      retryLoadingLiveData.value = Unit
    }
  }

  override fun onClear() {
    pagingLiveDataFactory.dispose()
  }
}
