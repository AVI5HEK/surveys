package com.avi5hek.surveys.presentation.feature.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.avi5hek.surveys.core.PagingFlowableFactory
import com.avi5hek.surveys.core.ViewState
import com.avi5hek.surveys.core.base.BaseViewModel
import com.avi5hek.surveys.core.scheduler.SchedulerProvider
import com.avi5hek.surveys.presentation.model.Survey
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by "Avishek" on 8/6/2020.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

  private lateinit var testScheduler: TestScheduler

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
  private lateinit var surveysObserver: Observer<ViewState<PagingData<Survey>>>

  @Mock
  private lateinit var retryObserver: Observer<Unit>

  @Mock
  private lateinit var errorObserver: Observer<BaseViewModel.ErrorEvent>

  @Mock
  private lateinit var pagingFlowableFactory: PagingFlowableFactory<Survey>

  private lateinit var viewModel: MainViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)

    testScheduler = TestScheduler()
    whenever(schedulerProvider.io()).thenReturn(testScheduler)
    whenever(schedulerProvider.ui()).thenReturn(testScheduler)

    viewModel =
      MainViewModel(
        savedStateHandle,
        schedulerProvider,
        pagingFlowableFactory
      )
    viewModel.surveysLiveData.observeForever(surveysObserver)
    viewModel.retryLoadingLiveData.observeForever(retryObserver)
    viewModel.error.observeForever(errorObserver)
    viewModel.error.observeForever {
      it.onRetry?.invoke()
    }
  }

  @Test
  fun `should get data`() {
    val mockPagingData = PagingData.from(listOf<Survey>())
    whenever(pagingFlowableFactory.create()).thenReturn(Flowable.just(mockPagingData))

    viewModel.getData()
    testScheduler.triggerActions()

    verify(pagingFlowableFactory).create()
    verify(surveysObserver).onChanged(eq(ViewState.Success(mockPagingData)))
    verifyNoMoreInteractions(pagingFlowableFactory)
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

    verify(pagingFlowableFactory).dispose()
    verifyNoMoreInteractions(pagingFlowableFactory)
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
