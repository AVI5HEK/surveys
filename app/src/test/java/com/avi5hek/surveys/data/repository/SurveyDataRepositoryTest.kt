package com.avi5hek.surveys.data.repository

import com.avi5hek.surveys.core.NetworkHandler
import com.avi5hek.surveys.core.exception.NetworkConnectionException
import com.avi5hek.surveys.core.exception.ServerException
import com.avi5hek.surveys.core.exception.TokenExpiredException
import com.avi5hek.surveys.data.model.ErrorResponse
import com.avi5hek.surveys.data.model.SurveyResponse
import com.avi5hek.surveys.data.service.SurveyService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Converter
import retrofit2.Response

/**
 * Created by "Avishek" on 8/7/2020.
 */
@RunWith(MockitoJUnitRunner::class)
class SurveyDataRepositoryTest {

  companion object {
    const val PAGE = 1
    const val PAGE_SIZE = 10
  }

  private val testSurvey = SurveyResponse("", "", "", "")

  @Mock
  private lateinit var networkHandler: NetworkHandler

  @Mock
  private lateinit var surveyService: SurveyService

  @Mock
  private lateinit var errorConverter: Converter<ResponseBody, ErrorResponse>

  private lateinit var surveyDataRepository: SurveyDataRepository

  @Before
  fun setUp() {
    surveyDataRepository = SurveyDataRepository(
      networkHandler,
      surveyService,
      errorConverter
    )
  }

  @Test
  fun `should get network connection error`() {
    whenever(networkHandler.isConnected).thenReturn(false)

    surveyDataRepository.getSurveys(PAGE, PAGE_SIZE)
      .subscribe(
        {
          //no op
        }, {
          assertTrue(it is NetworkConnectionException)
        }
      )
  }

  @Test
  fun `should get a non-empty list of survey`() {
    val response = Response.success(listOf(testSurvey))
    whenever(networkHandler.isConnected).thenReturn(true)
    whenever(surveyService.getSurveys(any(), any())).thenReturn(Single.just(response))

    surveyDataRepository.getSurveys(PAGE, PAGE_SIZE)
      .subscribe(
        {
          assertTrue(it.isNotEmpty())
        }, {
          assertFalse(it is NetworkConnectionException)
        }
      )
  }

  @Test
  fun `should call service method`() {
    val response = Response.success(listOf(testSurvey))
    whenever(networkHandler.isConnected).thenReturn(true)
    whenever(surveyService.getSurveys(any(), any())).thenReturn(Single.just(response))

    surveyDataRepository.getSurveys(PAGE, PAGE_SIZE)

    verify(surveyService).getSurveys(eq(PAGE), eq(PAGE_SIZE))
  }

  @Test
  fun `should get a server exception`() {
    val responseBody = Mockito.mock(ResponseBody::class.java)
    val response = Response.error<List<SurveyResponse>>(404, responseBody)
    whenever(networkHandler.isConnected).thenReturn(true)
    whenever(surveyService.getSurveys(any(), any())).thenReturn(Single.just(response))

    surveyDataRepository.getSurveys(PAGE, PAGE_SIZE)
      .subscribe(
        {
          // no op
        }, {
          assertTrue(it is ServerException)
        }
      )
  }

  @Test
  fun `should get unauthorized error`() {
    val responseBody = Mockito.mock(ResponseBody::class.java)
    val response = Response.error<List<SurveyResponse>>(401, responseBody)
    whenever(networkHandler.isConnected).thenReturn(true)
    whenever(surveyService.getSurveys(any(), any())).thenReturn(Single.just(response))

    surveyDataRepository.getSurveys(PAGE, PAGE_SIZE)
      .subscribe(
        {
          // no op
        }, {
          assertTrue(it is TokenExpiredException)
        }
      )
  }

}
