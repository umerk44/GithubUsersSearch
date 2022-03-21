package com.scalio.searchgithubuser.di

import com.scalio.searchgithubuser.data.remote.SearchUserRemote
import com.scalio.searchgithubuser.data.repository.SearchUserRepository
import com.scalio.searchgithubuser.data.repository.SearchUserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    fun provideSearchUserRepo(searchUserRemote: SearchUserRemote): SearchUserRepository {
        return SearchUserRepositoryImpl(searchUserRemote)
    }
}