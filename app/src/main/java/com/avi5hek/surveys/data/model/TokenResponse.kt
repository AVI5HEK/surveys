package com.avi5hek.surveys.data.model

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class TokenResponse(
  @SerializedName("access_token")
  val accessToken: String,
  @SerializedName("created_at")
  val createdAt: DateTime,
  @SerializedName("expires_in")
  val expiresIn: DateTime,
  @SerializedName("token_type")
  val tokenType: String
)
