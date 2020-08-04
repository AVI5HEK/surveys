package com.avi5hek.surveys.data.model


import com.google.gson.annotations.SerializedName

data class Question(
  @SerializedName("answers")
  val answers: List<Answer>,
  @SerializedName("correct_answer_id")
  val correctAnswerId: Any,
  @SerializedName("cover_background_color")
  val coverBackgroundColor: Any,
  @SerializedName("cover_image_opacity")
  val coverImageOpacity: Double,
  @SerializedName("cover_image_url")
  val coverImageUrl: String,
  @SerializedName("display_order")
  val displayOrder: Int,
  @SerializedName("display_type")
  val displayType: String,
  @SerializedName("facebook_profile")
  val facebookProfile: Any,
  @SerializedName("font_face")
  val fontFace: Any,
  @SerializedName("font_size")
  val fontSize: Any,
  @SerializedName("help_text")
  val helpText: Any,
  @SerializedName("id")
  val id: String,
  @SerializedName("image_url")
  val imageUrl: String,
  @SerializedName("is_mandatory")
  val isMandatory: Boolean,
  @SerializedName("is_shareable_on_facebook")
  val isShareableOnFacebook: Boolean,
  @SerializedName("is_shareable_on_twitter")
  val isShareableOnTwitter: Boolean,
  @SerializedName("pick")
  val pick: String,
  @SerializedName("short_text")
  val shortText: String,
  @SerializedName("tag_list")
  val tagList: String,
  @SerializedName("text")
  val text: String,
  @SerializedName("twitter_profile")
  val twitterProfile: Any
)