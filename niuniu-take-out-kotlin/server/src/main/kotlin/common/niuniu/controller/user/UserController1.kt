package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.constant.JwtClaimsConstant
import common.niuniu.dto.UserDTO
import common.niuniu.dto.UserLoginDTO
import common.niuniu.po.*
import common.niuniu.properties.JwtProperties
import common.niuniu.result.Result
import common.niuniu.service.UserService
import common.niuniu.utils.JwtUtil.createJWT
import common.niuniu.vo.UserLoginVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/user")
class UserController {
    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val jwtProperties: JwtProperties? = null

    @PostMapping("/login")
    fun login(@RequestBody userLoginDTO: UserLoginDTO?): Result<UserLoginVO> {
        log.info("用户传过来的登录信息：{}", userLoginDTO)
        val user = userService!!.wxLogin(userLoginDTO)

        // 上面的没抛异常，正常来到这里，说明登录成功
        // claims就是用户数据payload部分
        val claims: MutableMap<String, Any> = HashMap() // jsonwebtoken包底层就是Map<String, Object>格式，不能修改！
        claims[JwtClaimsConstant.USER_ID] = user!!.id
        // 需要加个token给他，再返回响应
        val token = createJWT(
            jwtProperties!!.userSecretKey,
            jwtProperties.userTtl,
            claims
        )
        val userLoginVO = UserLoginVO.builder()
            .id(user.id)
            .openid(user.openid)
            .token(token)
            .build()
        return Result.success(userLoginVO)
    }

    /**
     * 根据id查询用户
     * @return
     */
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int?): Result<User> {
        log.info("用户id:{}", id)
        val user = userService!!.getUser(id)!!
        return Result.success(user)
    }

    /**
     * 修改用户信息
     * @param userDTO
     * @return
     */
    @PutMapping
    fun update(@RequestBody userDTO: UserDTO?): Result<*> {
        log.info("新的用户信息：{}", userDTO)
        userService!!.update(userDTO)
        return Result.success()
    }
}
