package com.scalio.searchgithubuser.data.repository

data class Resource<T>(val data: T?, val status: Status) {

	sealed class Status {
		object Loading : Status()
		object Success : Status()
		class Error(val error: Throwable) : Status()
	}
}