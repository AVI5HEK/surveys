package com.avi5hek.surveys.core

import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single

/**
 * Created by "Avishek" on 8/1/2020.
 */
abstract class GenericPagingSource<T : Any> :
  RxPagingSource<Int, T>() {

  override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, T>> {
    val position = params.key ?: 1
    return getSingle(position, params.loadSize)
      .map {
        LoadResult.Page(
          data = it,
          prevKey = if (position == 1) null else position - 1,
          nextKey = if (it.isEmpty()) null else position + 1
        ) as LoadResult<Int, T>
      }
      .onErrorReturn {
        LoadResult.Error(it)
      }
  }

  abstract fun getSingle(page: Int, pageSize: Int): Single<List<T>>
}
