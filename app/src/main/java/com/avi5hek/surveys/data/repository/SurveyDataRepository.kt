package com.avi5hek.surveys.data.repository

import com.avi5hek.surveys.core.NetworkHandler
import com.avi5hek.surveys.core.exception.ErrorHelper
import com.avi5hek.surveys.core.exception.NetworkConnectionException
import com.avi5hek.surveys.core.exception.ServerException
import com.avi5hek.surveys.data.model.ErrorResponse
import com.avi5hek.surveys.data.service.SurveyService
import com.avi5hek.surveys.domain.repository.SurveyRepository
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Converter
import javax.inject.Inject
import com.avi5hek.surveys.domain.model.Survey as DomainSurvey

/**
 * Created by "Avishek" on 8/3/2020.
 */
class SurveyDataRepository
@Inject
constructor(
  private val networkHandler: NetworkHandler,
  private val surveyService: SurveyService,
  private val errorConverter: Converter<ResponseBody, ErrorResponse>
) : SurveyRepository {

  override fun getSurveys(page: Int, pageSize: Int): Single<List<DomainSurvey>> {
    if (!networkHandler.isConnected) {
      return Single.error(NetworkConnectionException())
    }
    return surveyService.getSurveys(page, pageSize)
      .compose(ErrorHelper.applyError(errorConverter))
      .map { response ->
        val surveys = response.body()
        surveys?.map { DomainSurvey(it.id, it.title, it.description, it.coverImageUrl + "l") }
          ?: throw ServerException()
      }
  }
}
