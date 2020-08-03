package com.avi5hek.surveys.presentation.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avi5hek.surveys.R
import com.avi5hek.surveys.databinding.ItemSurveyBinding
import com.avi5hek.surveys.presentation.model.Survey
import com.bumptech.glide.Glide

/**
 * Created by "Avishek" on 8/3/2020.
 */
class SurveyListAdapter : PagingDataAdapter<Survey, SurveyListAdapter.ViewHolder>(COMPARATOR) {

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = DataBindingUtil.inflate<ItemSurveyBinding>(
      LayoutInflater.from(parent.context),
      R.layout.item_survey,
      parent,
      false
    )
    return ViewHolder(binding)
  }

  fun getData(position: Int): Survey? = getItem(position)

  inner class ViewHolder(private val binding: ItemSurveyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(survey: Survey?) {
      binding.survey = survey
      Glide.with(binding.root)
        .load(survey?.imageUrl)
        .into(binding.imageSurvey)
    }
  }

  companion object {
    private val COMPARATOR = object : DiffUtil.ItemCallback<Survey>() {

      override fun areItemsTheSame(oldItem: Survey, newItem: Survey): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: Survey, newItem: Survey): Boolean =
        oldItem == newItem
    }
  }
}
