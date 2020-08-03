package com.avi5hek.surveys.presentation.feature.main

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.avi5hek.surveys.R
import com.avi5hek.surveys.core.ViewState
import com.avi5hek.surveys.core.base.BaseFragment
import com.avi5hek.surveys.core.extension.setOnClickListenerWithDebounce
import com.avi5hek.surveys.databinding.FragmentSurveyListBinding
import timber.log.Timber

class SurveyListFragment : BaseFragment<FragmentSurveyListBinding>() {
  override val layoutId: Int
    get() = R.layout.fragment_survey_list

  private val surveyListAdapter by lazy { SurveyListAdapter() }
  private val viewModel: MainViewModel by activityViewModels()

  override fun initialize(savedInstanceState: Bundle?) {
    super.initialize(savedInstanceState)
    viewModel.getData()
  }

  override fun configureViews() {
    super.configureViews()
    configureList()
    binding?.run {
      imageRefresh.setOnClickListenerWithDebounce {
        pager.setCurrentItem(0, false)
        surveyListAdapter.refresh()
      }

      buttonTakeSurvey.setOnClickListenerWithDebounce {
        val currentSurvey = surveyListAdapter.getData(pager.currentItem)
        Timber.i("Selected survey ID: ${currentSurvey?.id}")
        val action =
          SurveyListFragmentDirections.actionSurveyListFragmentToSurveyDetailFragment(currentSurvey?.title)
        findNavController().navigate(action)
      }
    }
  }

  private fun configureList() {
    binding?.run {
      surveyListAdapter.addLoadStateListener { loadStates ->
        when (loadStates.refresh) {
          is LoadState.Loading -> viewModel.showProgress()
          is LoadState.Error -> viewModel.hideProgress()
          else -> viewModel.hideProgress()
        }

        val errorState = loadStates.source.append as? LoadState.Error
          ?: loadStates.source.prepend as? LoadState.Error
          ?: loadStates.append as? LoadState.Error
          ?: loadStates.prepend as? LoadState.Error
        errorState?.let {
          viewModel.showError(it.error) {
            surveyListAdapter.retry()
          }
        }
      }

      pager.adapter = surveyListAdapter
    }
  }

  override fun bindWithViewModel() {
    super.bindWithViewModel()
    viewModel.surveysLiveData.observe(this, Observer {
      when (it) {
        is ViewState.Loading -> {
          // We don't need to show loading in this case.
          // Paging library handles the loading states.
        }
        is ViewState.Success -> surveyListAdapter.submitData(lifecycle, it.data)
        is ViewState.Error -> viewModel.showError(it.throwable, it.onRetry)
        else -> throw IllegalStateException("State $it is not allowed.")
      }
    })
  }
}
