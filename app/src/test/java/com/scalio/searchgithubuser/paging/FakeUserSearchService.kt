package com.scalio.searchgithubuser.paging

import com.scalio.searchgithubuser.data.remote.api.UserSearchService
import com.scalio.searchgithubuser.data.remote.model.SearchUserResponse
import com.scalio.searchgithubuser.data.remote.model.UserRemote
import io.reactivex.Single

class FakeUserSearchService : UserSearchService {

    private val models = arrayListOf<UserRemote>()
    var failureMessage: String? = null

    fun addUsers(user: UserRemote) {
        models.add(user)
    }

    override fun getUsers(query: String, pageSize: Int, page: Int): Single<SearchUserResponse> {
        failureMessage?.let {
            return Single.error(RuntimeException(failureMessage, Throwable()))
        }

        return Single.just(SearchUserResponse(findBreeds(page, pageSize), models.size))
    }

    private fun findBreeds(page: Int, limit: Int): List<UserRemote> {
        val startingFrom = (page - 1) * limit
        if (startingFrom >= models.size) return arrayListOf()
        val until = (page - 1) * limit + limit
        val untilConsideringSize = if (until >= models.size) models.size else until
        return models.subList(startingFrom, untilConsideringSize)
    }
}