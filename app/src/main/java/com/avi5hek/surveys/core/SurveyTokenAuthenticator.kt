package com.avi5hek.surveys.core

import com.avi5hek.surveys.core.di.module.NetworkModule
import com.avi5hek.surveys.data.dao.TokenDao
import com.avi5hek.surveys.data.service.TokenService
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.io.IOException

/**
 * Created by "Avishek" on 8/3/2020.
 */
class SurveyTokenAuthenticator
constructor(
  private val authService: TokenService,
  private val tokenDao: TokenDao
) : Authenticator {

  private var request: Request? = null

  @Throws(IOException::class)
  override fun authenticate(route: Route?, response: Response): Request? {
    Timber.d("authenticate() called for host: ${response.request.url.host}")
    this.request = null
    val tokenResponse = authService
      .refreshToken("password", "carlos@nimbl3.com", "antikera")
      .execute()

    val refreshTokenResponse = tokenResponse.body()

    if (refreshTokenResponse != null && tokenResponse.isSuccessful) {

      val accessToken = refreshTokenResponse.accessToken

      tokenDao.saveAccessToken(accessToken)

      this.request = response.request.newBuilder()
        .header(NetworkModule.HEADER_CONTENT_TYPE, NetworkModule.HEADER_CONTENT_TYPE_VALUE)
        .header(
          NetworkModule.HEADER_AUTHORIZATION,
          NetworkModule.HEADER_AUTHORIZATION_TYPE + accessToken
        )
        .build()
    }

    // return null to stop retrying once responseCount returns 3 or above.
    return if (responseCount(response) >= 3) {
      null
    } else this.request

  }

  private fun responseCount(response: Response?): Int {
    var result = 1
    while (response?.priorResponse != null) {
      result++
    }
    return result
  }
}
