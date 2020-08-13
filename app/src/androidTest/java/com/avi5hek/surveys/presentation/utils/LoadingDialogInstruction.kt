package com.avi5hek.surveys.presentation.utils

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.avi5hek.surveys.core.CircularProgressDialogFragment
import com.avi5hek.surveys.core.extension.cast
import com.azimolabs.conditionwatcher.Instruction

/**
 * Created by "Avishek" on 8/14/2020.
 */
class LoadingDialogInstruction(private val fragmentActivity: FragmentActivity?) :
  Instruction() {

  override fun getDescription(): String {
    return "Loading dialog shouldn't be in view hierarchy"
  }

  override fun checkCondition(): Boolean {
    val dialogFragment: DialogFragment? =
      fragmentActivity
        ?.supportFragmentManager
        ?.findFragmentByTag(CircularProgressDialogFragment.fragmentTag)
        .cast<DialogFragment>()
    return dialogFragment == null
  }
}
