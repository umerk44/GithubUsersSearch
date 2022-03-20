package com.scalio.searchgithubuser.data.remote.model

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("items")
    val data: List<UserRemote>,
    @SerializedName("totalCount")
    val totalCount: Int
)