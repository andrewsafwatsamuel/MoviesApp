package com.example.moviesapp

import androidx.paging.*
import androidx.paging.PagingSource.LoadParams

fun <T : Any> pagingDataSource(
    pageSize: Int,
    load: suspend (LoadParams<Int>,Int) -> PagingSource.LoadResult<Int, T>
) = object : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.getClosestPage()
        ?.run { prevKey?.plus(1) ?: nextKey?.minus(1) }

    private fun PagingState<Int, T>.getClosestPage() = anchorPosition?.let(::closestPageToPosition)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = load(params,pageSize)

}

fun <T : Any> createPager(
    pageSize: Int,
    source: PagingSource<Int, T>
) = Pager(
    config = PagingConfig(pageSize, enablePlaceholders = false),
    pagingSourceFactory = { source }
)