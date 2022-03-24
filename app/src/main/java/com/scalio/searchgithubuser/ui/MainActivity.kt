package com.scalio.searchgithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.scalio.searchgithubuser.R
import com.scalio.searchgithubuser.data.repository.Resource
import com.scalio.searchgithubuser.databinding.ActivityMainBinding
import com.scalio.searchgithubuser.model.User
import com.scalio.searchgithubuser.viewmodel.SearchUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    private val searchUserViewModel: SearchUserViewModel by viewModels()
    private val userAdapter: UserAdapter by lazy { UserAdapter() }

    private var searchView: SearchView? = null

    override fun initViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.content.users.adapter = userAdapter

        viewBinding.content.errorView.buttonTryAgain.setOnClickListener {
            searchView?.query.toString().let {  searchUserViewModel.searchUsers(it) }
        }
    }

    override fun subscribeToLiveData() {
        observeSearchRepositoriesLiveData()
    }

    private fun observeSearchRepositoriesLiveData() {
        searchUserViewModel.observeUserLiveData().observe(this, Observer { result ->
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchView = (menu?.findItem(R.id.action_search)?.actionView as SearchView)
        searchView.maxWidth = Integer.MAX_VALUE;
        searchView.setOnQueryTextListener( SimpleQueryTextChangeListener { query -> searchUserViewModel.searchUsers(query) })


        return true
    }
    private fun showProgress() {
        viewBinding.content.progress.visibility = VISIBLE
        viewBinding.content.errorView.root.visibility = GONE
    }

    private fun showData(result: Resource<PagingData<User>>) {
        result.data?.let { userAdapter.submitData(lifecycle, it) }
        viewBinding.content.users.visibility = VISIBLE
        viewBinding.content.progress.visibility = GONE
        viewBinding.content.errorView.root.visibility = GONE
    }


    private fun showError(message: String?) {
        viewBinding.content.users.visibility = GONE
        viewBinding.content.progress.visibility = GONE
        viewBinding.content.errorView.root.visibility = VISIBLE
        viewBinding.content.errorView.error.text = message
    }


}