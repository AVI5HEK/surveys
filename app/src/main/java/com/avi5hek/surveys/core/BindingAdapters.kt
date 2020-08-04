package com.avi5hek.surveys.core

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Created by "Avishek" on 8/4/2020.
 */
@BindingAdapter("app:url")
fun setUrl(view: ImageView, url: String?) {
  Glide.with(view)
    .load(url)
    .into(view)
}
