package com.avi5hek.surveys.domain.interactor

import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.domain.model.Survey
import com.avi5hek.surveys.domain.repository.SurveyRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by "Avishek" on 8/7/2020.
 */
@RunWith(MockitoJUnitRunner::class)
class GetSurveysUseCaseTest {

  companion object {
    const val PAGE = 1
    const val PAGE_SIZE = 10
  }

  @Mock
  private lateinit var schedulerProvider: SchedulerProvider

  @Mock
  private lateinit var surveyRepository: SurveyRepository

  private lateinit var getSurveysUseCase: GetSurveysUseCase

  private val testScheduler = TestScheduler()

  @Before
  fun setUp() {
    whenever(schedulerProvider.io()).thenReturn(testScheduler)
    whenever(schedulerProvider.ui()).thenReturn(testScheduler)

    getSurveysUseCase = GetSurveysUseCase(schedulerProvider, surveyRepository)
  }

  @Test
  fun `should get a list of survey`() {
    val id = "testId"
    val title = "testTitle"
    val description = "testDescription"
    val imageUrl = "testImageUrl"
    val testSurveys = listOf(Survey(id, title, description, imageUrl))

    whenever(surveyRepository.getSurveys(any(), any())).thenReturn(Single.just(testSurveys))

    getSurveysUseCase.execute(object : DisposableSingleObserver<List<Survey>>() {
      override fun onSuccess(t: List<Survey>) {
        assertTrue(t.isNotEmpty())
      }

      override fun onError(e: Throwable) {
        // no op
      }
    }, GetSurveysUseCase.Params(PAGE, PAGE_SIZE))

    testScheduler.triggerActions()

    verify(surveyRepository).getSurveys(any(), any())
    verifyNoMoreInteractions(surveyRepository)

    inOrder(schedulerProvider) {
      verify(schedulerProvider).io()
      verify(schedulerProvider).ui()
    }
  }

}
