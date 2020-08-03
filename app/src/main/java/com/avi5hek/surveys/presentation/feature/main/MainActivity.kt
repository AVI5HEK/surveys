package com.avi5hek.surveys.presentation.feature.main

import androidx.activity.viewModels
import com.avi5hek.surveys.R
import com.avi5hek.surveys.core.base.BaseActivity
import com.avi5hek.surveys.core.base.BaseViewModel
import com.avi5hek.surveys.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

  override val layoutId: Int
    get() = R.layout.activity_main

  private val viewModel: MainViewModel by viewModels()

  override fun getBaseViewModel(): BaseViewModel? {
    return viewModel
  }
}
