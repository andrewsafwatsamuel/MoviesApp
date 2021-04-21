package com.example.moviesapp

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.PagingSource.LoadParams

fun <T : Any> pagingDataSource(
    load: suspend (LoadParams<Int>) -> PagingSource.LoadResult<Int, T>
) = object : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.getClosestPage()
        ?.run { prevKey?.plus(1) ?: nextKey?.minus(1) }

    private fun PagingState<Int, T>.getClosestPage() = anchorPosition?.let(::closestPageToPosition)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = load(params)

}