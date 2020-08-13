package com.avi5hek.surveys.core

import com.avi5hek.surveys.BuildConfig
import com.avi5hek.surveys.core.di.module.NetworkModule
import com.avi5hek.surveys.data.dao.TokenDao
import com.avi5hek.surveys.data.service.TokenService
import okhttp3.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by "Avishek" on 8/3/2020.
 */
class SurveyTokenAuthenticator
@Inject
constructor(
  private val authService: TokenService,
  private val tokenDao: TokenDao
) : Authenticator {

  @Throws(IOException::class)
  override fun authenticate(route: Route?, response: Response): Request? {
    Timber.d("authenticate() called for host: ${response.request.url.host}")

    return if (responseCount(response) >= 2) {
      null
    } else {
      val requestBody: RequestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("grant_type", BuildConfig.GRANT_TYPE)
        .addFormDataPart("username", BuildConfig.USERNAME)
        .addFormDataPart("password", BuildConfig.PASSWORD)
        .build()

      val tokenResponse = authService
        .refreshToken(requestBody)
        .execute()

      val refreshTokenResponse = tokenResponse.body()

      if (refreshTokenResponse != null && tokenResponse.isSuccessful) {

        val accessToken = refreshTokenResponse.accessToken

        tokenDao.saveAccessToken(accessToken)

        response.request.newBuilder()
          .header(NetworkModule.HEADER_CONTENT_TYPE, NetworkModule.HEADER_CONTENT_TYPE_VALUE)
          .header(
            NetworkModule.HEADER_AUTHORIZATION,
            NetworkModule.HEADER_AUTHORIZATION_TYPE + accessToken
          )
          .build()
      } else {
        null
      }
    }
  }

  private fun responseCount(response: Response?): Int {
    var result = 1
    while (response?.priorResponse != null) {
      result++
    }
    return result
  }
}
