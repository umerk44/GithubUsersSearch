package com.scalio.searchgithubuser.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.scalio.searchgithubuser.data.repository.Resource
import com.scalio.searchgithubuser.data.repository.SearchUserRepository
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

@RunWith(JUnit4::class)
class ViewModelTesting {
    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    private var userRepository = mock<SearchUserRepository>()
    private val userLiveDataObserver = mock<Observer<Resource<PagingData<User>>>>()

    private lateinit var SUT: SearchUserViewModel

    private val fakeUsers: List<User> = (1..18).map {
        TestFactory.createUser()
    }

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        SUT = SearchUserViewModel(userRepository)
        SUT.observeUserLiveData().observeForever(userLiveDataObserver)

    }

     @Test
     fun `get user data- success- repo search getUsers called once`() {

          whenever(userRepository.getUsers(TestFactory.dummySearchQuery)).thenReturn(Flowable.just(
              Resource(null, Resource.Status.Loading)
          ))

         SUT.searchUsers(TestFactory.dummySearchQuery)

         verify(userRepository, times(1)).getUsers(TestFactory.dummySearchQuery)

     }

    @Test
    fun `get user data from repo - success - initial state is loading`() {
        val data = PagingData.empty<User>()

        whenever(userRepository.getUsers(TestFactory.emptyQuery)).thenReturn(Flowable.just(
            Resource(data, Resource.Status.Loading)
        ))

        SUT.searchUsers(TestFactory.emptyQuery)

        verify(userLiveDataObserver).onChanged(Resource(data, Resource.Status.Loading))

    }

    @Test
    fun `get user data with empty query string - success - return empty data`() {
        val data = PagingData.empty<User>()

        whenever(userRepository.getUsers(TestFactory.emptyQuery)).thenReturn(Flowable.just(
            Resource(data, Resource.Status.Success)
        ))

        SUT.searchUsers(TestFactory.emptyQuery)

        verify(userLiveDataObserver).onChanged(Resource(data, Resource.Status.Success))

    }

    @Test
    fun `get user data with with some query string - success - return a list`() {
        val list = fakeUsers.subList(0,9)
        val data = PagingData.from(list)

        whenever(userRepository.getUsers(TestFactory.emptyQuery)).thenReturn(Flowable.just(
            Resource(data, Resource.Status.Success)
        ))

        SUT.searchUsers(TestFactory.emptyQuery)

        verify(userLiveDataObserver).onChanged(Resource(data, Resource.Status.Success))

    }

}