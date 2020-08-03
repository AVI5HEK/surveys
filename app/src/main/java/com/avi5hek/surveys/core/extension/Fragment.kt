package com.avi5hek.surveys.core.extension

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.avi5hek.surveys.core.base.BaseActivity

val Fragment.baseActivity: BaseActivity<out ViewDataBinding>?
  get() {
    return this.activity?.cast<BaseActivity<out ViewDataBinding>>()
  }
