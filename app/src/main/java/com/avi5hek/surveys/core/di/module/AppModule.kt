package com.avi5hek.surveys.core.di.module

import android.content.Context
import android.content.SharedPreferences
import com.avi5hek.surveys.core.constant.PREFERENCE_NAME
import com.avi5hek.surveys.core.scheduler.AndroidSchedulerProvider
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class AppModule {

  @Binds
  abstract fun bindSchedulerProvider(androidSchedulerProvider: AndroidSchedulerProvider): SchedulerProvider

  companion object {

    @JvmStatic
    @Provides
    fun provideSharedPreference(context: Context): SharedPreferences {
      return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

  }
}
