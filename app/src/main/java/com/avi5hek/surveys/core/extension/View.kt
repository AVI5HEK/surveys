package com.avi5hek.surveys.core.extension

import android.os.SystemClock
import android.view.View

const val DEBOUNCE_DURATION = 500L

fun View.setOnClickListenerWithDebounce(onClick: (view: View) -> Unit) {
  this.setOnClickListener(OnClickListenerDebounceDecorator(View.OnClickListener { view ->
    view?.apply { onClick.invoke(this) }
  }))
}

class OnClickListenerDebounceDecorator(private val wrappee: View.OnClickListener) :
  View.OnClickListener {

  private var lastClickTime: Long = 0

  override fun onClick(view: View?) {
    if (SystemClock.elapsedRealtime() - lastClickTime > DEBOUNCE_DURATION) {
      view?.apply { wrappee.onClick(this) }
    }
    lastClickTime = SystemClock.elapsedRealtime()
  }
}
