package com.scalio.searchgithubuser.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.scalio.searchgithubuser.data.remote.model.UserRemote
import com.scalio.searchgithubuser.data.repository.SearchUserSource
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
class PagingSourceTest {
    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    private lateinit var userPagingSource: SearchUserSource

    private val fakeUserUserRemotes: List<UserRemote> = (1..27).map {
        TestFactory.createRemoteUser()
    }

    private val fakeGitHubService = FakeUserSearchService().apply {
        fakeUserUserRemotes.forEach { addUsers(it) }
    }


    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        userPagingSource = SearchUserSource(TestFactory.dummySearchQuery, fakeGitHubService)


    }

    @Test
    fun `users paging source load - failure - error`() {
        val errorMessage = TestFactory.errorMessage
        val error = RuntimeException(errorMessage, Throwable())
        val expectedResult = PagingSource.LoadResult.Error<Int, UserRemote>(error)
        fakeGitHubService.failureMessage = errorMessage


        userPagingSource.loadSingle(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 1,
                placeholdersEnabled = false
            )
        ).test()
            .await()
            .assertValueCount(1)
            .assertValue {
                ((it as PagingSource.LoadResult.Error).throwable.message?.equals(expectedResult.throwable.message) ?: false)
            }

        fakeGitHubService.failureMessage = null
    }

    @Test
    fun `users paging source loads - success - valid value`() {
        val refreshRequest = PagingSource.LoadParams.Refresh<Int>(null, 9, false)

        userPagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertNoErrors()
            .assertValue(
                PagingSource.LoadResult.Page(fakeUserUserRemotes.subList(0, 9), null, 2)
            )
    }


    @Test
    fun `users paging source loads out of page size - success - empty list`() {
        val refreshRequest = PagingSource.LoadParams.Append<Int>(4, 9, false)

        userPagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertNoErrors()
            .assertValue{ (it as PagingSource.LoadResult.Page).data.isEmpty() }
    }

}