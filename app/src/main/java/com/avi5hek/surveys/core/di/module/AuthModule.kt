package com.avi5hek.surveys.core.di.module

import com.avi5hek.surveys.BuildConfig
import com.avi5hek.surveys.core.di.qualifier.AuthApiQualifier
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.data.dao.TokenDao
import com.avi5hek.surveys.data.preference.AppPreferenceHelper
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class AuthModule {

  @Binds
  abstract fun bindAuthDao(appPreferenceHelper: AppPreferenceHelper): TokenDao

  companion object {

    @Singleton
    @Provides
    @AuthApiQualifier
    fun provideAuthApiOkHttpClient(
      httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
      val httpClient = OkHttpClient.Builder()
      httpClient.addInterceptor(httpLoggingInterceptor)
      httpClient.connectTimeout(NetworkModule.CONNECT_TIMEOUT_S, TimeUnit.SECONDS)
      httpClient.readTimeout(NetworkModule.READ_TIMEOUT_S, TimeUnit.SECONDS)
      httpClient.writeTimeout(NetworkModule.WRITE_TIMEOUT_S, TimeUnit.SECONDS)
      return httpClient.build()
    }

    @Singleton
    @Provides
    @AuthApiQualifier
    fun provideAuthApiRetrofit(
      @AuthApiQualifier httpClient: OkHttpClient,
      schedulerProvider: SchedulerProvider,
      gson: Gson
    ): Retrofit {
      return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(
          RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io())
        )
        .client(httpClient)
        .build()
    }
  }
}
