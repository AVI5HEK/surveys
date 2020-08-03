package com.avi5hek.surveys.core

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.avi5hek.surveys.R

open class CircularProgressDialogFragment : DialogFragment() {

  private fun disableDismissOnBackPress() {
    dialog?.setOnKeyListener { dialog, keyCode, event ->
      if (keyCode == KeyEvent.KEYCODE_BACK) {
        if (event.action != KeyEvent.ACTION_DOWN) {
          true
        } else {
          true
        }
      } else {
        false
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_dialog_circular_progress, container, false)
    return view
  }

  override fun onResume() {
    super.onResume()
    dialog?.window?.setLayout(
      WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.MATCH_PARENT
    )
    disableDismissOnBackPress()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    dialog.window?.let {
      // request a window without the title
      it.requestFeature(Window.FEATURE_NO_TITLE)
      // set dialog background_tinted color to transparent
      it.setBackgroundDrawableResource(android.R.color.transparent)
    }
    return dialog
  }

  companion object {

    val fragmentTag: String = CircularProgressDialogFragment::class.java.simpleName

    fun newInstance(): CircularProgressDialogFragment {
      return CircularProgressDialogFragment()
    }
  }
}
