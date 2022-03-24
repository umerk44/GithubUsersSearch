package com.scalio.searchgithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.scalio.searchgithubuser.data.repository.Resource
import com.scalio.searchgithubuser.databinding.ActivityMainBinding
import com.scalio.searchgithubuser.model.User
import com.scalio.searchgithubuser.viewmodel.SearchUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    private val searchUserViewModel: SearchUserViewModel by viewModels()
    private val userAdapter: UserAdapter by lazy { UserAdapter() }

    override fun initViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        viewBinding.users.adapter = userAdapter
        viewBinding.searchView.setOnQueryTextListener(SimpleQueryTextChangeListener {
            searchUserViewModel.searchRepositoriesBy(it)
        })

        viewBinding.errorView.buttonTryAgain.setOnClickListener {
            searchUserViewModel.searchRepositoriesBy(viewBinding.searchView.query.toString())
        }
    }

    override fun subscribeToLiveData() {
        observeSearchRepositoriesLiveData()
    }

    private fun observeSearchRepositoriesLiveData() {
        searchUserViewModel.observeUserRepositoriesLiveData().observe(this, Observer { result ->
            when (result.status) {
                Resource.Status.Loading -> {
                    showProgress()
                }
                Resource.Status.Success -> {
                    showData(result)
                }
            }

            userAdapter.addLoadStateListener { state ->
                (state.refresh as? LoadState.Error)?.let { showError(it.error.message) }
            }
        })
    }

    private fun showProgress() {
        viewBinding.progress.visibility = VISIBLE
        viewBinding.errorView.root.visibility = GONE
    }

    private fun showData(result: Resource<PagingData<User>>) {
        result.data?.let { userAdapter.submitData(lifecycle, it) }
        viewBinding.users.visibility = VISIBLE
        viewBinding.progress.visibility = GONE
        viewBinding.errorView.root.visibility = GONE
    }


    private fun showError(message: String?) {
        viewBinding.users.visibility = GONE
        viewBinding.progress.visibility = GONE
        viewBinding.errorView.root.visibility = VISIBLE
        viewBinding.errorView.error.text = message
    }


}