package com.avi5hek.surveys.presentation.feature.main

import androidx.hilt.lifecycle.ViewModelInject
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
import com.avi5hek.surveys.presentation.model.Survey
import io.reactivex.Single

/**
 * Created by "Avishek" on 8/3/2020.
 */
class MainViewModel
@ViewModelInject
constructor(private val schedulerProvider: SchedulerProvider) : BaseViewModel() {

  val surveysLiveData by lazy { SingleLiveEvent<ViewState<PagingData<Survey>>>() }
  private val surveysFlowable by lazy {
    Pager(
      config = PagingConfig(
        pageSize = 2,
        prefetchDistance = 1,
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
      if (!singleEmitter.isDisposed) {
        // TODO: execute use case
        if (page < 3) {
          val list = listOf(
            Survey(
              "1",
              "Title 1",
              "Description 1",
              "https://dhdbhh0jsld0o.cloudfront.net/m/1ea51560991bcb7d00d0_l"
            ),
            Survey(
              "2",
              "Title 2",
              "Description 2",
              "https://dhdbhh0jsld0o.cloudfront.net/m/287db81c5e4242412cc0_l"
            ),
            Survey(
              "1",
              "Title 3",
              "Description 3",
              "https://dhdbhh0jsld0o.cloudfront.net/m/1ea51560991bcb7d00d0_l"
            ),
            Survey(
              "2",
              "Title 4",
              "Description 4",
              "https://dhdbhh0jsld0o.cloudfront.net/m/287db81c5e4242412cc0_l"
            ),

            Survey(
              "1",
              "Title 5",
              "Description 5",
              "https://dhdbhh0jsld0o.cloudfront.net/m/1ea51560991bcb7d00d0_l"
            ),
            Survey(
              "2",
              "Title 6",
              "Description6",
              "https://dhdbhh0jsld0o.cloudfront.net/m/287db81c5e4242412cc0_l"
            )
          )
          singleEmitter.onSuccess(list)
        } else {
          singleEmitter.onError(Throwable("Test error"))
        }
      }
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
    // TODO: dispose use cases here
  }
}
