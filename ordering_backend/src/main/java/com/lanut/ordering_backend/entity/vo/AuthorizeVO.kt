package com.lanut.ordering_backend.entity.vo

import java.util.Date

/**
 * 授权信息

 */
data class AuthorizeVO(
    val nickname: String,
    val openid: String,
    val role: String,
    val token: String,
    val expire: Date
)