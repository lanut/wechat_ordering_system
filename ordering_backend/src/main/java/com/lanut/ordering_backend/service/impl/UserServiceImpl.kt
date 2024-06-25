package com.lanut.ordering_backend.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lanut.ordering_backend.entity.dto.Order
import com.lanut.ordering_backend.entity.dto.User
import com.lanut.ordering_backend.entity.vo.OrderDishVO
import com.lanut.ordering_backend.entity.vo.OrderedDishVO
import com.lanut.ordering_backend.entity.vo.OrdersVO
import com.lanut.ordering_backend.mapper.DishMapper
import com.lanut.ordering_backend.mapper.OrderDetailMapper
import com.lanut.ordering_backend.mapper.OrderMapper
import com.lanut.ordering_backend.mapper.UserMapper
import com.lanut.ordering_backend.service.IUserService
import com.lanut.ordering_backend.utils.JwtUtils
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Service
open class UserServiceImpl : ServiceImpl<UserMapper, User>(), IUserService {
    @Autowired
    private lateinit var dishMapper: DishMapper

    @Autowired
    private lateinit var orderDetailMapper: OrderDetailMapper

    @Resource
    lateinit var jwtUtils: JwtUtils

    @Resource
    lateinit var userMapper: UserMapper

    @Resource
    lateinit var orderMapper: OrderMapper

    override fun getUserByOpenid(openid: String): User? {
        return baseMapper.getUserByOpenId(openid)
    }

    override fun tokenToUser(token: String): User {
        val userDetail = jwtUtils.tokenToUserDetail(token)
        val openid = userDetail.openid
        return userMapper.getUserByOpenId(openid) ?: throw Exception("没有这个用户")
    }

}
