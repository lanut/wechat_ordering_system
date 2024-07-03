package common.niuniu.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// 日志扩展函数，不需要任何注解就可实现日志输出。
fun <T : Any> T.logger(): Logger {
    return LoggerFactory.getLogger(this.javaClass)
}

fun <T : Any> T.logError(e: Throwable) {
    logger().error("", e)
}

fun <T : Any> T.logError(msg: String) {
    logger().error(msg)
}

fun <T : Any> T.logInfo(msg: String) {
    logger().info(msg)
}

fun <T : Any> T.logDebug(msg: String) {
    logger().debug(msg)
}