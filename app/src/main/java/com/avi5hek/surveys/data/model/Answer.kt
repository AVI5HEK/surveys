package com.avi5hek.surveys.data.model


import com.google.gson.annotations.SerializedName

data class Answer(
  @SerializedName("alerts")
  val alerts: List<Any>,
  @SerializedName("date_constraint")
  val dateConstraint: Any,
  @SerializedName("default_value")
  val defaultValue: Any,
  @SerializedName("display_order")
  val displayOrder: Int,
  @SerializedName("display_type")
  val displayType: String,
  @SerializedName("help_text")
  val helpText: Any,
  @SerializedName("id")
  val id: String,
  @SerializedName("input_mask")
  val inputMask: Any,
  @SerializedName("input_mask_placeholder")
  val inputMaskPlaceholder: Any,
  @SerializedName("is_customer_email")
  val isCustomerEmail: Boolean,
  @SerializedName("is_customer_first_name")
  val isCustomerFirstName: Boolean,
  @SerializedName("is_customer_last_name")
  val isCustomerLastName: Boolean,
  @SerializedName("is_customer_title")
  val isCustomerTitle: Boolean,
  @SerializedName("is_mandatory")
  val isMandatory: Boolean,
  @SerializedName("prompt_custom_answer")
  val promptCustomAnswer: Boolean,
  @SerializedName("question_id")
  val questionId: String,
  @SerializedName("reference_identifier")
  val referenceIdentifier: Any,
  @SerializedName("response_class")
  val responseClass: String,
  @SerializedName("score")
  val score: Any,
  @SerializedName("short_text")
  val shortText: String,
  @SerializedName("text")
  val text: String,
  @SerializedName("weight")
  val weight: Any
)