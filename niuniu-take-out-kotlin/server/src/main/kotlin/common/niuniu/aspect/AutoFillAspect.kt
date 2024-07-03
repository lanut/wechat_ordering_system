package common.niuniu.aspect

import common.niuniu.annotation.AutoFill
import common.niuniu.constant.AutoFillConstant
import common.niuniu.context.BaseContext.currentId
import common.niuniu.enumeration.OperationType
import common.niuniu.utils.logInfo
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@Aspect
class AutoFillAspect {
    /**
     * 切入点表达式，封装成一个小方法
     */
    @Pointcut("execution(* common.niuniu.mapper.*.*(..)) && @annotation(common.niuniu.annotation.AutoFill)")
    fun autoFillPointCut() {
    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()") // 调用上面的切入点表达式
    fun autoFill(joinPoint: JoinPoint) {
        // 可先进行调试，是否能进入该方法 提前在mapper方法添加AutoFill注解
        logInfo("开始进行公共字段(create_time等)的自动填充...")
        // 获取到当前被拦截的方法上的数据库操作类型
        val signature = joinPoint.signature as MethodSignature // 方法签名对象
        val autoFill = signature.method.getAnnotation(AutoFill::class.java) // 获得方法上的注解对象
        val operationType = autoFill.value // 获得数据库操作类型
        // 获取到当前被拦截的方法的参数--实体对象
        val args = joinPoint.args
        if (args == null || args.isEmpty()) {
            return
        }
        val entity = args[0]
        // 准备赋值的数据：自动填充当前时间
        val now = LocalDateTime.now()
        val currentId = currentId
        // 根据当前不同的操作类型，为对应的属性通过反射来赋值
        when (operationType) {
            OperationType.INSERT -> {
                // 为4个公共字段(时间字段)赋值，刚开始插入需要设置create字段
                try {
                    val setCreateUser =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Integer::class.java)
                    val setUpdateUser =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Integer::class.java)
                    val setCreateTime =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime::class.java)
                    val setUpdateTime =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime::class.java)
                    // 通过反射为对象属性赋值
                    setCreateUser.invoke(entity, currentId)
                    setUpdateUser.invoke(entity, currentId)
                    setCreateTime.invoke(entity, now)
                    setUpdateTime.invoke(entity, now)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            OperationType.REG -> {
                // 为注册的2个公共时间字段赋值，因为一开始无法获得线程id(管理员登录新增员工那个才有)，user字段手动设置100表示自己操作
                try {
                    val setCreateTime =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime::class.java)
                    val setUpdateTime =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime::class.java)
                    // 通过反射为对象属性赋值
                    setCreateTime.invoke(entity, now)
                    setUpdateTime.invoke(entity, now)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            OperationType.UPDATE -> {
                // 为2个公共字段赋值
                try {
                    val setUpdateUser =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Integer::class.java)
                    val setUpdateTime =
                        entity.javaClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime::class.java)
                    // 通过反射为对象属性赋值
                    setUpdateUser.invoke(entity, currentId)
                    setUpdateTime.invoke(entity, now)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
