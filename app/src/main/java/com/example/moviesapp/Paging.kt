package com.example.moviesapp

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState

fun <T : Any> createPager(
    pageSize: Int,
    load: suspend (params: LoadParams<Int>) -> LoadResult<Int, T>,
    pagingSource: PagingSource<Int, T> = createPagingSource(load)
) = Pager(
    config = PagingConfig(pageSize, enablePlaceholders = false),
    pagingSourceFactory = { pagingSource }
)

fun <T : Any> createPagingSource(
    load: suspend (params: LoadParams<Int>) -> LoadResult<Int, T>
) = object : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.setRefreshKey()

    private fun <T : Any> PagingState<Int, T>.setRefreshKey() =
        getClosestPosition()?.prevKey?.plus(1) ?: getClosestPosition()?.nextKey?.minus(1)

    private fun <T : Any> PagingState<Int, T>.getClosestPosition() =
        anchorPosition?.let(::closestPageToPosition)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = load(params)

}