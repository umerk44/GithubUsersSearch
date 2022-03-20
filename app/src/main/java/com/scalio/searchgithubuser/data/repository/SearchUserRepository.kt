package com.scalio.searchgithubuser.data.repository

import androidx.paging.PagingData
import com.scalio.searchgithubuser.model.User
import io.reactivex.Flowable

interface SearchUserRepository{
    fun getUsers(query: String) : Flowable<Resource<PagingData<User>>>
}