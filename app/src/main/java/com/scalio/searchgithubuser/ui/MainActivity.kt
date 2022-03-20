package com.scalio.searchgithubuser.ui

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.scalio.searchgithubuser.databinding.ActivityMainBinding
import com.scalio.searchgithubuser.viewmodel.SearchUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    private val searchUserViewModel: SearchUserViewModel by viewModels()
    private val userAdapter: UserAdapter by lazy { UserAdapter() }

    override fun initViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    
}