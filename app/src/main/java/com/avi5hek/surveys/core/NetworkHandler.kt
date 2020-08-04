package com.avi5hek.surveys.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler
@Inject constructor(@ApplicationContext private val context: Context) :
  ConnectivityManager.NetworkCallback() {

  val isConnected: Boolean
    get() = run {
      val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

      if (Build.VERSION.SDK_INT < 23) {
        val ni = connectivityManager.activeNetworkInfo
        if (ni != null) {
          return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
        }
      } else {
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork != null) {
          val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
          return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI
          )
        }
      }

      return false
    }
}
