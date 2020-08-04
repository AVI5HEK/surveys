package com.avi5hek.surveys.data.model


import com.google.gson.annotations.SerializedName

data class Survey(
  @SerializedName("access_code_prompt")
  val accessCodePrompt: Any,
  @SerializedName("access_code_validation")
  val accessCodeValidation: String,
  @SerializedName("active_at")
  val activeAt: String,
  @SerializedName("cover_background_color")
  val coverBackgroundColor: Any,
  @SerializedName("cover_image_url")
  val coverImageUrl: String,
  @SerializedName("created_at")
  val createdAt: String,
  @SerializedName("default_language")
  val defaultLanguage: String,
  @SerializedName("description")
  val description: String,
  @SerializedName("footer_content")
  val footerContent: String,
  @SerializedName("id")
  val id: String,
  @SerializedName("inactive_at")
  val inactiveAt: Any,
  @SerializedName("is_access_code_required")
  val isAccessCodeRequired: Boolean,
  @SerializedName("is_access_code_valid_required")
  val isAccessCodeValidRequired: Boolean,
  @SerializedName("is_active")
  val isActive: Boolean,
  @SerializedName("language_list")
  val languageList: List<String>,
  @SerializedName("questions")
  val questions: List<Question>,
  @SerializedName("short_url")
  val shortUrl: String,
  @SerializedName("survey_version")
  val surveyVersion: Int,
  @SerializedName("tag_list")
  val tagList: String,
  @SerializedName("thank_email_above_threshold")
  val thankEmailAboveThreshold: String,
  @SerializedName("thank_email_below_threshold")
  val thankEmailBelowThreshold: String,
  @SerializedName("theme")
  val theme: Theme,
  @SerializedName("title")
  val title: String,
  @SerializedName("type")
  val type: String
)