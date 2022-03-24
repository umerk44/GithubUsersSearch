package com.scalio.searchgithubuser.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.flowable
import com.scalio.searchgithubuser.core.Constant.PAGE_SIZE
import com.scalio.searchgithubuser.core.Constant.PRE_FETCH_DISTANCE
import com.scalio.searchgithubuser.core.toUser
import com.scalio.searchgithubuser.data.remote.api.UserSearchService
import com.scalio.searchgithubuser.data.repository.SearchUserSource
import com.scalio.searchgithubuser.model.User
import io.reactivex.Flowable
import javax.inject.Inject

class SearchUserRemoteImpl @Inject constructor(
    private val userSearchService: UserSearchService
) : SearchUserRemote {
    override fun searchUser(query: String): Flowable<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                prefetchDistance = PRE_FETCH_DISTANCE,
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchUserSource(
                    service = userSearchService,
                    query = query
                )
            }
        ).flowable.map { data -> data.map { userRemote -> userRemote.toUser() } }
    }
}