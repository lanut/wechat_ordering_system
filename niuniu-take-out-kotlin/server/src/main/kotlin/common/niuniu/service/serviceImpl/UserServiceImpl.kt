package common.niuniu.service.serviceImpl

import com.alibaba.fastjson.JSON
import common.niuniu.constant.MessageConstant
import common.niuniu.dto.UserDTO
import common.niuniu.dto.UserLoginDTO
import common.niuniu.exception.LoginFailedException
import common.niuniu.mapper.UserMapper
import common.niuniu.po.User
import common.niuniu.properties.WeChatProperties
import common.niuniu.service.UserService
import common.niuniu.utils.HttpClientUtil.doGet
import common.niuniu.utils.logInfo
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl : UserService {
    @Autowired
    private lateinit var weChatProperties: WeChatProperties

    @Autowired
    private lateinit var userMapper: UserMapper


    /**
     * 用户微信登录
     *
     * @param userLoginDTO
     * @return
     */
    override fun wxLogin(userLoginDTO: UserLoginDTO?): User? {
        // 调用私有方法，其中利用HttpClient来调用微信API服务，获取openid
        val openid = getOpenId(userLoginDTO!!.code)
        // 判断openid是否为空，如果为空表示登录失败，抛出业务异常
        // 判断当前用户是否为新用户
        var user = userMapper.getByOpenid(openid)
        // 如果是新用户，自动完成注册，插入到数据库
        if (user == null) {
            user = User.builder()
                .openid(openid)
                .createTime(LocalDateTime.now())
                .build()
            userMapper.insert(user)
        }
        return user
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    override fun getUser(id: Int?): User? {
        return userMapper.getById(id)
    }

    /**
     * 修改用户信息
     * @param userDTO
     */
    override fun update(userDTO: UserDTO?) {
        val user = User()
        BeanUtils.copyProperties(userDTO!!, user)
        userMapper.update(user)
    }

    /**
     * 调用微信接口服务，获取微信用户的openid
     * 4参数： appid secret(在小程序平台查看，忘了就重置) 临时登录凭证code 常量authorization_code
     * @param code
     * @return
     */
    private fun getOpenId(code: String): String {
        // 调用微信接口服务，获得当前微信用户的openid
        val map: MutableMap<String, String> = HashMap()
        map["appid"] = weChatProperties.appid
        map["secret"] = weChatProperties.secret
        map["js_code"] = code
        map["grant_type"] = "authorization_code"
        // 利用HttpClient来调用微信的API服务，得到序列化好的json
        val json = doGet(WX_LOGIN, map) // 需自定义HttpClientUtil工具类
        // 解析返回的json对象，并抽取其中的openid
        val jsonObject = JSON.parseObject(json)
        logInfo(jsonObject.toString())
        val openid = jsonObject.getString("openid")
        return openid
    }

    companion object {
        // 微信服务接口地址
        const val WX_LOGIN: String = "https://api.weixin.qq.com/sns/jscode2session"
    }
}
