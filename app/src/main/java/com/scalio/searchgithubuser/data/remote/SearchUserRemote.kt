package com.scalio.searchgithubuser.data.remote

import androidx.paging.PagingData
import com.scalio.searchgithubuser.data.remote.model.UserRemote
import com.scalio.searchgithubuser.model.User
import io.reactivex.Flowable

interface SearchUserRemote {
    fun searchUser(query: String): Flowable<PagingData<UserRemote>>
}