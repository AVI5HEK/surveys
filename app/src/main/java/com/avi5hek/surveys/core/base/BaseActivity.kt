package com.avi5hek.surveys.core.base

import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.avi5hek.surveys.core.ActivityNavigator
import com.avi5hek.surveys.core.CircularProgressDialogFragment
import com.avi5hek.surveys.core.extension.cast
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

  @Inject
  lateinit var activityNavigator: ActivityNavigator

  @get:LayoutRes
  protected abstract val layoutId: Int

  private var alertDialog: AlertDialog? = null

  protected lateinit var binding: B

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, layoutId)
    binding.lifecycleOwner = this
    initialize(savedInstanceState)
  }

  open fun initialize(savedInstanceState: Bundle?) {
    configureViews()
    bindWithViewModel()
  }

  open fun configureViews() {}

  open fun bindWithViewModel() {

    getBaseViewModel()?.let {
      it.progress.observe(this, Observer {
        when (it) {
          is BaseViewModel.Progress.Show -> {
            showProgress()
          }
          is BaseViewModel.Progress.Hide -> {
            hideProgress()
          }
        }
      })

      it.error.observe(this, Observer { errorEvent ->
        // TODO: move texts to strings.xml
        showDialog(
          "Error!",
          errorEvent.throwable.message,
          if (errorEvent.onRetry != null) "Retry" else null,
          if (errorEvent.onRetry != null) DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
            errorEvent.onRetry.invoke()
          } else null,
          if (errorEvent.onRetry != null) "Cancel" else "OK",
          DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
      })
    }
  }

  open fun getBaseViewModel(): BaseViewModel? {
    return null
  }

  open fun showProgress() {
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

  open fun hideProgress() {
    supportFragmentManager.executePendingTransactions()
    val fragment = supportFragmentManager
      .findFragmentByTag(CircularProgressDialogFragment.fragmentTag)
    fragment.cast<DialogFragment>()?.dismiss() ?: Timber.d("Failed to hide progress")
  }

  fun showDialog(
    message: String?,
    positiveButtonText: String?,
    onPositiveClickListener: DialogInterface.OnClickListener?,
    negativeButtonText: String? = null,
    onNegativeClickListener: DialogInterface.OnClickListener? = null
  ) {
    showDialog(
      null,
      message,
      positiveButtonText,
      onPositiveClickListener,
      negativeButtonText,
      onNegativeClickListener
    )
  }

  fun showDialog(
    title: String? = null,
    message: String?,
    positiveButtonText: String?,
    onPositiveClickListener: DialogInterface.OnClickListener?,
    negativeButtonText: String? = null,
    onNegativeClickListener: DialogInterface.OnClickListener? = null
  ) {
    if (alertDialog?.isShowing == true) {
      alertDialog?.dismiss()
    }
    alertDialog = AlertDialog.Builder(this)
      .setTitle(title)
      .setMessage(message)
      .setCancelable(false)
      .setPositiveButton(positiveButtonText, onPositiveClickListener)
      .setNegativeButton(negativeButtonText, onNegativeClickListener)
      .create()
    alertDialog?.show()
  }

  override fun onBackPressed() {
    if (isTaskRoot && supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.backStackEntryCount == 0) {
      finishAfterTransition()
    } else {
      super.onBackPressed()
    }
  }
}
