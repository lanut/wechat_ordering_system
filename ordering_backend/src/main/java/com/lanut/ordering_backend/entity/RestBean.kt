package com.lanut.ordering_backend.entity

import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.toJSONString

data class RestBean<T>(val code: Int, val data: T?, val message: String) {
    companion object {
        @JvmStatic
        fun <T> success(data: T): RestBean<T> {
            return RestBean(200, data, "请求成功")
        }

        @JvmStatic
        fun <T> failure(code: Int, message: String): RestBean<T> {
            return RestBean(code, null, message)
        }

        @JvmStatic
        fun <T> failure(code: Int): RestBean<T> {
            return failure(code, "请求失败")
        }
    }

    fun asJsonString(): String {
        return this.toJSONString(JSONWriter.Feature.WriteNulls)
    }
}

@Suppress("FunctionName")
fun <T> T.RestSuccess(): RestBean<T> {
    return RestBean.success(this)
}

@Suppress("FunctionName")
fun String.RestFailure(code: Int): RestBean<String> {
    return RestBean.failure(code, this)
}

@Suppress("FunctionName")
fun String.RestAuthFailure(): RestBean<String> {
    return RestBean.failure(401, this)
}