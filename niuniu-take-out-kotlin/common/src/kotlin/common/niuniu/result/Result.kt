package common.niuniu.result

import java.io.Serializable

data class Result<T>(
    val code: Int = 0, // 编码：0成功，1和其它数字为失败
    val msg: String? = null, // 错误信息
    val data: T? = null // 数据
) : Serializable {

    companion object {
        fun success(): Result<Nothing> {
            return Result(code = 0)
        }

        fun <T> success(data: T): Result<T> {
            return Result(code = 0, data = data)
        }

        fun <T> error(msg: String): Result<T> {
            return Result(code = 1, msg = msg)
        }
    }
}

// 扩展函数
fun <T> T.toSuccessResult(): Result<T> = Result(code = 0, data = this)

fun String.toErrorResult(): Result<Nothing> = Result(code = 1, msg = this)