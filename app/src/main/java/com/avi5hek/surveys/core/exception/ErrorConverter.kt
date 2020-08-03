package com.avi5hek.surveys.core.exception

import com.avi5hek.surveys.core.di.qualifier.HostApiQualifier
import com.avi5hek.surveys.data.model.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorConverter
@Inject constructor(@HostApiQualifier private val retrofit: Retrofit): Converter<ResponseBody, ErrorResponse> {

  private val converter: Converter<ResponseBody, ErrorResponse> = retrofit.responseBodyConverter(
    ErrorResponse::class.java,
    arrayOfNulls(0)
  )

  override fun convert(value: ResponseBody): ErrorResponse? {
    return converter.convert(value)
  }
}
