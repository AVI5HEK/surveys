package com.avi5hek.surveys.presentation.feature.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.flowable
import com.avi5hek.surveys.core.GenericPagingSource
import com.avi5hek.surveys.core.SingleLiveEvent
import com.avi5hek.surveys.core.ViewState
import com.avi5hek.surveys.core.base.BaseViewModel
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.domain.interactor.GetSurveys
import com.avi5hek.surveys.presentation.model.Survey
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import com.avi5hek.surveys.domain.model.Survey as DomainSurvey

/**
 * Created by "Avishek" on 8/3/2020.
 */
class MainViewModel
@ViewModelInject
constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val schedulerProvider: SchedulerProvider,
  private val getSurveys: GetSurveys
) :
  BaseViewModel() {

  val surveysLiveData by lazy { SingleLiveEvent<ViewState<PagingData<Survey>>>() }
  private val surveysFlowable by lazy {
    Pager(
      config = PagingConfig(
        initialLoadSize = 10,
        pageSize = 5,
        prefetchDistance = 5,
        enablePlaceholders = false
      ),
      pagingSourceFactory = {
        object : GenericPagingSource<Survey>() {
          override fun getSingle(page: Int, pageSize: Int): Single<List<Survey>> =
            fetchSurveys(page, pageSize)
              .subscribeOn(schedulerProvider.io())
        }
      }
    ).flowable.cachedIn(viewModelScope)
  }

  private fun fetchSurveys(page: Int, pageSize: Int): Single<List<Survey>> {
    return Single.create { singleEmitter ->
      getSurveys.execute(object : DisposableSingleObserver<List<DomainSurvey>>() {
        override fun onSuccess(surveys: List<DomainSurvey>) {
          if (!singleEmitter.isDisposed) {
            singleEmitter.onSuccess(surveys.map {
              Survey(
                it.id,
                it.title,
                it.description,
                it.imageUrl
              )
            })
          }
        }

        override fun onError(e: Throwable) {
          if (!singleEmitter.isDisposed) {
            singleEmitter.onError(e)
          }
        }

      }, GetSurveys.Params(page, pageSize))
    }
  }

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

  override fun onClear() {
    getSurveys.dispose()
  }
}
