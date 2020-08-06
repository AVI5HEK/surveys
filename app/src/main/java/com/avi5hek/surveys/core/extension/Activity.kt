package com.avi5hek.surveys.core.extension

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.avi5hek.surveys.core.CircularProgressDialogFragment
import timber.log.Timber

fun Activity.hideKeyboard() {
  hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
  val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun AppCompatActivity.showProgress() {
  var fragment: CircularProgressDialogFragment? = supportFragmentManager
    .findFragmentByTag(
      CircularProgressDialogFragment.fragmentTag
    ) as CircularProgressDialogFragment?
  if (fragment == null) {
    fragment = CircularProgressDialogFragment.newInstance()
  }
  if (!fragment.isAdded) {
    fragment
      .show(supportFragmentManager, CircularProgressDialogFragment.fragmentTag)
  }
}

fun AppCompatActivity.hideProgress() {
  supportFragmentManager.executePendingTransactions()
  val fragment = supportFragmentManager
    .findFragmentByTag(CircularProgressDialogFragment.fragmentTag)
  fragment.cast<DialogFragment>()?.dismiss() ?: Timber.d("Failed to hide progress")
}
