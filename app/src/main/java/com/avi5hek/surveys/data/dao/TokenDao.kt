package com.avi5hek.surveys.data.dao

interface TokenDao {

  fun saveAccessToken(value: String)

  fun loadAccessToken(): String
}
