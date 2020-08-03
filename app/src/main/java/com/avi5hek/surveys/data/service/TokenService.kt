package com.avi5hek.surveys.data.service

import com.avi5hek.surveys.core.constant.REFRESH_TOKEN
import com.avi5hek.surveys.data.model.TokenResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by "Avishek" on 8/3/2020.
 */
interface TokenService {

  @POST(REFRESH_TOKEN)
  fun refreshToken(@Body requestBody: RequestBody): Call<TokenResponse>
}
