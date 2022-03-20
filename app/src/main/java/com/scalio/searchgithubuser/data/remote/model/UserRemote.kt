package com.scalio.searchgithubuser.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserRemote(
    @SerializedName("id")
    val id: Long,
    @SerializedName("avatar_url")
    val url: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("type")
    val type: String
)