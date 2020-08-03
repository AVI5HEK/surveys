package com.avi5hek.surveys

import android.app.Application
import timber.log.Timber

/**
 * Created by "Avishek" on 8/2/2020.
 */
class SurveysApp : Application() {

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
