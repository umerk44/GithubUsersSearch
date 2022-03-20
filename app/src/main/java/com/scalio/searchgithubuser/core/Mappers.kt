package com.scalio.searchgithubuser.core

import com.scalio.searchgithubuser.data.remote.model.UserRemote
import com.scalio.searchgithubuser.model.User

fun UserRemote.toUser(): User {
    return User(
        id = id,
        login = login,
        url = url,
        type = type
    )
}