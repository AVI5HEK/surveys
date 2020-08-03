package com.avi5hek.surveys.data.service

import com.avi5hek.surveys.core.constant.GET_SURVEYS
import com.avi5hek.surveys.data.model.Survey
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by "Avishek" on 8/3/2020.
 */
interface SurveyService {

  @GET(GET_SURVEYS)
  fun getSurveys(
    @Query("page") page: Int,
    @Query("per_page") pageSize: Int
  ): Single<Response<List<Survey>>>
}
