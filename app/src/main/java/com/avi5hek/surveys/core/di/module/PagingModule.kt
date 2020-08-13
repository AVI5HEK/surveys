package com.avi5hek.surveys.core.di.module

import com.avi5hek.surveys.core.PagingFlowableFactory
import com.avi5hek.surveys.presentation.feature.main.SurveyPagingFlowableFactory
import com.avi5hek.surveys.presentation.model.SurveyUiModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by "Avishek" on 8/7/2020.
 */
@InstallIn(ApplicationComponent::class)
@Module
abstract class PagingModule {

  @Binds
  abstract fun bindSurveyPagingFlowableFactory(surveyPagingFlowableFactory: SurveyPagingFlowableFactory): PagingFlowableFactory<SurveyUiModel>
}
