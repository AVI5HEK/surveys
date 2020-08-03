package com.avi5hek.surveys.core

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GenericSavedStateViewModelFactory
@Inject
constructor(
  owner: SavedStateRegistryOwner,
  private val assistedFactory: SavedStateViewModelFactory<out ViewModel>,
  defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

  override fun <T : ViewModel?> create(
    key: String,
    modelClass: Class<T>,
    handle: SavedStateHandle
  ): T {
    try {
      return assistedFactory.create(handle) as T
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
}
