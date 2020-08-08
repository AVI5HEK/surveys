package com.avi5hek.surveys.core

import androidx.paging.PagingData
import io.reactivex.Flowable

/**
 * Created by "Avishek" on 8/7/2020.
 */
interface PagingFlowableFactory<T : Any> {

  fun create(): Flowable<PagingData<T>>

  fun dispose()
}
