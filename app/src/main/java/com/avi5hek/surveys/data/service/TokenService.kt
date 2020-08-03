package com.avi5hek.surveys.data.service

import com.avi5hek.surveys.core.constant.REFRESH_TOKEN
import com.avi5hek.surveys.data.model.TokenResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

/**
 * Created by "Avishek" on 8/3/2020.
 */
interface TokenService {

  @POST(REFRESH_TOKEN)
  fun refreshToken(
    @Field("grant_type") grantType: String,
    @Field("username") username: String,
    @Field("password") password: String
  ): Call<TokenResponse>
}
