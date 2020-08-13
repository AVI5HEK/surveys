package com.avi5hek.surveys.presentation.feature.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.avi5hek.surveys.core.PagingLiveDataFactory
import com.avi5hek.surveys.core.base.BaseViewModel
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.presentation.model.SurveyUiModel
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by "Avishek" on 8/6/2020.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

  private val testDispatcher = TestCoroutineDispatcher()
  private val testScope = TestCoroutineScope(testDispatcher)

  @Rule
  @JvmField
  val instantExecutorRule = InstantTaskExecutorRule()

  @Mock
  private lateinit var savedStateHandle: SavedStateHandle

  @Mock
  private lateinit var schedulerProvider: SchedulerProvider

  @Mock
  private lateinit var surveysObserver: Observer<PagingData<SurveyUiModel>>

  @Mock
  private lateinit var retryObserver: Observer<Unit>

  @Mock
  private lateinit var errorObserver: Observer<BaseViewModel.ErrorEvent>

  @Mock
  private lateinit var pagingLiveDataFactory: PagingLiveDataFactory<SurveyUiModel>

  private lateinit var viewModel: MainViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)

    viewModel =
      MainViewModel(
        savedStateHandle,
        pagingLiveDataFactory
      )
    viewModel.retryLoadingLiveData.observeForever(retryObserver)
    viewModel.error.observeForever(errorObserver)
    viewModel.error.observeForever {
      it.onRetry?.invoke()
    }
  }

  @Test
  fun `should get data`() {
    val mockPagingData = PagingData.from(listOf<SurveyUiModel>())
    whenever(pagingLiveDataFactory.create()).thenReturn(MutableLiveData(mockPagingData))

    viewModel.surveysLiveData.observeForever(surveysObserver)

    verify(pagingLiveDataFactory).create()
    verify(surveysObserver).onChanged(eq(mockPagingData))
    verifyNoMoreInteractions(pagingLiveDataFactory)
  }

  @Test
  fun `observer should get error event dispatch when retrying`() {
    val testThrowable = Throwable()

    viewModel.retryLoading(testThrowable)

    verify(errorObserver).onChanged(any())
  }

  @Test
  fun `observer should get retry dispatch`() {
    viewModel.retryLoading(Throwable())

    verify(retryObserver).onChanged(any())
  }

  @Test
  fun `should dispose pagingFlowableFactory in onClear()`() {
    viewModel.onClear()

    verify(pagingLiveDataFactory).dispose()
    verifyNoMoreInteractions(pagingLiveDataFactory)
    verifyNoMoreInteractions(schedulerProvider)
  }

  @After
  fun tearDown() {
    viewModel.onClear()
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
    testScope.cleanupTestCoroutines()
  }

}
