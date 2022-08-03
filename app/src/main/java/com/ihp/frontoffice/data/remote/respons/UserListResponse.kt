package com.ihp.frontoffice.data.remote.respons

data class UserListResponse(
	val data: List<ListUser?>? = null,
	val length: Int? = null,
	val state: Boolean? = null,
	val message: String? = null
)

data class ListUser(
	val username: String? = null
)

