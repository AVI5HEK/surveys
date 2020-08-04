package com.avi5hek.surveys.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by "Avishek" on 8/3/2020.
 */
data class ErrorResponse(
  @SerializedName("message") val message : String
)
