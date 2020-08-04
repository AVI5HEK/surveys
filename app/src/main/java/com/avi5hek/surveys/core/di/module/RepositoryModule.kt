package com.avi5hek.surveys.core.di.module

import com.avi5hek.surveys.data.repository.SurveyDataRepository
import com.avi5hek.surveys.domain.repository.SurveyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by "Avishek" on 8/3/2020.
 */
@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {

  @Binds
  abstract fun bindSurveyRepository(surveyDataRepository: SurveyDataRepository): SurveyRepository
}
