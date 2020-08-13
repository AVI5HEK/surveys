package com.avi5hek.surveys.core.di.module

import android.content.Context
import android.content.SharedPreferences
import com.avi5hek.surveys.core.constant.PREFERENCE_NAME
import com.avi5hek.surveys.core.di.qualifier.MainCoroutineScopeQualifier
import com.avi5hek.surveys.core.exception.ErrorConverter
import com.avi5hek.surveys.core.scheduler.AndroidSchedulerProvider
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.data.model.ErrorResponse
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.ResponseBody
import retrofit2.Converter

@InstallIn(ApplicationComponent::class)
@Module
abstract class AppModule {

  @Binds
  abstract fun bindSchedulerProvider(androidSchedulerProvider: AndroidSchedulerProvider): SchedulerProvider

  @Binds
  abstract fun bindConverter(errorConverter: ErrorConverter): Converter<ResponseBody, ErrorResponse>

  companion object {

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
      return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @MainCoroutineScopeQualifier
    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)

  }
}
