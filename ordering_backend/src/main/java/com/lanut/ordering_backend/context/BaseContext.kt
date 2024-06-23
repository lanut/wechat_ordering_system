package com.lanut.ordering_backend.context

object BaseContext {
    private var threadLocal: ThreadLocal<String> = ThreadLocal()

    var currentOpenid: String
        get() = threadLocal.get()
        set(openid) {
            threadLocal.set(openid)
        }

    fun removeCurrentId() {
        threadLocal.remove()
    }
}
