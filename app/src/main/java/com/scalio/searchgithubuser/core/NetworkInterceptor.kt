package com.scalio.searchgithubuser.core

import com.scalio.searchgithubuser.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequest = chain.request().newBuilder().apply {
            header("Authorization", "Bearer ${BuildConfig.ACCESS_TOKEN}")
        }.build()
        return chain.proceed(authRequest)
    }
}