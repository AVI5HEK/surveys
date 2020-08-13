package com.avi5hek.surveys.presentation.feature.main

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.flowable
import com.avi5hek.surveys.core.GenericPagingSource
import com.avi5hek.surveys.core.PagingFlowableFactory
import com.avi5hek.surveys.core.di.qualifier.MainCoroutineScopeQualifier
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.domain.interactor.GetSurveysUseCase
import com.avi5hek.surveys.presentation.model.SurveyUiModel
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import com.avi5hek.surveys.domain.model.Survey as DomainSurvey

/**
 * Created by "Avishek" on 8/7/2020.
 */
class SurveyPagingFlowableFactory
@Inject
constructor(
  private val getSurveysUseCase: GetSurveysUseCase,
  private val schedulerProvider: SchedulerProvider,
  @MainCoroutineScopeQualifier private val coroutineScope: CoroutineScope
) : PagingFlowableFactory<SurveyUiModel> {

  override fun create(): Flowable<PagingData<SurveyUiModel>> {
    return Pager(
      config = PagingConfig(
        initialLoadSize = 10,
        pageSize = 5,
        prefetchDistance = 5,
        enablePlaceholders = false
      ),
      pagingSourceFactory = {
        object : GenericPagingSource<SurveyUiModel>() {
          override fun getSingle(page: Int, pageSize: Int): Single<List<SurveyUiModel>> =
            getSurveys(page, pageSize)
              .subscribeOn(schedulerProvider.io())
        }
      }
    ).flowable.cachedIn(coroutineScope)
  }

  private fun getSurveys(page: Int, pageSize: Int): Single<List<SurveyUiModel>> {
    return Single.create { singleEmitter ->
      getSurveysUseCase.execute(object :
        DisposableSingleObserver<List<DomainSurvey>>() {
        override fun onSuccess(surveys: List<DomainSurvey>) {
          if (!singleEmitter.isDisposed) {
            singleEmitter.onSuccess(surveys.map {
              SurveyUiModel(
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

      }, GetSurveysUseCase.Params(page, pageSize))
    }
  }

  override fun dispose() {
    getSurveysUseCase.dispose()
  }
}
