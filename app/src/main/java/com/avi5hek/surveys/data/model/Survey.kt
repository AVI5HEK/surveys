package com.avi5hek.surveys.data.model


import com.google.gson.annotations.SerializedName

data class Survey(
  @SerializedName("id")
  val id: String,
  @SerializedName("title")
  val title: String,
  @SerializedName("description")
  val description: String,
  @SerializedName("cover_image_url")
  val coverImageUrl: String
)
