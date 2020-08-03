package com.avi5hek.surveys.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

  protected var binding: B? = null

  @get:LayoutRes
  protected abstract val layoutId: Int

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
    binding?.lifecycleOwner = this
    return binding?.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initialize(savedInstanceState)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    bindWithViewModel()
  }

  open fun initialize(savedInstanceState: Bundle?) {
    configureViews()
  }

  open fun configureViews() {

  }

  open fun bindWithViewModel() {

  }
}
