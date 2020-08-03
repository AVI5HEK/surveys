package com.avi5hek.surveys.presentation.feature.main

import androidx.navigation.fragment.navArgs
import com.avi5hek.surveys.R
import com.avi5hek.surveys.core.base.BaseFragment
import com.avi5hek.surveys.databinding.FragmentSurveyDetailsBinding


class SurveyDetailsFragment : BaseFragment<FragmentSurveyDetailsBinding>() {
  override val layoutId: Int
    get() = R.layout.fragment_survey_details

  override fun configureViews() {
    super.configureViews()
    val args: SurveyDetailsFragmentArgs by navArgs()
    binding?.name = args.hotelName
  }
}
