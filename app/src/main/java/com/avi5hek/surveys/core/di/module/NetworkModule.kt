package com.avi5hek.surveys.core.di.module

import com.avi5hek.surveys.BuildConfig
import com.avi5hek.surveys.core.GsonUtcDateAdapter
import com.avi5hek.surveys.core.SurveyTokenAuthenticator
import com.avi5hek.surveys.core.di.qualifier.HostApiQualifier
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.data.dao.TokenDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class NetworkModule {

  @Binds
  @HostApiQualifier
  abstract fun bindTokenAuthenticator(tokenAuthenticator: SurveyTokenAuthenticator): Authenticator

  companion object {
    internal const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_ACCESS_TOKEN = "access_token"
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val HEADER_CONTENT_TYPE_VALUE = "application/json"
    const val HEADER_AUTHORIZATION_TYPE = "Bearer "
    const val CONNECT_TIMEOUT_S: Long = 30
    const val READ_TIMEOUT_S: Long = 30
    const val WRITE_TIMEOUT_S: Long = 30

    @JvmStatic
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
      val interceptor = HttpLoggingInterceptor()
      interceptor.level = HttpLoggingInterceptor.Level.BODY
      return interceptor
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideInterceptor(tokenDao: TokenDao): Interceptor {
      return Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
        val accessToken = tokenDao.loadAccessToken()
        if (accessToken.isNotEmpty()) {
          requestBuilder.header(
            HEADER_AUTHORIZATION,
            HEADER_AUTHORIZATION_TYPE + accessToken
          )
        }
        val request = requestBuilder.build()
        chain.proceed(request)
      }
    }

    @JvmStatic
    @Singleton
    @Provides
    @HostApiQualifier
    fun provideOkHttpClient(
      httpLoggingInterceptor: HttpLoggingInterceptor,
      interceptor: Interceptor,
      @HostApiQualifier authenticator: Authenticator

    ): OkHttpClient {
      val httpClient = OkHttpClient.Builder()
      httpClient.addInterceptor(interceptor)
      httpClient.addInterceptor(httpLoggingInterceptor)
      httpClient.authenticator(authenticator)
      httpClient.connectTimeout(CONNECT_TIMEOUT_S, TimeUnit.SECONDS)
      httpClient.readTimeout(READ_TIMEOUT_S, TimeUnit.SECONDS)
      httpClient.writeTimeout(WRITE_TIMEOUT_S, TimeUnit.SECONDS)
      return httpClient.build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGson(): Gson {
      return GsonBuilder()
        .registerTypeAdapter(
          DateTime::class.java,
          GsonUtcDateAdapter()
        )
        .create()
    }

    @JvmStatic
    @Singleton
    @Provides
    @HostApiQualifier
    fun provideHostApiRetrofit(
      @HostApiQualifier httpClient: OkHttpClient,
      schedulerProvider: SchedulerProvider, gson: Gson
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
