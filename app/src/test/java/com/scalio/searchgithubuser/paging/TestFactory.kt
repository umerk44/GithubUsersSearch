package com.scalio.searchgithubuser.paging

import com.scalio.searchgithubuser.data.remote.model.UserRemote
import com.scalio.searchgithubuser.model.User
import java.util.*


object TestFactory {

    val dummySearchQuery get() = "john"
    val emptyQuery get() = ""
    val errorMessage get() = "404"


    fun createRemoteUser() = UserRemote(
        id = Math.random().toLong(),
        login = UUID.randomUUID().toString(),
        url = "http://github.com",
        type = "User"
    )

    fun createUser() = User(
        id = Math.random().toLong(),
        login = UUID.randomUUID().toString(),
        url = "http://github.com",
        type = "User"
    )

}