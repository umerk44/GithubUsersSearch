package com.scalio.searchgithubuser.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.scalio.searchgithubuser.data.remote.SearchUserRemote
import com.scalio.searchgithubuser.data.repository.Resource
import com.scalio.searchgithubuser.data.repository.SearchUserRepository
import com.scalio.searchgithubuser.data.repository.SearchUserRepositoryImpl
import com.scalio.searchgithubuser.model.User
import com.scalio.searchgithubuser.paging.TestFactory
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class SearchUserRepoTest {
    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

      private val searchUserRemote = mock<SearchUserRemote>()

    private lateinit var SUT: SearchUserRepository

    private val fakeUsers: List<User> = (1..18).map {
        TestFactory.createUser()
    }

     @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        SUT = SearchUserRepositoryImpl(searchUserRemote)

    }

    @Test
    fun `search user with empty string - success - empty data`() {
        val expected : PagingData<User> = PagingData.empty()
        whenever(searchUserRemote.searchUser(TestFactory.emptyQuery)).thenReturn(Flowable.just(PagingData.empty()))

        SUT.getUsers(TestFactory.emptyQuery).test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue{
                it.data?.equals(expected) ?: false
            }
            .dispose()

    }

    @Test
    fun `search user with valid string - success - valid data`() {
        val list = fakeUsers.subList(0,9)
        val data = PagingData.from(list)
        val expected = Resource(data, Resource.Status.Success)
        whenever(searchUserRemote.searchUser(TestFactory.dummySearchQuery)).thenReturn(Flowable.just(data))

        SUT.getUsers(TestFactory.dummySearchQuery).test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
            .dispose()

    }
}