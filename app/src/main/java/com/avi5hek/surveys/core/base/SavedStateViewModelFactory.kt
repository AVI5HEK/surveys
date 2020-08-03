package com.avi5hek.surveys.core.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface SavedStateViewModelFactory<T : ViewModel> {
  fun create(savedStateHandle: SavedStateHandle): T
}
