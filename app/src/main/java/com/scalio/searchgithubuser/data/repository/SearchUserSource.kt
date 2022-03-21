package com.scalio.searchgithubuser.data.repository

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.scalio.searchgithubuser.core.Constant.PAGE_SIZE
import com.scalio.searchgithubuser.data.remote.api.UserSearchService
import com.scalio.searchgithubuser.data.remote.model.SearchUserResponse
import com.scalio.searchgithubuser.data.remote.model.UserRemote
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class SearchUserSource(
    private val query: String,
    private val service: UserSearchService
) : RxPagingSource<Int, UserRemote>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, UserRemote>> {
        val page = params.key ?: 1
        return service.getUsers(query, PAGE_SIZE, page)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, page) }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(data: SearchUserResponse, page: Int): LoadResult<Int, UserRemote> {
        return LoadResult.Page(
            data = data.data,
            prevKey = if (page <= 1) null else page - 1,
            nextKey = if (data.totalCount == 1) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, UserRemote>): Int? {
        return state.anchorPosition
    }

}