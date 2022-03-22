package com.scalio.searchgithubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.scalio.searchgithubuser.data.repository.Resource
import com.scalio.searchgithubuser.data.repository.SearchUserRepository
import com.scalio.searchgithubuser.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(private val userRepository: SearchUserRepository) : ViewModel() {

    private var dispose: CompositeDisposable = CompositeDisposable()

    private val getUserLiveData: MutableLiveData<Resource<PagingData<User>>> = MutableLiveData()

    fun observeUserRepositoriesLiveData(): LiveData<Resource<PagingData<User>>> = getUserLiveData

    fun searchRepositoriesBy(query: String) {
        getUserLiveData.value = Resource(null, Resource.Status.Loading)
        dispose.addAll(
            userRepository.getUsers(query)
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { users ->
                    getUserLiveData.value = users
                }
        )

    }

    override fun onCleared() {
        super.onCleared()
        dispose.dispose()
    }

}


