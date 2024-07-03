package common.niuniu.annotation

import common.niuniu.enumeration.OperationType

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 * 注解接口@interface与普通接口类似，但它们只能包含抽象方法，并且这些方法不能有实现，用于定义自定义注解
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class AutoFill( //数据库操作类型：UPDATE INSERT
    val value: OperationType
)