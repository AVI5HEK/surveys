package com.avi5hek.surveys.core

sealed class ViewState<T> {
  data class Success<T>(val data: T) : ViewState<T>()
  data class Loading<T>(val data: T? = null) : ViewState<T>()
  data class Error<T>(val throwable: Throwable, val onRetry: (() -> Unit)? = null) : ViewState<T>()
}
