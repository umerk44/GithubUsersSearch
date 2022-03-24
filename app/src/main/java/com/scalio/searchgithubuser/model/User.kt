package com.scalio.searchgithubuser.model

data class User(
    val id: Long,
    val login: String?,
    val url: String?,
    val type: String?
)