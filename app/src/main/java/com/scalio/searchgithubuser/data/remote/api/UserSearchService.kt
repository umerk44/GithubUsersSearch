package com.scalio.searchgithubuser.data.remote.api

import com.scalio.searchgithubuser.data.remote.model.SearchUserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserSearchService {
    @GET("search/users?")
    fun getUsers(
        @Query("q") query: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int
    ): Single<SearchUserResponse>
}