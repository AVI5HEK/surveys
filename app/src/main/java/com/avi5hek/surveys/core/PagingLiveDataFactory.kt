package com.avi5hek.surveys.core

import androidx.lifecycle.LiveData
import androidx.paging.PagingData

/**
 * Created by "Avishek" on 8/7/2020.
 */
interface PagingLiveDataFactory<T : Any> {

  fun create(): LiveData<PagingData<T>>

  fun dispose()
}
