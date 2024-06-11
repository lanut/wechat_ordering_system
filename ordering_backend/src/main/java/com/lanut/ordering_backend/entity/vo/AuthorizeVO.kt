package com.lanut.ordering_backend.entity.vo

import java.util.Date

data class AuthorizeVO(
    val username: String,
    val role: String,
    val token: String,
    val expire: Date
)