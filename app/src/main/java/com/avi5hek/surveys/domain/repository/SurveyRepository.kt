package com.avi5hek.surveys.domain.repository

import com.avi5hek.surveys.domain.model.Survey
import io.reactivex.Single

/**
 * Created by "Avishek" on 8/3/2020.
 */
interface SurveyRepository {

  fun getSurveys(page: Int, pageSize: Int): Single<List<Survey>>
}
