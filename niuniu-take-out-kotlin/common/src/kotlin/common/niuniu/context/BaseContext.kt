package common.niuniu.context

object BaseContext {
    private var threadLocal: ThreadLocal<Int> = ThreadLocal()

    var currentId: Int
        get() = threadLocal.get()
        set(id) {
            threadLocal.set(id)
        }

    fun removeCurrentId() {
        threadLocal.remove()
    }
}
