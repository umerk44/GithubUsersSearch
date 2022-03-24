package com.scalio.searchgithubuser.ui

import android.widget.SearchView

class SimpleQueryTextChangeListener(private val search: (String) -> Unit) : SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { search(it) }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let { search(it) } 
        return true
    }

}