package com.avi5hek.surveys.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.avi5hek.surveys.core.constant.ACCESS_TOKEN
import com.avi5hek.surveys.data.dao.TokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferenceHelper
@Inject
constructor(private val sharedPreferences: SharedPreferences) : TokenDao {

  override fun saveAccessToken(value: String) {
    sharedPreferences.edit { putString(ACCESS_TOKEN, value) }
  }

  override fun loadAccessToken(): String {
    return sharedPreferences.getString(ACCESS_TOKEN, "")!!
  }
}
