package com.lanut.ordering_backend.entity

import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.toJSONString

data class RestBean<T>(val code: Int, val data: T?, val message: String) {
    companion object {
        fun <T> success(data: T): RestBean<T> {
            return RestBean(200, data, "请求成功")
        }

        fun <T> failure(code: Int, message: String): RestBean<T> {
            return RestBean(code, null, message)
        }

        fun <T> failure(code: Int): RestBean<T> {
            return failure(code, "请求失败")
        }
    }

    fun asJsonString(): String {
        return this.toJSONString(JSONWriter.Feature.WriteNulls)
    }
}

fun <T> T.RestSuccess(): RestBean<T> {
    return RestBean.success(this)
}

fun String.RestFailure(code: Int): RestBean<String> {
    return RestBean.failure(code, this)
}
