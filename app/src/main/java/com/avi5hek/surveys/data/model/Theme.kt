package com.avi5hek.surveys.data.model


import com.google.gson.annotations.SerializedName

data class Theme(
  @SerializedName("color_active")
  val colorActive: String,
  @SerializedName("color_answer_inactive")
  val colorAnswerInactive: String,
  @SerializedName("color_answer_normal")
  val colorAnswerNormal: String,
  @SerializedName("color_inactive")
  val colorInactive: String,
  @SerializedName("color_question")
  val colorQuestion: String
)