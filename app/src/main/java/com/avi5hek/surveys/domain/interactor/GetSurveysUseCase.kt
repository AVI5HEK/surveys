package com.avi5hek.surveys.domain.interactor

import com.avi5hek.surveys.core.base.interactor.SingleUseCase
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.domain.model.Survey
import com.avi5hek.surveys.domain.repository.SurveyRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by "Avishek" on 8/3/2020.
 */
class GetSurveysUseCase
@Inject
constructor(schedulerProvider: SchedulerProvider, private val surveyRepository: SurveyRepository) :
  SingleUseCase<List<Survey>, GetSurveysUseCase.Params>(schedulerProvider) {

  data class Params(val page: Int, val pageSize: Int)

  override fun buildUseCaseSingle(params: Params): Single<List<Survey>> {
    return surveyRepository.getSurveys(params.page, params.pageSize)
  }
}
