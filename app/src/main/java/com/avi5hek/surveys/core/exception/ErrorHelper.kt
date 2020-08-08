package com.avi5hek.surveys.core.exception

import com.avi5hek.surveys.data.model.ErrorResponse
import io.reactivex.SingleTransformer
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

object ErrorHelper {

  fun <T> applyError(errorConverter: Converter<ResponseBody, ErrorResponse>): SingleTransformer<Response<T>, Response<T>> {
    return SingleTransformer {
      it.doOnSuccess { response ->
        if (!response.isSuccessful) {
          response?.errorBody()?.apply {
            if (isTokenExpired(response.code())) {
              throw TokenExpiredException("Authentication failed")
            }
            val errorResponse = errorConverter.convert(this)
            val message = errorResponse?.message ?: ""
            throw ServerException(message)
          } ?: throw ServerException()
        }
      }
    }
  }

  private fun isTokenExpired(code: Int): Boolean {
    return code == HTTP_UNAUTHORIZED
  }
}
