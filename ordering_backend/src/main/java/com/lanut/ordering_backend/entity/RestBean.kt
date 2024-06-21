package com.lanut.ordering_backend.entity

import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.toJSONString
import java.text.SimpleDateFormat
import java.util.Date

data class RestBean<T>(val code: Int, val data: T?, val message: String) {

    @Suppress("unused")
    val dateTime: String = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date())

    companion object {
        @JvmStatic
        fun <T> success(data: T): RestBean<T> {
            return RestBean(200, data, "请求成功")
        }

        @JvmStatic
        fun failure(code: Int, message: String): RestBean<Any> {
            return RestBean(code, null, message)
        }

        @JvmStatic
        fun failure(code: Int): RestBean<Any> {
            return failure(code, "请求失败")
        }
    }

    fun asJsonString(): String {
        return this.toJSONString(JSONWriter.Feature.WriteNulls)
    }

    override fun toString(): String {
        return asJsonString()
    }
}

@Suppress("FunctionName")
fun <T> T.RestSuccess(): RestBean<T> {
    return RestBean.success(this)
}

@Suppress("FunctionName")
fun String.RestFailure(code: Int): RestBean<Any> {
    return RestBean.failure(code, this)
}

@Suppress("FunctionName")
fun String.RestAuthFailure(): RestBean<Any> {
    return RestBean.failure(401, this)
}

@Suppress("FunctionName")
fun String.RestForbidden(): RestBean<Any> {
    return RestBean.failure(403, this)
}