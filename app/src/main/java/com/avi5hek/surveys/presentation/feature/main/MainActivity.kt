package com.avi5hek.surveys.presentation.feature.main

import com.avi5hek.surveys.R
import com.avi5hek.surveys.core.base.BaseActivity
import com.avi5hek.surveys.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

  override val layoutId: Int
    get() = R.layout.activity_main
}
