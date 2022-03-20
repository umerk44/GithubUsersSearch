package com.scalio.searchgithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseViewBindingActivity<T : ViewBinding> : AppCompatActivity() {

	protected lateinit var viewBinding: T
		private set

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewBinding = initViewBinding(layoutInflater)
		setContentView(viewBinding.root)
		initViews(savedInstanceState)
		subscribeToLiveData()
	}


	/**
	 * Inflate [T] here.
	 * Do not save the reference, it's done automatically
	 */
	abstract fun initViewBinding(inflater: LayoutInflater): T

	/**
	 * Initialize your views here.
	 * Use [viewBinding] to access them
	 */
	abstract fun initViews(savedInstanceState: Bundle?)

	/**
	 * Subscribe to LiveData here
	 */
	abstract fun subscribeToLiveData()

}