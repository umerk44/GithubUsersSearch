package com.scalio.searchgithubuser.di

import com.scalio.searchgithubuser.BuildConfig
import com.scalio.searchgithubuser.core.NetworkInterceptor
import com.scalio.searchgithubuser.data.remote.SearchUserRemote
import com.scalio.searchgithubuser.data.remote.SearchUserRemoteImpl
import com.scalio.searchgithubuser.data.remote.api.UserSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule() {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(NetworkInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): UserSearchService {
        return retrofit.create(UserSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRemote(userSearchService: UserSearchService): SearchUserRemote {
        return SearchUserRemoteImpl(userSearchService)
    }

}