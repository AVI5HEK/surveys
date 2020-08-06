package com.avi5hek.surveys.core

import com.avi5hek.surveys.core.di.module.AuthModule
import com.avi5hek.surveys.core.di.module.NetworkModule
import com.avi5hek.surveys.core.di.module.ServiceModule
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.data.dao.TokenDao
import com.avi5hek.surveys.data.model.TokenResponse
import com.avi5hek.surveys.data.service.TokenService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.TestScheduler
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by "Avishek" on 8/5/2020.
 */
@RunWith(MockitoJUnitRunner::class)
class SurveyTokenAuthenticatorTest {

  companion object {
    const val BASE_URL = "/"
  }

  private interface TestService {
    @GET("test")
    fun test(): Call<Unit>
  }

  private lateinit var httpUrl: HttpUrl

  @Mock
  private lateinit var schedulerProvider: SchedulerProvider

  @Mock
  private lateinit var tokenDao: TokenDao

  private lateinit var tokenService: TokenService

  private lateinit var testService: TestService

  private lateinit var mockServer: MockWebServer

  private lateinit var surveyTokenAuthenticator: SurveyTokenAuthenticator

  private val gson = NetworkModule.provideGson()

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    mockServer = MockWebServer()
    mockServer.start()
    httpUrl = mockServer.url(BASE_URL)

    whenever(schedulerProvider.io()).doReturn(TestScheduler())

    configureTokenService()
    configureTestService()
  }

  private fun configureTokenService() {
    val authHttpClient = AuthModule.provideAuthApiOkHttpClient(
      NetworkModule.provideHttpLoggingInterceptor()
    )

    val authRetrofit = Retrofit.Builder().baseUrl(httpUrl)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(
        RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io())
      )
      .client(authHttpClient)
      .build()

    tokenService = ServiceModule.provideTokenService(authRetrofit)
    surveyTokenAuthenticator = SurveyTokenAuthenticator(tokenService, tokenDao)
  }

  private fun configureTestService() {
    whenever(tokenDao.loadAccessToken()).thenReturn("")

    val authHttpClient = NetworkModule.provideOkHttpClient(
      NetworkModule.provideHttpLoggingInterceptor(),
      NetworkModule.provideInterceptor(tokenDao),
      surveyTokenAuthenticator
    )

    val hostRetrofit = Retrofit.Builder().baseUrl(httpUrl)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(
        RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io())
      )
      .client(authHttpClient)
      .build()

    testService = hostRetrofit.create(TestService::class.java)
  }

  @After
  fun tearDown() {
    mockServer.shutdown()
  }

  @Test
  fun `authentication should fail`() {
    val invalidTokenResponse = MockResponse().setResponseCode(401)
    val invalidRefreshResponse = MockResponse().setResponseCode(401)

    mockServer.enqueue(invalidTokenResponse)
    mockServer.enqueue(invalidRefreshResponse)

    val response = testService.test().execute()

    mockServer.takeRequest()
    mockServer.takeRequest()

    assertFalse(response.isSuccessful)
    assertTrue(response.code() == 401)
  }

  @Test
  fun `test response should have access token in header after refreshing token`() {

    val invalidTokenResponse = MockResponse().setResponseCode(401)
    val validTokenResponse = MockResponse().setResponseCode(200)

    val tokenResponse = TokenResponse("testToken", "Bearer")
    val validRefreshResponse = MockResponse()
      .setResponseCode(200)
      .setBody(gson.toJson(tokenResponse))

    whenever(tokenDao.loadAccessToken()).thenReturn(tokenResponse.accessToken)

    mockServer.enqueue(invalidTokenResponse)
    mockServer.enqueue(validRefreshResponse)
    mockServer.enqueue(validTokenResponse)

    val response = testService.test().execute()

    mockServer.takeRequest()
    mockServer.takeRequest()

    val retryRequest = mockServer.takeRequest()

    val header = retryRequest.getHeader("Authorization")
    assertEquals(header, "Bearer ${tokenResponse.accessToken}")
    assertEquals(tokenDao.loadAccessToken(), tokenResponse.accessToken)
    assertTrue(response.isSuccessful)
  }
}
