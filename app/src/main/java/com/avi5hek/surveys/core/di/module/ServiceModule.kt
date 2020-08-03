package com.avi5hek.surveys.core.di.module

import com.avi5hek.surveys.core.di.qualifier.AuthApiQualifier
import com.avi5hek.surveys.core.di.qualifier.HostApiQualifier
import com.avi5hek.surveys.data.service.SurveyService
import com.avi5hek.surveys.data.service.TokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class ServiceModule {

    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideSurveyService(@HostApiQualifier retrofit: Retrofit): SurveyService {
            return retrofit.create(SurveyService::class.java)
        }

        @JvmStatic
        @Singleton
        @Provides
        fun provideTokenService(@AuthApiQualifier retrofit: Retrofit): TokenService {
            return retrofit.create(TokenService::class.java)
        }
    }
}
