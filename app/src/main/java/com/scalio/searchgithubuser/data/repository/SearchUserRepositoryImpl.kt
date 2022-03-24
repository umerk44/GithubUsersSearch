package com.scalio.searchgithubuser.data.repository

import androidx.paging.PagingData
import com.scalio.searchgithubuser.data.remote.SearchUserRemote
import com.scalio.searchgithubuser.model.User
import io.reactivex.Flowable
import javax.inject.Inject

class SearchUserRepositoryImpl @Inject constructor(private val searchUserRemote: SearchUserRemote) :
    SearchUserRepository {
    override fun getUsers(query: String): Flowable<Resource<PagingData<User>>> {
        return when {
            query.isNotEmpty() -> {
                searchUserRemote.searchUser(query)
                    .map { data -> Resource(data, Resource.Status.Success) }
            }
            else -> Flowable.just(Resource(PagingData.empty(), Resource.Status.Success))
        }
    }
}